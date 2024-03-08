package team.b2.bingojango.domain.chatting.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.b2.bingojango.domain.chatting.model.ChatRoom
import team.b2.bingojango.domain.chatting.model.ChatRoomStatus
import team.b2.bingojango.domain.chatting.repository.ChatRoomRepository
import team.b2.bingojango.domain.member.model.MemberRole
import team.b2.bingojango.domain.member.repository.MemberRepository
import team.b2.bingojango.domain.refrigerator.model.Refrigerator
import team.b2.bingojango.domain.refrigerator.repository.RefrigeratorRepository
import team.b2.bingojango.domain.user.repository.UserRepository
import team.b2.bingojango.global.exception.cases.ModelNotFoundException
import team.b2.bingojango.global.security.util.UserPrincipal

@Service
class ChatRoomService(
    private val refrigeratorRepository: RefrigeratorRepository,
    private val chatRoomRepository: ChatRoomRepository,
    private val memberRepository: MemberRepository,
    private val userRepository: UserRepository,
) {

    //채팅방 생성 (냉장고 생성 로직에 추가)
    @Transactional
    fun buildChatRoom(refrigerator: Refrigerator, userPrincipal: UserPrincipal) =
        chatRoomRepository.save(
            ChatRoom(
                name = "${refrigerator.name} 채팅방",
                refrigerator = refrigerator,
                chatRoomStatus = ChatRoomStatus.OPEN
            )
        )

    // 채팅방 멤버 확인 (chatroom 기능 중 컨트롤러 필요한 유일 기능으로 chat 컨트롤러에 편입)
    fun getChatRoomMember(chatRoomId: Long, userPrincipal: UserPrincipal): List<String> {
        val chatRoom = chatRoomRepository.findByIdOrNull(chatRoomId) ?: throw ModelNotFoundException("chatRoom")
        val user = userRepository.findByIdOrNull(userPrincipal.id) ?: throw ModelNotFoundException("userId")
        val member = memberRepository.findByUserAndChatRoom(user, chatRoom) ?: throw ModelNotFoundException("memberId")
        if (member.chatRoom == chatRoom) {
            val members = memberRepository.findAllByRefrigerator(chatRoom.refrigerator)
            return members
                .sortedByDescending { it.role == MemberRole.STAFF }
                .map { "${it.user.name} / ${it.role} \n" }
        } else throw IllegalArgumentException("채팅창 멤버가 아니에요.")
    }

    // 채팅방 삭제 (냉장고 삭제 로직에 추가)
    fun deleteChatRoom(refrigerator: Refrigerator) {
        val chatRoom = getChatRoom(refrigerator)
        chatRoom.chatRoomStatus = ChatRoomStatus.DELETED
        chatRoomRepository.save(chatRoom)
    }

    // 채팅방 정보 객체 호출
    fun getChatRoom(refrigerator: Refrigerator) =
        chatRoomRepository.findByRefrigerator(refrigerator) ?: throw ModelNotFoundException("refrigerator")

}