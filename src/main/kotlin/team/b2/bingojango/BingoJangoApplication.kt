package team.b2.bingojango

import io.swagger.v3.oas.annotations.servers.Server
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
@Server(url = "https://bingo-jango.com", description = "Default Server URL")
class BingoJangoApplication

fun main(args: Array<String>) {
    runApplication<BingoJangoApplication>(*args)
}
