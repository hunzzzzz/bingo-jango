package team.b2.bingojango.domain.chatting.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Service
import team.b2.bingojango.domain.chatting.dto.ChatResponse
import team.b2.bingojango.global.exception.cases.ModelNotFoundException

@Service
class ListenerService(
    private val objectMapper: ObjectMapper,
    private val redisTemplate: RedisTemplate<String, String>,
    private val messageTemplate: SimpMessageSendingOperations
) : MessageListener {

    override fun onMessage(message: Message, pattern: ByteArray?) {
        try {
            // redis에서 발행된 데이터를 역직렬화
            val pubMessage = redisTemplate.stringSerializer.deserialize(message.body)
            // ChatResponse로 맵핑
            val roomMessage = objectMapper.readValue(pubMessage, ChatResponse::class.java)
//            val chatMessage= ChatResponse(roomMessage.chatRoomId, roomMessage.nickname, roomMessage.content, roomMessage.status, roomMessage.createdAt)
            // 채팅 발행
            messageTemplate.convertAndSend("/sub/chatRoom/${roomMessage.chatRoomId}", roomMessage)
        } catch (e: Exception) {
            throw ModelNotFoundException("redis chatMessage")
        }
    }
}