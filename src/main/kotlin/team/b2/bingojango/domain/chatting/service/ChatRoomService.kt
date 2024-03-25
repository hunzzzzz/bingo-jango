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
import team.b2.bingojango.domain.user.repository.UserRepository
import team.b2.bingojango.global.exception.cases.ModelNotFoundException
import team.b2.bingojango.global.security.util.UserPrincipal

@Service
class ChatRoomService(
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

    // 채팅방 삭제 (냉장고 삭제 로직에 추가)
    // [API] 마지막 멤버가 냉장고를 탈퇴할 때 같이 실행되어 채팅방을 soft delete
    fun deleteChatRoom(refrigerator: Refrigerator) { //
        val chatRoom = getChatRoom(refrigerator)
        chatRoom.chatRoomStatus = ChatRoomStatus.DELETED
        chatRoomRepository.save(chatRoom)
    }

    // 냉장고로부터 채팅방 정보 취득
    fun getChatRoom(refrigerator: Refrigerator) =
        chatRoomRepository.findByRefrigerator(refrigerator) ?: throw ModelNotFoundException("refrigerator")

}