package team.b2.bingojango

import SmsService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
@Configuration
class BingoJangoApplication {

    @Bean
    fun smsservice(): SmsService {
        return SmsService()
    }
}

fun main(args: Array<String>) {
    runApplication<BingoJangoApplication>(*args)
}
