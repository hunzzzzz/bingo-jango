package team.b2.bingojango.domain.food.service

import org.springframework.data.domain.Page
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
import team.b2.bingojango.domain.food.model.SortFood
import team.b2.bingojango.domain.food.repository.FoodRepository
import team.b2.bingojango.domain.purchase.service.PurchaseService
import team.b2.bingojango.domain.refrigerator.repository.RefrigeratorRepository
import java.time.ZonedDateTime
import team.b2.bingojango.domain.refrigerator.model.Refrigerator
import team.b2.bingojango.global.exception.cases.AlreadyInPurchaseException
import team.b2.bingojango.global.exception.cases.InvalidRoleException
import team.b2.bingojango.global.exception.cases.ModelNotFoundException
import team.b2.bingojango.global.exception.cases.NoCurrentPurchaseException
import team.b2.bingojango.global.security.UserPrincipal
import team.b2.bingojango.global.util.EntityFinder

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