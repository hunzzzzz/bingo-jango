package team.b2.bingojango.domain.member.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.b2.bingojango.domain.member.model.MemberRole
import team.b2.bingojango.domain.member.repository.MemberRepository
import team.b2.bingojango.domain.refrigerator.model.Refrigerator
import team.b2.bingojango.domain.refrigerator.model.RefrigeratorStatus
import team.b2.bingojango.domain.refrigerator.repository.RefrigeratorRepository
import team.b2.bingojango.domain.user.repository.UserRepository
import team.b2.bingojango.global.exception.cases.*
import team.b2.bingojango.global.security.util.UserPrincipal

@Service
class MemberService(
        private val memberRepository: MemberRepository,
        private val refrigeratorRepository: RefrigeratorRepository,
        private val userRepository: UserRepository
) {
    @Transactional
    fun assignStaff(refrigeratorId: Long, memberId: Long, userPrincipal: UserPrincipal) {
        val existMember = memberRepository.findByUserId(userPrincipal.id) ?: throw ModelNotFoundException("User's member")
        if (existMember.role != MemberRole.STAFF) {throw InvalidRoleException()}

        val userRefrigerator = findRefrigeratorByUserId(userPrincipal.id)
        if (userRefrigerator.status != RefrigeratorStatus.NORMAL) {throw ModelNotFoundException("Refrigerator")}
        if (refrigeratorId != userRefrigerator.id) {throw InvalidCredentialException()}

        val member = memberRepository.findByIdOrNull(memberId) ?: throw ModelNotFoundException("memberId")
        if (member.refrigerator.id != refrigeratorId) {throw InvalidCredentialException()}
        if (member.role == MemberRole.STAFF) {throw AlreadyHaveStaffAccessException()}
        member.role = MemberRole.STAFF
        memberRepository.save(member)
    }

    private fun findRefrigeratorByUserId(userId: Long): Refrigerator {
        val member = memberRepository.findByUserId(userId)
                ?: throw ModelNotFoundException("Member")
        return member.refrigerator
    }

    @Transactional
    fun withdrawMember(refrigeratorId: Long, userPrincipal: UserPrincipal) {
        val refrigerator = refrigeratorRepository.findByIdOrNull(refrigeratorId)?: throw ModelNotFoundException("Refrigerator")
        if (refrigerator.status != RefrigeratorStatus.NORMAL) {throw ModelNotFoundException("Refrigerator")}
        val user = userRepository.findByIdOrNull(userPrincipal.id)?: throw ModelNotFoundException("User")
        val member = memberRepository.findByUserAndRefrigerator(user, refrigerator) ?: throw ModelNotFoundException("Member")

        // staff 인지 확인 (아니면 자동 탈퇴)
        if (member.role != MemberRole.STAFF) {memberRepository.delete(member)}

        // staff 이면 -> 다른 멤버 있고, 그 중 staff 있으면 삭제 / 다른 멤버 없으면 냉장고 삭제
        else {
            val findMember = memberRepository.findAllByRefrigerator(member.refrigerator)
            val countMember = findMember.size
            val staffMemberList = findMember.filter{ it.role == MemberRole.STAFF }

            if (countMember > 1 && staffMemberList.size > 1) {memberRepository.delete(member)}
            if (countMember > 1 && staffMemberList.size == 1) {throw MustAssignException()}
            if (countMember == 1) {refrigerator.status = RefrigeratorStatus.DELETED}
        }
    }
}