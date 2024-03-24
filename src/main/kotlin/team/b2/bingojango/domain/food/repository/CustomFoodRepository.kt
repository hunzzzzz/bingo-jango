package team.b2.bingojango.domain.food.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import team.b2.bingojango.domain.food.model.Food
import team.b2.bingojango.domain.food.model.FoodCategory
import team.b2.bingojango.domain.food.model.SortFood

interface CustomFoodRepository {
    fun findByFood(refrigeratorId: Long, page: Int, sort: SortFood?, category: FoodCategory?, count: Int?, keyword: String?): Page<Food>

    fun findFirstPage(refrigeratorId: Long, pageable: Pageable): List<Food>

    fun findNextPage(refrigeratorId: Long, cursorName: String, pageable: Pageable): List<Food>
}