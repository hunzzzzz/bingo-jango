package team.b2.bingojango.domain.refrigerator.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.b2.bingojango.domain.chatting.service.ChatRoomService
import team.b2.bingojango.domain.mail.repository.MailRepository
import team.b2.bingojango.domain.member.dto.MemberResponse
import team.b2.bingojango.domain.member.model.Member
import team.b2.bingojango.domain.member.model.MemberRole
import team.b2.bingojango.domain.member.repository.MemberRepository
import team.b2.bingojango.domain.refrigerator.dto.request.AddRefrigeratorRequest
import team.b2.bingojango.domain.refrigerator.dto.request.JoinByInvitationCodeRequest
import team.b2.bingojango.domain.refrigerator.dto.request.JoinByPasswordRequest
import team.b2.bingojango.domain.refrigerator.dto.response.RefrigeratorResponse
import team.b2.bingojango.domain.refrigerator.model.Refrigerator
import team.b2.bingojango.domain.refrigerator.model.RefrigeratorStatus
import team.b2.bingojango.domain.refrigerator.repository.RefrigeratorRepository
import team.b2.bingojango.domain.user.repository.UserRepository
import team.b2.bingojango.global.exception.cases.ModelNotFoundException
import team.b2.bingojango.global.security.util.UserPrincipal

@Service
class RefrigeratorService(
        private val refrigeratorRepository: RefrigeratorRepository,
        private val memberRepository: MemberRepository,
        private val userRepository: UserRepository,
        private val chatRoomService: ChatRoomService,
        private val mailRepository: MailRepository,
) {
    //냉장고 목록 조회
    fun getRefrigerator(userPrincipal: UserPrincipal): List<RefrigeratorResponse> {
        val member = memberRepository.findAllByUserId(userPrincipal.id)
        val refrigerator = member.map { it.refrigerator }
        val filtered = refrigerator.filter { it.status == RefrigeratorStatus.NORMAL }
        return filtered.map { it.toResponse() }
    }

    //신규 냉장고 생성
    @Transactional
    fun addRefrigerator(userPrincipal: UserPrincipal, request: AddRefrigeratorRequest): RefrigeratorResponse {
        val user = userRepository.findByIdOrNull(userPrincipal.id) ?: throw ModelNotFoundException("User")
        val refrigerator = refrigeratorRepository.save(Refrigerator.toEntity(request))
        val chatRoom = chatRoomService.buildChatRoom(refrigerator, userPrincipal)
        val member = memberRepository.save(Member.toEntity(user, MemberRole.STAFF, refrigerator, chatRoom))
        return refrigerator.toResponse()
    }

    //기존 냉장고 참여 - 비밀번호 이용
    @Transactional
    fun joinRefrigeratorByPassword(userPrincipal: UserPrincipal, request: JoinByPasswordRequest): RefrigeratorResponse {
        val user = userRepository.findByIdOrNull(userPrincipal.id) ?: throw ModelNotFoundException("User")
        //확인사항1: 냉장고 존재 유무
        val refrigerator = refrigeratorRepository.findByName(request.name)
                ?: throw ModelNotFoundException("Refrigerator")
        //확인사항2: 비밀번호 일치 여부
        if (refrigerator.password != request.password) throw IllegalArgumentException("냉장고의 비밀번호가 일치하지 않습니다.")

        val chatRoom = chatRoomService.getChatRoom(refrigerator)
        memberRepository.save(Member.toEntity(user, MemberRole.MEMBER, refrigerator, chatRoom))

        return refrigerator.toResponse()
    }

    //기존 냉장고 참여 - 초대코드 이용
    @Transactional
    fun joinRefrigeratorByInvitationCode(userPrincipal: UserPrincipal, request: JoinByInvitationCodeRequest): RefrigeratorResponse {
        val user = userRepository.findByIdOrNull(userPrincipal.id) ?: throw ModelNotFoundException("User")
        val mail = mailRepository.findByCode(request.invitationCode) ?: throw ModelNotFoundException("Mail")
        val refrigerator = mail.refrigerator
        val chatRoom = chatRoomService.getChatRoom(refrigerator)

        memberRepository.save(Member.toEntity(user, MemberRole.MEMBER, refrigerator, chatRoom))

        return refrigerator.toResponse()
    }

    //냉장고 참여 멤버 조회
    @Transactional
    fun getMembers(refrigeratorId: Long): List<MemberResponse> {
        val refrigerator = refrigeratorRepository.findByIdOrNull(refrigeratorId)
                ?: throw ModelNotFoundException("Refrigerator")
        val members = memberRepository.findAllByRefrigerator(refrigerator)
        return members.sortedWith(compareBy<Member> { it.role }.thenBy { it.createdAt }).map { member ->
            MemberResponse(
                    name = member.user.name,
                    role = member.role,
                    memberId = member.id!!,
                    createdAt = member.createdAt
            )
        }
    }
}