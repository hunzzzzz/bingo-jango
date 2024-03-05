package team.b2.bingojango.domain.refrigerator.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import team.b2.bingojango.domain.refrigerator.dto.request.AddRefrigeratorRequest
import team.b2.bingojango.domain.refrigerator.dto.request.JoinByInvitationCodeRequest
import team.b2.bingojango.domain.refrigerator.dto.request.JoinByPasswordRequest
import team.b2.bingojango.domain.refrigerator.dto.response.RefrigeratorResponse
import team.b2.bingojango.domain.refrigerator.service.RefrigeratorService
import team.b2.bingojango.global.security.util.UserPrincipal

@RestController
@RequestMapping("/api/v1/refrigerator")
class RefrigeratorController(
    private val refrigeratorService: RefrigeratorService
) {
    @Operation(summary = "냉장고 목록 조회")
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    fun getRefrigerator(
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<List<RefrigeratorResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(refrigeratorService.getRefrigerator(userPrincipal))
    }

    @Operation(summary = "신규 냉장고 생성")
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    fun addRefrigerator(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestBody request: AddRefrigeratorRequest
    ): ResponseEntity<RefrigeratorResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(refrigeratorService.addRefrigerator(userPrincipal, request))
    }

    @Operation(summary = "기존 냉장고 참여 - 비밀번호 이용")
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/join/password")
    fun joinRefrigeratorByPassword(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestBody request: JoinByPasswordRequest
    ): ResponseEntity<RefrigeratorResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(refrigeratorService.joinRefrigeratorByPassword(userPrincipal, request))
    }

    @Operation(summary = "기존 냉장고 참여 - 초대코드 이용")
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/join/code")
    fun joinRefrigeratorByInvitationCode(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestBody request: JoinByInvitationCodeRequest
    ): ResponseEntity<RefrigeratorResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(refrigeratorService.joinRefrigeratorByInvitationCode(userPrincipal, request))
    }
}
