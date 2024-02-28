package team.b2.bingojango.domain.chatroom.service

import org.springframework.stereotype.Service
import team.b2.bingojango.domain.chatroom.model.ChatRoom
import team.b2.bingojango.domain.chatroom.model.ChatRoomStatus
import team.b2.bingojango.domain.chatroom.repository.ChatRoomRepository
import team.b2.bingojango.domain.member.model.MemberRole
import team.b2.bingojango.domain.member.repository.MemberRepository
import team.b2.bingojango.domain.refrigerator.model.Refrigerator
import team.b2.bingojango.domain.refrigerator.repository.RefrigeratorRepository
import team.b2.bingojango.domain.user.repository.UserRepository
import team.b2.bingojango.global.exception.cases.ModelNotFoundException
import team.b2.bingojango.global.security.UserPrincipal

@Service
class ChatRoomService(
    private val refrigeratorRepository: RefrigeratorRepository,
    private val chatRoomRepository: ChatRoomRepository,
    private val memberRepository: MemberRepository,
    private val userRepository: UserRepository,
) {

    fun buildChatRoom(refrigerator: Refrigerator, userPrincipal: UserPrincipal) =
        chatRoomRepository.save(
            ChatRoom(
                name = "${refrigerator.name} 채팅방",
                refrigerator = refrigerator,
                chatRoomStatus = ChatRoomStatus.OPEN
            )
        )

    fun getChatRoomMember(refrigerator: Refrigerator): List<String> {
        val members = memberRepository.findAllByRefrigerator(refrigerator)
        return members
            .sortedByDescending { it.role==MemberRole.STAFF }
            .map { "${it.user.name} / ${it.role}\n" }
    }

    fun deleteChatRoom(refrigerator: Refrigerator) {
        val chatRoom = getChatRoom(refrigerator)
        chatRoom.chatRoomStatus = ChatRoomStatus.DELETED
        chatRoomRepository.save(chatRoom)
    }

    fun getChatRoom(refrigerator: Refrigerator) =
        chatRoomRepository.findByRefrigerator(refrigerator) ?: throw ModelNotFoundException("refrigerator")

}