package team.b2.bingojango.domain.member.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import team.b2.bingojango.domain.member.dto.MemberResponse
import team.b2.bingojango.domain.member.service.MemberService
import team.b2.bingojango.global.security.util.UserPrincipal

@RestController
@RequestMapping("refrigerator/{refrigeratorId}/members")
class MemberController(
    private val memberService: MemberService
) {
    @Operation(summary = "냉장고 참여 멤버 조회")
    @PreAuthorize("isAuthenticated()")
    @GetMapping()
    fun getMembers(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable refrigeratorId: Long
    ): ResponseEntity<List<MemberResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.getMembers(userPrincipal, refrigeratorId))
    }

    @Operation(summary = "STAFF 권한 부여")
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{memberId}")
    fun assignStaff(
        @PathVariable refrigeratorId: Long,
        @PathVariable memberId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<Unit> {
        memberService.assignStaff(refrigeratorId, memberId, userPrincipal)
        return ResponseEntity
            .status(HttpStatus.OK)
            .build()
    }

    @Operation(summary = "냉장고 탈퇴")
    @PreAuthorize("isAuthenticated()")
    @PutMapping
    fun withdrawMember(
        @PathVariable refrigeratorId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<Unit> {
        memberService.withdrawMember(refrigeratorId, userPrincipal)
        return ResponseEntity
            .status(HttpStatus.OK)
            .build()
    }
}