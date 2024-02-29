package team.b2.bingojango.domain.purchase.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import team.b2.bingojango.domain.purchase.service.PurchaseService
import team.b2.bingojango.global.security.UserPrincipal

@RestController
@RequestMapping("/api/v1/refrigerator/{refrigeratorId}/purchase")
class PurchaseController(
    private val purchaseService: PurchaseService
) {
    @Operation(summary = "특정 식품에 대한 공동구매 신청")
    @PostMapping("/foods/{foodId}")
    fun addFoodToPurchase(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable refrigeratorId: Long,
        @PathVariable foodId: Long,
        @RequestParam count: Int
    ) =
        ResponseEntity.ok().body(purchaseService.addFoodToPurchase(userPrincipal, refrigeratorId, foodId, count))

    @Operation(summary = "공동구매 목록에 포함된 식품의 개수 수정")
    @PutMapping("/foods/{foodId}")
    fun updateFoodInPurchase(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable refrigeratorId: Long,
        @PathVariable foodId: Long,
        @RequestParam count: Int
    ) =
        ResponseEntity.ok().body(purchaseService.updateFoodInPurchase(userPrincipal, refrigeratorId, foodId, count))

    @Operation(summary = "공동구매 목록에서 특정 식품 삭제")
    @DeleteMapping("/foods/{foodId}")
    fun deleteFoodFromPurchase(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable refrigeratorId: Long,
        @PathVariable foodId: Long
    ) =
        ResponseEntity.ok().body(purchaseService.deleteFoodFromPurchase(userPrincipal, refrigeratorId, foodId))

    @Operation(summary = "현재 공동구매 목록을 출력")
    @GetMapping
    fun showPurchase(
        @PathVariable refrigeratorId: Long
    ) =
        purchaseService.showPurchase(refrigeratorId)
}