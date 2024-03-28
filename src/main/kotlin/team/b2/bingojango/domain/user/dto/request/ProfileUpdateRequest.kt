package team.b2.bingojango.domain.user.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class ProfileUpdateRequest(
    val name: String,
    val nickname: String,

    @field:NotBlank(message = "전화번호를 입력해주세요.")
    @field:Pattern(
        regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}\$",
        message = "전화번호는 하이픈(-)을 포함해서 입력해주세요. ex) 010-1234-5678")
    val phone: String,
)