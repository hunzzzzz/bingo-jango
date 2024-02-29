package team.b2.bingojango.domain.food.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.b2.bingojango.domain.food.model.Food
import team.b2.bingojango.domain.member.model.MemberRole
import team.b2.bingojango.domain.product.model.Product
import team.b2.bingojango.domain.product.repository.ProductRepository
import team.b2.bingojango.domain.purchase.model.Purchase
import team.b2.bingojango.domain.purchase.model.PurchaseStatus
import team.b2.bingojango.domain.purchase.repository.PurchaseRepository
import team.b2.bingojango.domain.purchase_product.model.PurchaseProduct
import team.b2.bingojango.domain.purchase_product.repository.PurchaseProductRepository
import team.b2.bingojango.domain.food.dto.AddFoodRequest
import team.b2.bingojango.domain.food.dto.FoodResponse
import team.b2.bingojango.domain.food.dto.UpdateFoodRequest
import team.b2.bingojango.domain.food.model.FoodCategory
import team.b2.bingojango.domain.food.repository.FoodRepository
import team.b2.bingojango.domain.purchase.service.PurchaseService
import team.b2.bingojango.domain.refrigerator.repository.RefrigeratorRepository
import java.time.ZonedDateTime
import team.b2.bingojango.domain.refrigerator.model.Refrigerator
import team.b2.bingojango.global.exception.cases.*
import team.b2.bingojango.global.security.UserPrincipal
import team.b2.bingojango.global.util.EntityFinder
import team.b2.bingojango.global.util.ZonedDateTimeConverter

@Service
@Transactional
class FoodService(
        private val foodRepository: FoodRepository,
        private val refrigeratorRepository: RefrigeratorRepository,
        private val purchaseService: PurchaseService,
        private val productRepository: ProductRepository,
        private val purchaseRepository: PurchaseRepository,
        private val purchaseProductRepository: PurchaseProductRepository,
        private val entityFinder: EntityFinder
) {
    fun getFood(refrigeratorId: Long): List<FoodResponse> {
        return foodRepository.findAll().map {
            FoodResponse(
                    category = it.category.name,
                    name = it.name,
                    expirationDate = ZonedDateTimeConverter.convertZonedDateTimeFromStringDateTime(it.expirationDate),
                    count = it.count,
            )
        }
    }


    @Transactional
    fun addFood(refrigeratorId: Long, request: AddFoodRequest) {
        val findRefrigerator = refrigeratorRepository.findByIdOrNull(refrigeratorId)
                ?: throw ModelNotFoundException("냉장고를 찾을 수 없습니다.")
        //해당 냉장고에 동일 음식이 있을 경우
        //테스트 시 오류나면 refrigeratorId가 아니라 findRefrigerator 로 변경해보기
        val existsFood = foodRepository.existsByRefrigeratorIdAndName(refrigeratorId, request.name)
        if (existsFood) {
            throw AlreadyExistsException("음식")
        }
        val newFood = Food(
                category = request.category,
                name = request.name,
                expirationDate = ZonedDateTimeConverter.convertStringDateFromZonedDateTime(request.expirationDate),
                count = request.count,
                refrigerator = findRefrigerator
        )
        foodRepository.save(newFood)
    }

    @Transactional
    fun updateFood(refrigeratorId: Long, foodId: Long, request: UpdateFoodRequest) {
        val findRefrigerator = refrigeratorRepository.findByIdOrNull(refrigeratorId)
                ?: throw ModelNotFoundException("냉장고를 찾을 수 없습니다.")
        val findFood = foodRepository.findByIdOrNull(foodId) ?: throw ModelNotFoundException("음식을 찾을 수 없습니다.")
        if (findFood.refrigerator?.id != findRefrigerator.id) {
            throw ModelNotFoundException("냉장고에 음식이 존재하지 않습니다.")
        }
        //기존 음식 이름과 변경한 음식 이름이 같으면 pass(변경 값 저장)
        //이름이 다를때, 변경한 음식의 이름이 이미 존재하면 이미 존재하는 음식이라고 알려주기
        if (findFood.name != request.name) {
            val existsFoodName = foodRepository.existsByRefrigeratorIdAndName(refrigeratorId, request.name)
            if (existsFoodName) {
                throw AlreadyExistsException("음식")
            }
        }
        findFood.category = FoodCategory.valueOf(request.category)
        findFood.name = request.name
        findFood.expirationDate = ZonedDateTimeConverter.convertStringDateFromZonedDateTime(request.expirationDate)
        foodRepository.save(findFood)
    }

    @Transactional
    fun updateFoodCount(refrigeratorId: Long, foodId: Long, count: Int) {
        val findRefrigerator = refrigeratorRepository.findByIdOrNull(refrigeratorId)
                ?: throw ModelNotFoundException("냉장고를 찾을 수 없습니다.")
        val findFood = foodRepository.findByIdOrNull(foodId) ?: throw ModelNotFoundException("음식을 찾을 수 없습니다.")
        if (findFood.refrigerator?.id != findRefrigerator.id) {
            throw ModelNotFoundException("냉장고에 음식이 존재하지 않습니다.")
        }
        findFood.count = count
        foodRepository.save(findFood)
    }

    @Transactional
    fun deleteFood(refrigeratorId: Long, foodId: Long) {
        val findRefrigerator = refrigeratorRepository.findByIdOrNull(refrigeratorId)
                ?: throw ModelNotFoundException("냉장고를 찾을 수 없습니다.")
        val findFood = foodRepository.findByIdOrNull(foodId) ?: throw ModelNotFoundException("음식을 찾을 수 없습니다.")
        if (findFood.refrigerator?.id != findRefrigerator.id) {
            throw ModelNotFoundException("냉장고에 음식이 존재하지 않습니다.")
        }
        foodRepository.delete(findFood)
    }

    /*
        [API] 해당 식품을 n개 만큼 공동구매 신청
            - 검증 조건 1 : 관리자(STAFF)만 공동구매를 신청할 수 있음
            - 검증 조건 2 : 현재 공동구매 중인 식품은 추가할 수 없음
     */
    fun addFoodToPurchase(userPrincipal: UserPrincipal, refrigeratorId: Long, foodId: Long, count: Int) =
            getCurrentPurchase(userPrincipal, refrigeratorId).let {
                if (entityFinder.getMember(userPrincipal.id, refrigeratorId).role != MemberRole.STAFF)
                    throw InvalidRoleException()
                else if (purchaseProductRepository.findAllByPurchase(getCurrentPurchase())
                                .map { purchaseProduct -> purchaseProduct.product.food }
                                .contains(entityFinder.getFood(foodId))
                ) throw AlreadyInPurchaseException()

                purchaseProductRepository.save(
                        PurchaseProduct(
                                count = count,
                                purchase = it,
                                product = getProduct(foodId, refrigeratorId),
                                refrigerator = entityFinder.getRefrigerator(refrigeratorId)
                        )
                )
                "${entityFinder.getFood(foodId).name} ${count}개가 공동구매 신청되었습니다." // TODO: 추후 분리 예정
            }

    /*
        [API] 공동구매 목록에서 특정 식품 삭제
            - 검증 조건 1: 해당 공동구매를 올린 사람만 삭제를 할 수 있음
            - 검증 조건 2: 현재 공동구매에 존재하는 식품만 삭제할 수 있음
     */
    fun deleteFoodFromPurchase(userPrincipal: UserPrincipal, refrigeratorId: Long, foodId: Long) {
        if (getCurrentPurchase().proposedBy != userPrincipal.id)
            throw InvalidRoleException()
        purchaseProductRepository.delete(
                purchaseProductRepository.findByRefrigeratorAndProduct(
                        refrigerator = entityFinder.getRefrigerator(refrigeratorId),
                        product = getProduct(foodId, refrigeratorId)
                ) ?: throw ModelNotFoundException("식품")
        )
    }

    // [내부 메서드] 현재 진행 중인(status 가 ACTIVE 한) Purchase 를 리턴 (없으면 예외 처리)
    private fun getCurrentPurchase() =
            purchaseRepository.findAll().firstOrNull { it.status == PurchaseStatus.ACTIVE }
                    ?: throw NoCurrentPurchaseException()

    // [내부 메서드] 현재 진행 중인(status 가 ACTIVE 한) Purchase 를 리턴 (없으면 새로운 Purchase 객체 생성 후 리턴)
    private fun getCurrentPurchase(userPrincipal: UserPrincipal, refrigeratorId: Long) =
            purchaseRepository.findAll().firstOrNull { it.status == PurchaseStatus.ACTIVE }
                    ?: makePurchase(userPrincipal, entityFinder.getRefrigerator(refrigeratorId))

    // [내부 메서드] Purchase 객체 생성
    private fun makePurchase(userPrincipal: UserPrincipal, refrigerator: Refrigerator) =
            purchaseRepository.save(
                    Purchase(
                            status = PurchaseStatus.ACTIVE,
                            proposedBy = userPrincipal.id,
                            refrigerator = refrigerator
                    )
            )

    // [내부 메서드] 현재 냉장고 내에 등록된 (해당 식품에 대한) Product 를 리턴 (없으면 새로운 Product 객체 생성 후 리턴)
    private fun getProduct(foodId: Long, refrigeratorId: Long): Product =
            productRepository.findByFoodAndRefrigerator(
                    entityFinder.getFood(foodId),
                    entityFinder.getRefrigerator(refrigeratorId)
            )
                    ?: addProduct(entityFinder.getFood(foodId), entityFinder.getRefrigerator(refrigeratorId))

    // [내부 메서드] Product 객체 생성
    fun addProduct(food: Food, refrigerator: Refrigerator) =
            productRepository.save(
                    Product(food = food, refrigerator = refrigerator)
            )
}