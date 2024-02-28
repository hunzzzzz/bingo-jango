package team.b2.bingojango.domain.food.dto

import team.b2.bingojango.domain.food.model.FoodCategory
import java.time.ZonedDateTime

data class FoodResponse (
    var category: FoodCategory,
    var name: String,
    var expirationDate: ZonedDateTime,
    var count: Int
)