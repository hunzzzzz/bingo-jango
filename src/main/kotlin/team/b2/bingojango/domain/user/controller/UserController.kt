package team.b2.bingojango.domain.user.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import team.b2.bingojango.domain.user.dto.request.EditRequest
import team.b2.bingojango.domain.user.dto.request.LoginRequest
import team.b2.bingojango.domain.user.dto.request.PasswordRequest
import team.b2.bingojango.domain.user.dto.request.SignUpRequest
import team.b2.bingojango.domain.user.dto.response.LoginResponse
import team.b2.bingojango.domain.user.dto.response.SignUpResponse
import team.b2.bingojango.domain.user.service.UserService
import team.b2.bingojango.global.security.UserPrincipal

@Tag(name = "user", description = "유저")
@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {
    @Operation(summary = "로그인")
    @PreAuthorize("isAnonymous()")
    @PostMapping("/login")
    fun login(
        @RequestBody loginRequest: LoginRequest, response: HttpServletResponse
    ): ResponseEntity<LoginResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.login(loginRequest, response))
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    fun logout(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        request: HttpServletRequest,
        response: HttpServletResponse
    ): ResponseEntity<Unit>{
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(userService.logout(userPrincipal, request, response))
    }

    //회원가입
    @PostMapping("/signup")
    fun signUp(
        @RequestBody signUpRequest: SignUpRequest
    ): ResponseEntity<SignUpResponse> {
        val signUpResponse = userService.signUp(signUpRequest)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(signUpResponse)
    }

    // 프로필 수정
    @PatchMapping("mypage/update")
    fun updateUserProfile(
        @RequestBody editRequest: EditRequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<String>{
        userService.updateUserProfile(editRequest, userPrincipal)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body("정보가 변경되었습니다.")
    }

    // 비밀번호 변경
    @PatchMapping("mypage/change-pwd")
    fun updateUserPassword(
        @RequestBody passwordRequest: PasswordRequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<String>{
        userService.updateUserPassword(passwordRequest, userPrincipal)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body("비밀번호가 변경되었습니다.")
    }

    // 회원 탈퇴
    @PutMapping("mypage/withdraw")
    fun withdrawUser(
        @Parameter(description = "password 만 입력")
        @RequestBody passwordRequest: PasswordRequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<String>{
        userService.withdrawUser(passwordRequest, userPrincipal)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body("탈퇴가 정상적으로 완료되었습니다.")
    }

}