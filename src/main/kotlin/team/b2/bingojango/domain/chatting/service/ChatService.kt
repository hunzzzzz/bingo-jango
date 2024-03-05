package team.b2.bingojango.domain.chatting.service

import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.b2.bingojango.domain.chatting.dto.ChatRequest
import team.b2.bingojango.domain.chatting.dto.ChatResponse
import team.b2.bingojango.domain.chatting.model.Chat
import team.b2.bingojango.domain.chatting.model.ChatRoom
import team.b2.bingojango.domain.chatting.model.ChatStatus
import team.b2.bingojango.domain.chatting.model.toResponse
import team.b2.bingojango.domain.chatting.repository.ChatRepository
import team.b2.bingojango.domain.chatting.repository.ChatRoomRepository
import team.b2.bingojango.domain.member.repository.MemberRepository
import team.b2.bingojango.domain.user.model.User
import team.b2.bingojango.domain.user.repository.UserRepository
import team.b2.bingojango.global.exception.cases.ModelNotFoundException
import team.b2.bingojango.global.security.UserPrincipal

@Service
class ChatService(
    private val chatRepository: ChatRepository,
    private val memberRepository: MemberRepository,
    private val userRepository: UserRepository,
    private val chatRoomRepository: ChatRoomRepository,
) {

    // 채팅 전송
    @Transactional
    fun sendMessage(
        userPrincipal: UserPrincipal,
        chatRoomId: Long,
        request: ChatRequest,
    ): ChatResponse {
        val user = getUserInfo(userPrincipal)
        val chatRoom = getChatRoomInfo(chatRoomId)
        val member = getMemberInfo(user, chatRoom)

        return chatRepository.save(
            Chat(
                content = request.content,
                status = ChatStatus.CHAT,
                chatRoom = chatRoom,
                member = member,
            )
        ).toResponse()
    }

    // 채팅 내역 불러오기
    fun getAllChat(userPrincipal: UserPrincipal, chatRoomId: Long): List<ChatResponse> {
        val user = getUserInfo(userPrincipal)
        val chatRoom = getChatRoomInfo(chatRoomId)
        val member = getMemberInfo(user, chatRoom)

        if (member.chatRoom.id != chatRoomId) throw IllegalArgumentException("냉장고의 멤버가 아니에요.")
        else {
            val chats = chatRepository.findAllByChatRoomId(chatRoomId)

            return chats.map {
                ChatResponse(
                    content = it.content,
                    nickname = it.member.user.nickname,
                    status = it.status,
                    createdAt = it.createdAt,
                )
            }
        }
    }

    // 채팅 내역 불러오기 v2
    fun getAllChat2(userPrincipal: UserPrincipal, chatRoomId: Long, cursor: Long?, size: Int): List<ChatResponse> {
        val user = getUserInfo(userPrincipal)
        val chatRoom = getChatRoomInfo(chatRoomId)
        val member = getMemberInfo(user, chatRoom)

        if (member.chatRoom.id != chatRoomId) throw IllegalArgumentException("냉장고의 멤버가 아니에요.")
        else {
            val pageable = PageRequest.of(0, size)
            return if (cursor == null) {
                chatRepository.findFirstPage(chatRoomId, pageable).map { it.toResponse() }
            } else {
                chatRepository.findNextPage(chatRoomId, cursor, pageable).map { it.toResponse() }
            }
        }
    }

    private fun getUserInfo(userPrincipal: UserPrincipal) =
        userRepository.findByIdOrNull(userPrincipal.id) ?: throw ModelNotFoundException("userId")

    private fun getChatRoomInfo(chatRoomId: Long) =
        chatRoomRepository.findByIdOrNull(chatRoomId) ?: throw ModelNotFoundException("chatRoomId")

    private fun getMemberInfo(user: User, chatRoom: ChatRoom) =
        memberRepository.findByUserAndChatRoom(user, chatRoom) ?: throw ModelNotFoundException("chatRoomId")
}