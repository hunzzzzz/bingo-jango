package team.b2.bingojango.domain.member.dto

import team.b2.bingojango.domain.member.model.MemberRole
import java.time.ZonedDateTime

data class MemberResponse (
        val name: String,
        val role: MemberRole,
        val createdAt: ZonedDateTime
)