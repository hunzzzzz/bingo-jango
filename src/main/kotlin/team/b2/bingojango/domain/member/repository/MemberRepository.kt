package team.b2.bingojango.domain.member.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.b2.bingojango.domain.member.model.Member
import team.b2.bingojango.domain.member.model.MemberRole
import team.b2.bingojango.domain.user.model.User

@Repository
interface MemberRepository : JpaRepository<Member, Long> {
    fun countByRole(role: MemberRole): Long
    fun findByUser(user: User): Member?
}