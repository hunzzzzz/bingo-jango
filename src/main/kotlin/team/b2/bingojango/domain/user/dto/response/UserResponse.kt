package team.b2.bingojango.domain.user.dto.response

import team.b2.bingojango.domain.refrigerator.dto.response.RefrigeratorResponse
import java.time.ZonedDateTime

data class UserResponse (
        val name : String,
        val nickname : String,
        val email : String,
        val refrigerators : List<RefrigeratorResponse>,
        val createdAt : ZonedDateTime
)