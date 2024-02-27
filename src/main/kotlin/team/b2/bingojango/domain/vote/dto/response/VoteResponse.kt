package team.b2.bingojango.domain.vote.dto.response

import team.b2.bingojango.domain.member.model.Member
import team.b2.bingojango.domain.vote.model.Vote
import team.b2.bingojango.global.util.ZonedDateTimeConverter

data class VoteResponse(
    val description: String,
    val dueDate: String,
    val memberName: String,
    val voteStatus: String
) {
    companion object {
        fun from(vote: Vote, member: Member, numberOfStaff: Long) = VoteResponse(
            description = vote.description ?: "",
            dueDate = ZonedDateTimeConverter.convertZonedDateTimeFromStringDateTime(vote.dueDate),
            memberName = member.user.nickname,
            voteStatus = "투표 현황: 찬성 ${vote.voters.size}명 / ${numberOfStaff}명"
        )
    }
}
