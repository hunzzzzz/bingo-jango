package team.b2.bingojango.global.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
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
import team.b2.bingojango.domain.chatting.dto.ChatResponse

@Configuration
class RedisConfig(
) {
    @Value("\${spring.data.redis.host")
    private lateinit var redisHost: String

    @Value("\${spring.data.redis.port}")
    private val redisPort: Int = 0

    @Bean
    fun redisConnectionFactory()= LettuceConnectionFactory(redisHost, redisPort)

    //필요 시 추가
//    @Bean
//    fun objectMapper()=
//        ObjectMapper().let {
//            it.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//            it.registerModule(JavaTimeModule())
//        }!!

//    @Bean
//    fun adapter(subscriber: RedisSubscriber)=MessageListenerAdapter(subscriber, "onMessage")

    @Bean
    fun redisTemplate(
        factory: RedisConnectionFactory,
        objectMapper: ObjectMapper,
    ):RedisTemplate<String, ChatResponse>{
        val template= RedisTemplate<String, ChatResponse>()
        template.connectionFactory= factory
        template.keySerializer= StringRedisSerializer()
        template.valueSerializer= GenericJackson2JsonRedisSerializer(objectMapper)
        return template
    }

    @Bean
    fun redisMessageListenerContainer(
        factory: RedisConnectionFactory,
//        adapter: MessageListenerAdapter,
//        topic: ChannelTopic 혹은 PatternTopic 찾아보기
    ):RedisMessageListenerContainer{
        val container= RedisMessageListenerContainer()
        container.setConnectionFactory(factory)
//        container.addMessageListener(adapter, topic)
        return container
    }

//    @Bean
//    fun topic()= ChannelTopic or PatternTopic

}