package team.b2.bingojango.domain.user.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class SignUpRequest(

    @field:NotBlank(message = "이름을 입력해주세요.")
    val name: String,

    @field:NotBlank(message = "닉네임을 입력해주세요.")
    val nickname: String,

    @field:NotBlank(message = "이메일을 입력해주세요.")
    @field:Email(message = "이메일 형식이 아닙니다.")
    @Schema(description = "이메일", example = "email@email.com")
    val email: String,

    @field:NotBlank(message = "전화번호를 입력해주세요.")
    @field:Pattern(
        regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}\$",
        message = "전화번호는 하이픈(-)을 포함해서 입력해주세요. ex) 010-1234-5678")
    val phone: String,

    @field:NotBlank(message = "비밀번호를 입력해주세요.")
    @Schema(description = "비밀번호", example = "Password12!@")
    @field:Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$",
        message = "비밀번호는 최소 4자 이상, 숫자, 문자, 특수문자를 포함해야 합니다. 공백은 포함하지 않습니다.")
    val password: String,

    @field:NotBlank(message = "비밀번호확인을 입력해주세요.")
    @Schema(description = "비밀번호확인", example = "Password12!@")
    @field:Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$",
        message = "비밀번호는 최소 4자 이상, 숫자, 문자, 특수문자를 포함해야 합니다. 공백은 포함하지 않습니다.")
    val passwordConfirm: String
)