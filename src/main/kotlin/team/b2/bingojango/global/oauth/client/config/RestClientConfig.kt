package team.b2.bingojango.global.oauth.client.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration
class RestClientConfig {

    @Bean
    fun restClient(): RestClient {
        return RestClient.builder()
            .build()
    }
}