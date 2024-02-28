package team.b2.bingojango.domain.food.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import team.b2.bingojango.domain.food.service.FoodService
import team.b2.bingojango.domain.food.dto.AddFoodRequest
import team.b2.bingojango.domain.food.dto.UpdateFoodRequest

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

    @PostMapping
    fun addFood(
            @PathVariable refrigeratorId: Long,
            @RequestBody addFoodRequest: AddFoodRequest) : ResponseEntity<Unit> {
        foodService.addFood(refrigeratorId, addFoodRequest)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @PutMapping("/{foodId}")
    fun updateFood(
            @PathVariable refrigeratorId: Long,
            @PathVariable foodId: Long,
            @RequestBody updateFoodRequest: UpdateFoodRequest): ResponseEntity<Unit> {
        foodService.updateFood(refrigeratorId, foodId, updateFoodRequest)
        return ResponseEntity(HttpStatus.OK)
    }

    @PatchMapping("/{foodId}")
    fun updateFoodCount(
            @PathVariable refrigeratorId: Long,
            @PathVariable foodId: Long,
            @RequestParam count: Int): ResponseEntity<Unit> {
        foodService.updateFoodCount(refrigeratorId, foodId, count)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/{foodId}")
    fun deleteFood(
            @PathVariable refrigeratorId: Long,
            @PathVariable foodId: Long): ResponseEntity<Unit> {
        foodService.deleteFood(refrigeratorId, foodId)
        return ResponseEntity.noContent().build()
    }
}