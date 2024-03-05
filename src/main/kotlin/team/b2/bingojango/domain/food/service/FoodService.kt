package team.b2.bingojango.domain.food.service

import org.springframework.data.domain.Page
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.b2.bingojango.domain.food.model.Food
import team.b2.bingojango.domain.product.repository.ProductRepository
import team.b2.bingojango.domain.purchase.repository.PurchaseRepository
import team.b2.bingojango.domain.purchase_product.repository.PurchaseProductRepository
import team.b2.bingojango.domain.food.dto.AddFoodRequest
import team.b2.bingojango.domain.food.dto.FoodResponse
import team.b2.bingojango.domain.food.dto.UpdateFoodRequest
import team.b2.bingojango.domain.food.model.FoodCategory
import team.b2.bingojango.domain.food.model.SortFood
import team.b2.bingojango.domain.food.repository.FoodRepository
import team.b2.bingojango.domain.purchase.service.PurchaseService
import team.b2.bingojango.domain.refrigerator.repository.RefrigeratorRepository
import team.b2.bingojango.global.exception.cases.*
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
            throw AlreadyExistsFoodException()
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
                throw AlreadyExistsFoodException()
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

    //음식 검색 및 정렬
    fun searchFood(
        refrigeratorId: Long,
        page: Int,
        sort: SortFood?,
        category: FoodCategory?,
        count: Int?,
        keyword: String?
    ): Page<FoodResponse> {
        return foodRepository.findByFood(refrigeratorId, page, sort, category, count, keyword).map { it.toResponse() }
    }
}