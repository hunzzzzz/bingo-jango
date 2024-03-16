package team.b2.bingojango.domain.food.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import team.b2.bingojango.domain.food.service.FoodService
import team.b2.bingojango.domain.food.dto.AddFoodRequest
import team.b2.bingojango.domain.food.dto.UpdateFoodRequest
import org.springframework.web.bind.annotation.*
import team.b2.bingojango.domain.food.dto.FoodResponse
import team.b2.bingojango.domain.food.model.FoodCategory
import team.b2.bingojango.domain.food.model.SortFood

@Tag(name = "food", description = "음식")
@RestController
@RequestMapping("/api/v1/refrigerator/{refrigeratorId}/foods")
class FoodController(
    private val foodService: FoodService
) {

    @Operation(summary = "음식 조회")
    @GetMapping
    fun getFood(
            @PathVariable refrigeratorId: Long
    ): ResponseEntity<List<FoodResponse>>{
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(foodService.getFood(refrigeratorId))
    }

    @Operation(summary = "음식 추가")
    @PostMapping
    fun addFood(
        @PathVariable refrigeratorId: Long,
        @RequestBody addFoodRequest: AddFoodRequest
    ): ResponseEntity<Unit> {
        foodService.addFood(refrigeratorId, addFoodRequest)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @Operation(summary = "음식 수정")
    @PutMapping("/{foodId}")
    fun updateFood(
        @PathVariable refrigeratorId: Long,
        @PathVariable foodId: Long,
        @RequestBody updateFoodRequest: UpdateFoodRequest
    ): ResponseEntity<Unit> {
        foodService.updateFood(refrigeratorId, foodId, updateFoodRequest)
        return ResponseEntity(HttpStatus.OK)
    }

    @Operation(summary = "음식 수량 수정")
    @PatchMapping("/{foodId}")
    fun updateFoodCount(
        @PathVariable refrigeratorId: Long,
        @PathVariable foodId: Long,
        @RequestParam count: Int
    ): ResponseEntity<Unit> {
        foodService.updateFoodCount(refrigeratorId, foodId, count)
        return ResponseEntity(HttpStatus.OK)
    }

    @Operation(summary = "음식 삭제")
    @DeleteMapping("/{foodId}")
    fun deleteFood(
        @PathVariable refrigeratorId: Long,
        @PathVariable foodId: Long
    ): ResponseEntity<Unit> {
        foodService.deleteFood(refrigeratorId, foodId)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "음식 검색 및 정렬")
    @GetMapping("/search")
    fun searchFood(
        @PathVariable refrigeratorId: Long,
        @RequestParam
        (defaultValue = "0") page: Int,
        sort: SortFood?,
        category: FoodCategory?,
        count: Int?,
        keyword: String?
    ): ResponseEntity<Page<FoodResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(foodService.searchFood(refrigeratorId, page, sort, category, count, keyword))
    }
}