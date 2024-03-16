package team.b2.bingojango.domain.chatting.service

import org.springframework.data.domain.PageRequest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.repository.findByIdOrNull
import org.springframework.messaging.simp.SimpMessageSendingOperations
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
import team.b2.bingojango.global.security.util.UserPrincipal

@Service
class ChatService(
    private val chatRepository: ChatRepository,
    private val memberRepository: MemberRepository,
    private val userRepository: UserRepository,
    private val chatRoomRepository: ChatRoomRepository,
    private val messageTemplate: SimpMessageSendingOperations,
    private val channelTopic: ChannelTopic,
    private val redisTemplate: RedisTemplate<String, Any>
) {

    // 채팅 전송
    @Transactional
    fun sendMessage(
        userPrincipal: UserPrincipal,
        request: ChatRequest,
    ): ChatResponse {
//        println("hA값 $headerAccessor")
//        println("hA.u 값 ${headerAccessor.user}")
//        val authorHeader= headerAccessor.getFirstNativeHeader("Authorization")
//        val userInfo= (headerAccessor.user as JwtAuthenticationToken).principal )
        val user = getUserInfo(userPrincipal)
        val chatRoom = getChatRoomInfo(request.chatRoomId.toLong())
        val member = getMemberInfo(user, chatRoom)

        val save = chatRepository.save(
            Chat(
                content = request.content,
                status = ChatStatus.CHAT,
                chatRoom = chatRoom,
                member = member,
            )
        )
        val response = save.toResponse()
        messageTemplate.convertAndSend("/sub/chatroom/${response.chatRoomId}", response)

        return response
    }

    @Transactional
    fun sendMessage2(
        userPrincipal: UserPrincipal,
        request: ChatRequest,
    ): ChatResponse {
        val user = getUserInfo(userPrincipal)
        val chatRoom = getChatRoomInfo(request.chatRoomId.toLong())
        val member = getMemberInfo(user, chatRoom)

        val save = chatRepository.save(
            Chat(
                content = request.content,
                status = ChatStatus.CHAT,
                chatRoom = chatRoom,
                member = member,
            )
        )
        val topic = channelTopic.topic
        val response = save.toResponse()
        redisTemplate.convertAndSend(topic, response)
        return response
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
                    chatRoomId = it.chatRoom.id!!,
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

    // 테스트용 채팅방 불러오기
    fun getAllChatRoom(userPrincipal: UserPrincipal): List<ChatRoom> {
        val members = memberRepository.findAllByUserId(userPrincipal.id)
        val chatRooms = members.map { it.chatRoom }
        return chatRooms
    }

    private fun getUserInfo(userPrincipal: UserPrincipal) =
        userRepository.findByIdOrNull(userPrincipal.id) ?: throw ModelNotFoundException("userId")

    private fun getChatRoomInfo(chatRoomId: Long) =
        chatRoomRepository.findByIdOrNull(chatRoomId) ?: throw ModelNotFoundException("chatRoomId")

    private fun getMemberInfo(user: User, chatRoom: ChatRoom) =
        memberRepository.findByUserAndChatRoom(user, chatRoom) ?: throw ModelNotFoundException("chatRoomId")
}