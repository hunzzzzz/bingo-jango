package team.b2.bingojango.global.util

import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object ZonedDateTimeConverter {
    // String 날짜 데이터 -> ZonedDateTime 날짜+시간 데이터
    fun convertStringDateFromZonedDateTime(date: String): ZonedDateTime =
        ZonedDateTime.of(
            LocalDate.parse(
                date,
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            ).atStartOfDay(),
            ZoneId.of("Asia/Seoul")
        )

    // String 날짜+시간 데이터 -> ZonedDateTime 날짜+시간 데이터
    fun convertStringDateTimeFromZonedDateTime(dateTime: String): ZonedDateTime =
        ZonedDateTime.of(
            LocalDate.parse(
                dateTime,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            ).atStartOfDay(),
            ZoneId.of("Asia/Seoul")
        )

    // ZonedDateTime 날짜+시간 데이터 -> String 날짜+시간 데이터
    fun convertZonedDateTimeFromStringDateTime(dateTime: ZonedDateTime): String =
        dateTime.format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        )
}