package team.b2.bingojango.domain.user.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class PasswordRequest(
    val password: String,
    @field: Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&+=]).{8,16}$",
        message = "비밀번호는 영문과 특수문자 숫자를 포함하는 8~16자리로 구성되어야 합니다.")
    val newPassword: String,
    @field: NotBlank(message = "비밀번호를 한 번 더 입력해주세요.")
    val reNewPassword: String,
)