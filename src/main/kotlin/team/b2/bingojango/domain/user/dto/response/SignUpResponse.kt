package team.b2.bingojango.domain.user.dto.response

import java.time.ZonedDateTime

data class SignUpResponse(
    val id: Long,
    val name: String,
    val nickname: String,
    val email: String,
    val phone: String,
    val createdAt: ZonedDateTime,
)