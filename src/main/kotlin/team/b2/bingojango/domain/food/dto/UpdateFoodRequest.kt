package team.b2.bingojango.domain.food.dto

data class UpdateFoodRequest(
        val category: String,
        val name: String,
        val expirationDate:String
)