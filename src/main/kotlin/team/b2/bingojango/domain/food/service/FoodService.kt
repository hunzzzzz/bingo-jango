package team.b2.bingojango.domain.food.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.b2.bingojango.domain.food.model.Food
import team.b2.bingojango.domain.food.dto.AddFoodRequest
import team.b2.bingojango.domain.food.dto.UpdateFoodRequest
import team.b2.bingojango.domain.food.model.FoodCategory
import team.b2.bingojango.domain.food.repository.FoodRepository
import team.b2.bingojango.domain.refrigerator.repository.RefrigeratorRepository
import java.time.ZonedDateTime

@Service
@Transactional
class FoodService(
    private val foodRepository: FoodRepository,
    private val refrigeratorRepository: RefrigeratorRepository
) {
    @Transactional
    fun addFood(refrigeratorId: Long, request: AddFoodRequest) {
        val findRefrigerator =
            refrigeratorRepository.findByIdOrNull(refrigeratorId) ?: throw Exception("냉장고를 찾을 수 없습니다.")
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
    fun updateFood(refrigeratorId: Long, foodId: Long, request: UpdateFoodRequest) {
        val findRefrigerator =
            refrigeratorRepository.findByIdOrNull(refrigeratorId) ?: throw Exception("냉장고를 찾을 수 없습니다.")
        val findFood = foodRepository.findByIdOrNull(foodId) ?: throw Exception("음식을 찾을 수 없습니다.")
        if (findFood.refrigerator?.id != findRefrigerator.id) {
            throw Exception("냉장고에 음식이 존재하지 않습니다.")
        }
        findFood.category = FoodCategory.valueOf(request.category)
        findFood.name = request.name
        //findFood.expirationDate = request.expirationDate
        foodRepository.save(findFood)
    }

    @Transactional
    fun updateFoodCount(refrigeratorId: Long, foodId: Long, count: Int) {
        val findRefrigerator =
            refrigeratorRepository.findByIdOrNull(refrigeratorId) ?: throw Exception("냉장고를 찾을 수 없습니다.")
        val findFood = foodRepository.findByIdOrNull(foodId) ?: throw Exception("음식을 찾을 수 없습니다.")
        if (findFood.refrigerator?.id != findRefrigerator.id) {
            throw Exception("냉장고에 음식이 존재하지 않습니다.")
        }
        findFood.count = count
        foodRepository.save(findFood)
    }

    @Transactional
    fun deleteFood(refrigeratorId: Long, foodId: Long) {
        val findRefrigerator =
            refrigeratorRepository.findByIdOrNull(refrigeratorId) ?: throw Exception("냉장고를 찾을 수 없습니다.")
        val findFood = foodRepository.findByIdOrNull(foodId) ?: throw Exception("음식을 찾을 수 없습니다.")
        if (findFood.refrigerator?.id != findRefrigerator.id) {
            throw Exception("냉장고에 음식이 존재하지 않습니다.")
        }
        foodRepository.delete(findFood)
    }
}