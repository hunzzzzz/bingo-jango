package team.b2.bingojango.domain.vote.dto.request

import team.b2.bingojango.domain.member.model.Member
import team.b2.bingojango.domain.refrigerator.model.Refrigerator
import team.b2.bingojango.domain.vote.model.Vote
import team.b2.bingojango.global.util.ZonedDateTimeConverter

data class VoteRequest(
    val description: String?,
    val dueDate: String,
) {
    fun to(request: VoteRequest, refrigerator: Refrigerator, member: Member) = Vote(
        description = request.description,
        dueDate = ZonedDateTimeConverter.convertStringDateFromZonedDateTime(request.dueDate),
        refrigerator = refrigerator,
        voters = mutableSetOf(member)
    )
}
