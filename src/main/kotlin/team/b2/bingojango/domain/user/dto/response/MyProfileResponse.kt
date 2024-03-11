package team.b2.bingojango.domain.user.dto.response

import team.b2.bingojango.domain.refrigerator.dto.response.RefrigeratorResponse
import java.time.ZonedDateTime

class MyProfileResponse (
        val name : String?,
        override val nickname : String,
        override val email : String,
        val phone : String?,
        override val refrigerators : List<RefrigeratorResponse>,
        override val createdAt : ZonedDateTime
) : UserResponse(nickname, email, refrigerators, createdAt)