package team.b2.bingojango.global.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.PatternTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import team.b2.bingojango.domain.chatting.service.ListenerService

@Configuration
class RedisConfig(
    @Value("\${spring.data.redis.host}") private val redisHost: String,
    @Value("\${spring.data.redis.port}") private val redisPort: Int,
) {


    @Bean
    fun redisConnectionFactory() = LettuceConnectionFactory(redisHost, redisPort)

    //필요 시 추가
//    @Bean
//    fun objectMapper()=
//        ObjectMapper().let {
//            it.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//            it.registerModule(JavaTimeModule())
//        }!!

    @Bean
    fun adapter(listener: ListenerService) = MessageListenerAdapter(listener, "onMessage")

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

    @Bean
    fun redisMessageListenerContainer(
        factory: RedisConnectionFactory,
        adapter: MessageListenerAdapter,
        topic: ChannelTopic
//        topic: ChannelTopic 혹은 PatternTopic 찾아보기
    ): RedisMessageListenerContainer {
        val container = RedisMessageListenerContainer()
        container.setConnectionFactory(factory)
        container.addMessageListener(adapter, topic)
        return container
    }

    @Bean
    fun topic() = ChannelTopic("chatroom")

}