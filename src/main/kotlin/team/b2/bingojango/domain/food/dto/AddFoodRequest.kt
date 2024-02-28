package team.b2.bingojango.domain.food.dto

import team.b2.bingojango.domain.food.model.FoodCategory

data class AddFoodRequest (
        val category: FoodCategory,
        val name: String,
        val expirationDate: String,
        val count: Int
)