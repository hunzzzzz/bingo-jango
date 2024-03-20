package team.b2.bingojango.domain.member.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import team.b2.bingojango.domain.member.service.MemberService
import team.b2.bingojango.global.security.util.UserPrincipal

@RestController
@RequestMapping("/api/v1/refrigerator/{refrigeratorId}/members")
class MemberController(
        private val memberService: MemberService
) {
    @Operation(summary = "STAFF 권한 부여")
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{memberId}")
    fun assignStaff(
            @PathVariable refrigeratorId: Long,
            @PathVariable memberId: Long,
            @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<Unit> {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(memberService.assignStaff(refrigeratorId, memberId, userPrincipal))
    }

    @Operation(summary = "냉장고 탈퇴")
    @PreAuthorize("isAuthenticated()")
    @PutMapping
    fun withdrawMember(
            @PathVariable refrigeratorId: Long,
            @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<Unit> {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(memberService.withdrawMember(refrigeratorId, userPrincipal))
    }
}