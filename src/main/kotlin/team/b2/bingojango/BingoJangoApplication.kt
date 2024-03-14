package team.b2.bingojango

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import team.b2.bingojango.domain.user.service.UserService

@SpringBootApplication
@EnableJpaAuditing
@Configuration
class BingoJangoApplication {

    @Value("\${twilio.accountSid}")
    private lateinit var accountSid: String

    @Value("\${twilio.authToken}")
    private lateinit var authToken: String

    @Bean
    fun twilioSmsService(): UserService.TwilioSmsService {
        return UserService.TwilioSmsService(accountSid, authToken)
    }
}

fun main(args: Array<String>) {
    runApplication<BingoJangoApplication>(*args)
}
