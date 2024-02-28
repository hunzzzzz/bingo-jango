package team.b2.bingojango.domain.food.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import team.b2.bingojango.domain.food.service.FoodService
import team.b2.bingojango.domain.food.dto.AddFoodRequest
import team.b2.bingojango.domain.food.dto.UpdateFoodRequest
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.b2.bingojango.domain.food.service.FoodService
import team.b2.bingojango.global.security.UserPrincipal

@RestController
@RequestMapping("/api/v1/refrigerator/{refrigeratorId}/foods")
class FoodController(
    private val foodService: FoodService
) {
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
    
    @Operation(summary = "특정 식품에 대한 공동구매 신청")
    @PostMapping("/{foodId}")
    fun addFoodToPurchase(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable refrigeratorId: Long,
        @PathVariable foodId: Long,
        @RequestParam count: Int
    ) =
        ResponseEntity.ok().body(foodService.addFoodToPurchase(userPrincipal, refrigeratorId, foodId, count))
        
    @Operation(summary = "공동구매 목록에서 특정 식품 삭제")
    @DeleteMapping("/{foodId}")
    fun deleteFoodFromPurchase(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable refrigeratorId: Long,
        @PathVariable foodId: Long
    ) =
        ResponseEntity.ok().body(foodService.deleteFoodFromPurchase(userPrincipal, refrigeratorId, foodId))
}