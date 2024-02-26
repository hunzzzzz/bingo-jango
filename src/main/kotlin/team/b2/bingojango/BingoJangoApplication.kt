package team.b2.bingojango

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class BingoJangoApplication

fun main(args: Array<String>) {
    runApplication<BingoJangoApplication>(*args)
}
