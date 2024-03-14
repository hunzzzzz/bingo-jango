package team.b2.bingojango.domain.chatting.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.stereotype.Service
import team.b2.bingojango.domain.chatting.dto.ChatResponse

@Service
class ScaleOutService(
    private val redisTemplate: RedisTemplate<String, ChatResponse>,
    private val container: RedisMessageListenerContainer,
    private val objectMapper: ObjectMapper,
) {

}