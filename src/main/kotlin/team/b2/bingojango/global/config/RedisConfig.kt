package team.b2.bingojango.global.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import team.b2.bingojango.domain.chatting.service.ListenerService

@Configuration
class RedisConfig(
    @Value("\${spring.data.redis.host}") private val redisHost: String,
    @Value("\${spring.data.redis.port}") private val redisPort: Int,
    @Value("\${spring.data.redis.password}") private val redisPassword: String
) {

    // LettuceConnectionFactory 세팅
    @Bean
    fun redisConnectionFactory() =
        RedisStandaloneConfiguration().let {
            it.hostName = redisHost
            it.port = redisPort
            it.setPassword(redisPassword)
            it
        }.let { LettuceConnectionFactory(it) }

    //필요 시 추가
//    @Bean
//    fun objectMapper()=
//        ObjectMapper().let {
//            it.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//            it.registerModule(JavaTimeModule())
//        }!!

    // 발행된 메시지를 레디스에서 받아서 처리시키는 메소드
    @Bean
    fun adapter(listener: ListenerService) = MessageListenerAdapter(listener, "onMessage")

    // 레디스 템플릿
    @Bean
    fun redisTemplate(
        factory: RedisConnectionFactory,
        objectMapper: ObjectMapper,
    ): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        template.connectionFactory = factory
        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = GenericJackson2JsonRedisSerializer(objectMapper)
        return template
    }

    // 레디스에서 메시지를 인식할 시 해석할 수 있도록 세팅
    @Bean
    fun redisMessageListenerContainer(
        factory: RedisConnectionFactory,
        adapter: MessageListenerAdapter,
        topic: ChannelTopic
    ): RedisMessageListenerContainer {
        val container = RedisMessageListenerContainer()
        container.setConnectionFactory(factory)
        container.addMessageListener(adapter, topic)
        return container
    }

    // scale-out 된 서버 명시
    @Bean
    fun topic() = ChannelTopic("chatroom")

}