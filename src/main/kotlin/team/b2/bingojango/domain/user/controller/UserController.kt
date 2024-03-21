package team.b2.bingojango.domain.user.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import team.b2.bingojango.domain.user.dto.response.UploadImageResponse
import team.b2.bingojango.domain.user.dto.request.*
import team.b2.bingojango.domain.user.dto.response.FindEmailResponse
import team.b2.bingojango.domain.user.dto.response.LoginResponse
import team.b2.bingojango.domain.user.dto.response.MyProfileResponse
import team.b2.bingojango.domain.user.dto.response.UserResponse
import team.b2.bingojango.domain.user.service.UserService
import team.b2.bingojango.global.security.util.UserPrincipal

@Tag(name = "user", description = "유저")
@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
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
    @PreAuthorize("isAuthenticated()")
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

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    fun signUp(@RequestBody signUpRequest: SignUpRequest): ResponseEntity<Any> {
        // UserService 내부에서 validatePassword 메서드를 호출하여 비밀번호 유효성 검사 수행
        try {
            userService.signUp(signUpRequest)
        } catch (e: IllegalArgumentException) {
            // 비밀번호 유효성 검사에 실패한 경우 에러 응답 반환
            return ResponseEntity.badRequest().body("회원가입에 실패하였습니다.")
        }

        // 회원가입 성공 시 성공 응답 반환
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 성공적으로 완료되었습니다.")
    }

    @Operation(summary = "본인 프로필 조회")
    @GetMapping("/me")
    fun getMyProfile(
            @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<MyProfileResponse>{
        //로그인한 본인 프로필 보기
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getMyProfile(userPrincipal))
    }

    @Operation(summary = "타인 프로필 조회")
    @GetMapping("/{userId}")
    fun getUser(
            @PathVariable userId: Long,
            @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<UserResponse>{
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUser(userId, userPrincipal))

    }

    @Operation(summary = "프로필 수정")
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

    @Operation(summary = "비밀번호 변경")
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

    @Operation(summary = "이메일 찾기")
    @PostMapping("/find-email")
    fun findEmail(@RequestBody request: FindEmailRequest): ResponseEntity<FindEmailResponse> {
        val response = userService.findEmail(request)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "비밀번호 찾기")
    @PostMapping("/find-password")
    fun findPassword(@RequestBody request: FindPasswordRequest): ResponseEntity<Any> {
        userService.findPassword(request)
        return ResponseEntity.ok().build()
    }

    @Operation(summary = "비밀번호 재설정")
    @PostMapping("/reset-password")
    fun resetPassword(@RequestBody request: PasswordResetRequest): ResponseEntity<Any> {
        userService.resetPassword(request)
        return ResponseEntity.ok().build()
    }

    @Operation(summary = "회원 탈퇴 (SoftDelete, Scheduled)")
    @PutMapping("mypage/withdraw")
    fun withdrawUser(
        @Parameter(description = "password 만 입력")
        @RequestBody withdrawRequest: WithdrawRequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<String>{
        userService.withdrawUser(withdrawRequest, userPrincipal)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body("탈퇴가 정상적으로 완료되었습니다.")
    }
    @Operation(summary = "프로필 이미지 업로드")
    @PostMapping("/images", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun uploadImage(
            @RequestParam("image") multipartFile: MultipartFile,
            @AuthenticationPrincipal userPrincipal: UserPrincipal)
    : ResponseEntity<UploadImageResponse>{
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.uploadImage(multipartFile, userPrincipal))
    }

}