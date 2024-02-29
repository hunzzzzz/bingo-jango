package team.b2.bingojango.domain.food.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.b2.bingojango.domain.food.model.Food

@Repository
interface FoodRepository : JpaRepository<Food, Long> {
    fun existsByRefrigeratorIdAndName(refrigeratorId: Long, name: String): Boolean
}