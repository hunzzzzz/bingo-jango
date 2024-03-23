package team.b2.bingojango.domain.user.dto.request

import jakarta.validation.constraints.NotBlank

data class FindEmailRequest(
    @field:NotBlank(message = "이름은 필수 입력 사항입니다.")
    val name: String,

    @field:NotBlank(message = "핸드폰은 필수 입력 사항입니다.")
    val phone: String
)