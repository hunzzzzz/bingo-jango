package team.b2.bingojango.domain.food.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.b2.bingojango.domain.food.repository.FoodRepository
import team.b2.bingojango.domain.product.model.Product
import team.b2.bingojango.domain.purchase.model.PurchaseStatus
import team.b2.bingojango.domain.purchase.repository.PurchaseRepository
import team.b2.bingojango.domain.purchase.service.PurchaseService
import team.b2.bingojango.domain.purchase_product.model.PurchaseProduct
import team.b2.bingojango.domain.purchase_product.repository.PurchaseProductRepository
import team.b2.bingojango.domain.food.dto.AddFoodRequest
import team.b2.bingojango.domain.food.dto.UpdateFoodRequest
import team.b2.bingojango.domain.food.model.Food
import team.b2.bingojango.domain.food.model.FoodCategory
import team.b2.bingojango.domain.refrigerator.repository.RefrigeratorRepository
import java.time.ZonedDateTime

@Service
@Transactional
class FoodService(
    private val foodRepository: FoodRepository,
    private val refrigeratorRepository: RefrigeratorRepository,
    private val purchaseService: PurchaseService,
    private val purchaseRepository: PurchaseRepository,
    private val purchaseProductRepository: PurchaseProductRepository
) {
    // [API] 해당 식품을 n개 만큼 공동구매 신청
    fun addFoodToPurchase(refrigeratorId: Long, foodId: Long, count: Int) =
        getCurrentPurchase(refrigeratorId).let {
            checkAlreadyInPurchase() // TODO : 현재 공동구매 진행 중인 식품은 추가할 수 없음을 검증해야 함
            purchaseProductRepository.save(
                PurchaseProduct(
                    count = count,
                    purchase = it,
                    product = Product(getFood(foodId)) // TODO : 추후에 연관관계에 따른 별도의 객체 생성 로직 필요
                )
            )
            "${getFood(foodId).name} ${count}개가 공동구매 신청되었습니다." // TODO: 추후 분리 예정
        }

    /*
        [내부 메서드] 현재 진행 중인 Purchase (공동구매)를 리턴
            - status 가 ON_VOTE (투표 진행중) 인 Purchase 확인
            - status 가 ON_VOTE (투표 진행중) 인 Purchase 가 없다면, 새로운 Purchase 객체를 생성 후 리턴
            * TODO : 추후 조회 과정 리팩토링 필요
     */
    private fun getCurrentPurchase(refrigeratorId: Long) =
        purchaseRepository.findAll().firstOrNull { it.status == PurchaseStatus.ON_VOTE }
            ?: purchaseService.makePurchase(refrigeratorId)

    /*
        [내부 메서드] 현재 공동구매 진행 중인 식품은 공동구매 목록에 추가할 수 없음을 검증
            * TODO : 추후 구현 예정
     */
    private fun checkAlreadyInPurchase() {}

    // [내부 메서드] id로 Food 객체 가져오기
    private fun getFood(foodId: Long) =
        foodRepository.findByIdOrNull(foodId) ?: throw Exception("") // TODO


    @Transactional
    fun addFood(refrigeratorId: Long, request: AddFoodRequest){

        val findRefrigerator = refrigeratorRepository.findByIdOrNull(refrigeratorId) ?: throw Exception("냉장고를 찾을 수 없습니다.")
        //해당 냉장고에 추가한 음식이 이미 있으면 이미 존재하는 음식이라고 응답하기?
        val newFood = Food(
                category = request.category,
                name = request.name,
                expirationDate = ZonedDateTime.now(),
                count = request.count,
                refrigerator = findRefrigerator
        )
        foodRepository.save(newFood)
    }

    @Transactional
    fun updateFood(refrigeratorId: Long, foodId: Long, request: UpdateFoodRequest){
        val findRefrigerator = refrigeratorRepository.findByIdOrNull(refrigeratorId) ?: throw Exception("냉장고를 찾을 수 없습니다.")
        val findFood = foodRepository.findByIdOrNull(foodId) ?: throw Exception("음식을 찾을 수 없습니다.")
        if (findFood.refrigerator?.id != findRefrigerator.id){throw Exception("냉장고에 음식이 존재하지 않습니다.")}
        findFood.category = FoodCategory.valueOf(request.category)
        findFood.name = request.name
        //findFood.expirationDate = request.expirationDate
        foodRepository.save(findFood)
    }

    @Transactional
    fun updateFoodCount(refrigeratorId: Long, foodId: Long, count: Int){
        val findRefrigerator = refrigeratorRepository.findByIdOrNull(refrigeratorId) ?: throw Exception("냉장고를 찾을 수 없습니다.")
        val findFood = foodRepository.findByIdOrNull(foodId) ?: throw Exception("음식을 찾을 수 없습니다.")
        if (findFood.refrigerator?.id != findRefrigerator.id){throw Exception("냉장고에 음식이 존재하지 않습니다.")}
        findFood.count = count
        foodRepository.save(findFood)
    }

    @Transactional
    fun deleteFood(refrigeratorId: Long, foodId: Long){
        val findRefrigerator = refrigeratorRepository.findByIdOrNull(refrigeratorId) ?: throw Exception("냉장고를 찾을 수 없습니다.")
        val findFood = foodRepository.findByIdOrNull(foodId) ?: throw Exception("음식을 찾을 수 없습니다.")
        if (findFood.refrigerator?.id != findRefrigerator.id){throw Exception("냉장고에 음식이 존재하지 않습니다.")}
        foodRepository.delete(findFood)
    }
}