package team.b2.bingojango.domain.purchase.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.b2.bingojango.domain.purchase.service.PurchaseService

@RestController
@RequestMapping("/api/v1/refrigerator/{refrigeratorId}/purchase")
class PurchaseController(
    private val purchaseService: PurchaseService
) {
    @Operation(summary = "현재 진행 중인 공동구매 목록을 출력")
    @GetMapping
    fun showPurchase(
        @PathVariable refrigeratorId: Long
    ) =
        purchaseService.showPurchase(refrigeratorId)
}