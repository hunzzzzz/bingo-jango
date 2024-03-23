package team.b2.bingojango.domain.user.dto.request

import jakarta.validation.constraints.NotBlank

data class LoginRequest (
    @field:NotBlank(message = "이메일은 필수 입력 사항입니다.")
    val email: String,

    @field:NotBlank(message = "패스워드는 필수 입력 사항입니다.")
    val password: String,
)