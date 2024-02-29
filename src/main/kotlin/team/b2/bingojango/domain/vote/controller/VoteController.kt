package team.b2.bingojango.domain.vote.controller

import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import team.b2.bingojango.domain.vote.dto.request.VoteRequest
import team.b2.bingojango.domain.vote.service.VoteService
import team.b2.bingojango.global.security.UserPrincipal

@RestController
@RequestMapping("/api/v1/refrigerator/{refrigeratorId}/vote")
class VoteController(
    private val voteService: VoteService
) {
    @Operation(summary = "현재 공동구매 목록에 대한 투표 현황 조회")
    @GetMapping("/{voteId}")
    fun showVote(
        @PathVariable refrigeratorId: Long,
        @PathVariable voteId: Long
    ) =
        ResponseEntity.ok().body(voteService.showVote(refrigeratorId, voteId))

    @Operation(summary = "현재 공동구매 목록에 대한 투표 시작")
    @PostMapping
    fun startVote(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable refrigeratorId: Long,
        @Valid @RequestBody voteRequest: VoteRequest
    ) =
        ResponseEntity.ok().body(voteService.startVote(userPrincipal, refrigeratorId, voteRequest))

    @Operation(summary = "현재 공동구매 목록에 대한 투표")
    @PutMapping("/{voteId}")
    fun vote(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable refrigeratorId: Long,
        @PathVariable voteId: Long,
        @RequestParam isAccepted: Boolean
    ) =
        ResponseEntity.ok().body(voteService.vote(userPrincipal, refrigeratorId, voteId, isAccepted))
}