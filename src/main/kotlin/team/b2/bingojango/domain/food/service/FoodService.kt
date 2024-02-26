package team.b2.bingojango.domain.food.service

import org.springframework.stereotype.Service
import team.b2.bingojango.domain.food.repository.FoodRepository

@Service
class FoodService(
    private val foodRepository: FoodRepository
) {
}