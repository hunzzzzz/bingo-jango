package team.b2.bingojango.domain.member.service

import org.springframework.stereotype.Service
import team.b2.bingojango.domain.member.repository.MemberRepository

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {
}