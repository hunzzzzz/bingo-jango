package team.b2.bingojango.domain.food.dto

data class FoodResponse(
    val category: String,
    val name: String,
    val expirationDate: String,
    val count: Int
)