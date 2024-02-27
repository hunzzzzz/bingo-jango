package team.b2.bingojango.global.util

import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object ZonedDateTimeConverter {
    fun convertStringDateFromZonedDateTime(date: String): ZonedDateTime =
        ZonedDateTime.of(
            LocalDate.parse(
                date,
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            ).atStartOfDay(),
            ZoneId.of("Asia/Seoul")
        )
}