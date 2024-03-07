package team.b2.bingojango.domain.user.dto.response

import team.b2.bingojango.domain.refrigerator.dto.response.RefrigeratorResponse
import java.time.ZonedDateTime

open class UserResponse (
        open val nickname : String,
        open val email : String,
        open val refrigerators : List<RefrigeratorResponse>,
        open val createdAt : ZonedDateTime
)