package team.b2.bingojango.domain.member.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.b2.bingojango.domain.member.model.MemberRole
import team.b2.bingojango.domain.member.repository.MemberRepository
import team.b2.bingojango.domain.refrigerator.model.Refrigerator
import team.b2.bingojango.global.exception.cases.AlreadyHaveStaffAccessException
import team.b2.bingojango.global.exception.cases.InvalidCredentialException
import team.b2.bingojango.global.exception.cases.InvalidRoleException
import team.b2.bingojango.global.exception.cases.ModelNotFoundException
import team.b2.bingojango.global.security.util.UserPrincipal

@Service
class MemberService (
    private val memberRepository: MemberRepository
){

    // 로그인한 사람이 member 있는지 확인
    // 로그인한 사람의 member 의 role 값이 STAFF 인지 확인
    // 권한주려는 사람의 냉장고와 로그인한 사람의 동일 냉장고인지, 동일하다면 해당 냉장고에 속한 멤버 id 인지 확인
    // 관리자 권한주기 (이미 관리자면 줄 수 X)
    @Transactional
    fun assignStaff(refrigeratorId: Long, memberId: Long, userPrincipal: UserPrincipal){
        val existMember = memberRepository.findByUserId(userPrincipal.id) ?: throw ModelNotFoundException("User's member")
        if(existMember.role != MemberRole.STAFF) {throw InvalidRoleException()}

        val userRefrigerator = findRefrigeratorByUserId(userPrincipal.id)
        if(refrigeratorId != userRefrigerator.id){throw InvalidCredentialException()}

        val member = memberRepository.findByIdOrNull(memberId) ?: throw ModelNotFoundException("memberId")
        if (member.refrigerator.id != refrigeratorId){throw InvalidCredentialException()}
        if (member.role == MemberRole.STAFF){throw AlreadyHaveStaffAccessException()}
        member.role = MemberRole.STAFF
        memberRepository.save(member)
    }

    private fun findRefrigeratorByUserId(userId: Long): Refrigerator{
        val member = memberRepository.findByUserId(userId)
                ?: throw ModelNotFoundException("Member")
        return member.refrigerator
    }
}