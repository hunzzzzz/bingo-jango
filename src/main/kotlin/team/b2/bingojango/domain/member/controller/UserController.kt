package team.b2.bingojango.domain.member.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.b2.bingojango.domain.member.dto.LoginRequest
import team.b2.bingojango.domain.member.dto.LoginResponse
import team.b2.bingojango.domain.member.service.UserService


@RestController
@RequestMapping("/")
class UserController(
    private val userService: UserService
) {
    @PreAuthorize("isAnonymous()")
    @PostMapping("/login")
    fun login(
        @RequestBody loginRequest: LoginRequest,
    ): ResponseEntity<LoginResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.login(loginRequest))
    }
}