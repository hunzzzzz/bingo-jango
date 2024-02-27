package team.b2.bingojango.domain.food.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.b2.bingojango.domain.food.service.FoodService

@RestController
@RequestMapping("/api/v1/refrigerator/{refrigeratorId}/foods")
class FoodController(
    private val foodService: FoodService
) {
    @Operation(summary = "특정 식품에 대한 공동구매 신청")
    @PostMapping("/{foodId}")
    fun addFoodToPurchase(
        @PathVariable refrigeratorId: Long,
        @PathVariable foodId: Long,
        @RequestParam count: Int
    ) =
        foodService.addFoodToPurchase(refrigeratorId, foodId, count)
}