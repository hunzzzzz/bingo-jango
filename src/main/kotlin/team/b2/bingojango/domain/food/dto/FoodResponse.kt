package team.b2.bingojango.domain.food.dto

import team.b2.bingojango.domain.food.model.FoodCategory
import java.time.ZonedDateTime

data class FoodResponse (
    var category: String,
    var name: String,
    var expirationDate: String,
    var count: Int
)