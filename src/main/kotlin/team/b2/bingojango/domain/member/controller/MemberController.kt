package team.b2.bingojango.domain.member.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.b2.bingojango.domain.member.service.MemberService
import team.b2.bingojango.global.security.util.UserPrincipal

@RestController
@RequestMapping("/api/v1/refrigerator/{refrigeratorId}/members")
class MemberController (
        private val memberService: MemberService
){
    // 로그인한 사람이 냉장고 관리자라면 >> 동일 냉장고 타멤버인지 확인하고 >> 관리자 권한주기
    @Operation(summary = "STAFF 권한 부여")
    @PatchMapping("/{memberId}")
    fun assignStaff(
            @PathVariable refrigeratorId: Long,
            @PathVariable memberId: Long,
            @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<Unit>{
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(memberService.assignStaff(refrigeratorId, memberId, userPrincipal))
    }
}