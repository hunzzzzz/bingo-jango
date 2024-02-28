package team.b2.bingojango.global.exception

import team.b2.bingojango.global.util.ZonedDateTimeConverter.convertZonedDateTimeFromStringDateTime
import java.time.ZonedDateTime

data class ErrorResponse(
    val httpStatus: String,
    val message: String,
    val path: String,
    val time: String = convertZonedDateTimeFromStringDateTime(ZonedDateTime.now())
)