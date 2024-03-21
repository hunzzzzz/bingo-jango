package team.b2.bingojango.global.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.Properties

@Configuration
class MailConfig(
    @Value("\${spring.mail.host}") private val mailHost: String,
    @Value("\${spring.mail.port}") private val mailPort: Int,
    @Value("\${spring.mail.username}") private val mailUsername: String,
    @Value("\${spring.mail.password}") private val mailPassword: String,
) {

    @Bean
    fun javaMailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = mailHost
        mailSender.port = mailPort
        mailSender.username = mailUsername
        mailSender.password = mailPassword

        val props: Properties = mailSender.javaMailProperties
        props["mail.transport.protocol"] = "smtp"
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.debug"] = "true"

        return mailSender
    }
}
