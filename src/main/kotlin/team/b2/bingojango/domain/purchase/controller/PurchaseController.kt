package team.b2.bingojango.domain.purchase.controller

import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.b2.bingojango.domain.purchase.service.PurchaseService
import team.b2.bingojango.domain.vote.dto.request.VoteRequest
import team.b2.bingojango.global.security.UserPrincipal

@RestController
@RequestMapping("/api/v1/refrigerator/{refrigeratorId}/purchase")
class PurchaseController(
    private val purchaseService: PurchaseService
) {
    @Operation(summary = "현재 공동구매 목록을 출력")
    @GetMapping
    fun showPurchase(
        @PathVariable refrigeratorId: Long
    ) =
        purchaseService.showPurchase(refrigeratorId)

    @Operation(summary = "현재 공동구매 목록에 대한 투표 시작")
    @PostMapping("/vote")
    fun startVote(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable refrigeratorId: Long,
        @Valid @RequestBody voteRequest: VoteRequest
    ) =
        ResponseEntity.ok().body(purchaseService.startVote(userPrincipal, refrigeratorId, voteRequest))

    @Operation(summary = "현재 공동구매 목록에 대한 투표")
    @PutMapping("/vote/{voteId}")
    fun vote(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable refrigeratorId: Long,
        @PathVariable voteId: Long,
        @RequestParam isAccepted: Boolean
    ) =
        ResponseEntity.ok().body(purchaseService.vote(userPrincipal, refrigeratorId, voteId, isAccepted))
}