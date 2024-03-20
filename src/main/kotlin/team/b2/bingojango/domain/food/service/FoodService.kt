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
import team.b2.bingojango.domain.member.repository.MemberRepository
import team.b2.bingojango.domain.refrigerator.model.RefrigeratorStatus
import team.b2.bingojango.domain.refrigerator.repository.RefrigeratorRepository
import team.b2.bingojango.domain.user.repository.UserRepository
import team.b2.bingojango.global.exception.cases.*
import team.b2.bingojango.global.security.util.UserPrincipal
import team.b2.bingojango.global.util.ZonedDateTimeConverter


@Service
@Transactional
class FoodService(
    private val foodRepository: FoodRepository,
    private val refrigeratorRepository: RefrigeratorRepository,
    private val memberRepository: MemberRepository,
    private val userRepository: UserRepository,
) {
    fun getFood(userPrincipal: UserPrincipal, refrigeratorId: Long): List<FoodResponse> {
        val refrigerator = refrigeratorRepository.findByIdOrNull(refrigeratorId) ?: throw ModelNotFoundException("Refrigerator")
        if (refrigerator.status != RefrigeratorStatus.NORMAL) {throw ModelNotFoundException("Refrigerator")}

        // 로그인한 사람이 냉장고에 소속되어 있는지 확인
        val user = userRepository.findByIdOrNull(userPrincipal.id) ?: throw ModelNotFoundException("User")
        if (!memberRepository.existsByUserAndRefrigerator(user, refrigerator)) {throw InvalidCredentialException()}

        return foodRepository.findByRefrigeratorId(refrigeratorId)
                .map {
            FoodResponse(
                    category = it.category.name,
                    name = it.name,
                    expirationDate = ZonedDateTimeConverter.convertZonedDateTimeFromStringDateTime(it.expirationDate),
                    count = it.count,
            )
        }
    }

    @Transactional
    fun addFood(userPrincipal: UserPrincipal, refrigeratorId: Long, request: AddFoodRequest) {
        val refrigerator = refrigeratorRepository.findByIdOrNull(refrigeratorId) ?: throw ModelNotFoundException("Refrigerator")
        if (refrigerator.status != RefrigeratorStatus.NORMAL) {throw ModelNotFoundException("Refrigerator")}
        val user = userRepository.findByIdOrNull(userPrincipal.id) ?: throw ModelNotFoundException("User")
        if (!memberRepository.existsByUserAndRefrigerator(user, refrigerator)) {throw InvalidCredentialException()}
        val existsFood = foodRepository.existsByRefrigeratorIdAndName(refrigeratorId, request.name)
        if (existsFood) {
            throw AlreadyExistsFoodException()
        }
        val newFood = Food(
                category = request.category,
                name = request.name,
                expirationDate = ZonedDateTimeConverter.convertStringDateFromZonedDateTime(request.expirationDate),
                count = request.count,
                refrigerator = refrigerator
        )
        foodRepository.save(newFood)
    }

    @Transactional
    fun updateFood(userPrincipal: UserPrincipal, refrigeratorId: Long, foodId: Long, request: UpdateFoodRequest) {
        val refrigerator = refrigeratorRepository.findByIdOrNull(refrigeratorId) ?: throw ModelNotFoundException("Refrigerator")
        if (refrigerator.status != RefrigeratorStatus.NORMAL) {throw ModelNotFoundException("Refrigerator")}
        val user = userRepository.findByIdOrNull(userPrincipal.id) ?: throw ModelNotFoundException("User")
        if (!memberRepository.existsByUserAndRefrigerator(user, refrigerator)) {throw InvalidCredentialException()}
        val findFood = foodRepository.findByIdOrNull(foodId) ?: throw ModelNotFoundException("Food")
        if (findFood.refrigerator?.id != refrigerator.id) {
            throw ModelNotFoundException("Food")
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
    fun updateFoodCount(userPrincipal: UserPrincipal, refrigeratorId: Long, foodId: Long, count: Int) {
        val refrigerator = refrigeratorRepository.findByIdOrNull(refrigeratorId) ?: throw ModelNotFoundException("Refrigerator")
        if (refrigerator.status != RefrigeratorStatus.NORMAL) {throw ModelNotFoundException("Refrigerator")}
        val user = userRepository.findByIdOrNull(userPrincipal.id) ?: throw ModelNotFoundException("User")
        if (!memberRepository.existsByUserAndRefrigerator(user, refrigerator)) {throw InvalidCredentialException()}
        val findFood = foodRepository.findByIdOrNull(foodId) ?: throw ModelNotFoundException("Food")
        if (findFood.refrigerator?.id != refrigerator.id) {
            throw ModelNotFoundException("Food")
        }
        findFood.count = count
        foodRepository.save(findFood)
    }

    @Transactional
    fun deleteFood(userPrincipal: UserPrincipal, refrigeratorId: Long, foodId: Long) {
        val refrigerator = refrigeratorRepository.findByIdOrNull(refrigeratorId) ?: throw ModelNotFoundException("Refrigerator")
        if (refrigerator.status != RefrigeratorStatus.NORMAL) {throw ModelNotFoundException("Refrigerator")}
        val user = userRepository.findByIdOrNull(userPrincipal.id) ?: throw ModelNotFoundException("User")
        if (!memberRepository.existsByUserAndRefrigerator(user, refrigerator)) {throw InvalidCredentialException()}
        val findFood = foodRepository.findByIdOrNull(foodId) ?: throw ModelNotFoundException("Food")
        if (findFood.refrigerator?.id != refrigerator.id) {
            throw ModelNotFoundException("Food")
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
        val refrigerator = refrigeratorRepository.findByIdOrNull(refrigeratorId) ?: throw ModelNotFoundException("Refrigerator")
        if (refrigerator.status != RefrigeratorStatus.NORMAL) {throw ModelNotFoundException("Refrigerator")}
        return foodRepository.findByFood(refrigeratorId, page, sort, category, count, keyword).map { it.toResponse() }
    }
}