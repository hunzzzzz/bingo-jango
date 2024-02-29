package team.b2.bingojango.domain.mail.utility

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component
import java.util.*

@Component
class MailUtility(
    val javaMailSender: JavaMailSender
) {
    //인증번호 생성
    fun createCode() : String {
        val length = 6
        val code = UUID.randomUUID().toString().substring(0,length)
        return code

    }
    //이메일 발송하기
    fun sendMail(email: String): String {
        val code = createCode()
        val message = javaMailSender.createMimeMessage()
        val helper = MimeMessageHelper(message)

        helper.setTo(email) // mail 수신자
        helper.setSubject("냉장고에 참여하기 위한 인증코드 입니다.") // mail 제목
        helper.setText(code) // mail 내용
        helper.setFrom("gks777777@gmail.com") // mail 발송자

        javaMailSender.send(message) // mail 발송

        return code
    }
}