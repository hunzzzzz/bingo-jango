package team.b2.bingojango.domain.mail.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import team.b2.bingojango.domain.mail.dto.MailResponse
import team.b2.bingojango.domain.mail.model.Mail
import team.b2.bingojango.domain.mail.repository.MailRepository
import team.b2.bingojango.domain.mail.utility.MailUtility
import team.b2.bingojango.domain.refrigerator.repository.RefrigeratorRepository
import team.b2.bingojango.global.exception.cases.ModelNotFoundException

@Service
class MailService(
    private val mailRepository: MailRepository,
    private val mailUtility: MailUtility,
    private val refrigeratorRepository: RefrigeratorRepository,
) {
    fun sendInvitationCode(refrigeratorId: Long, email: String): MailResponse {
        val refrigerator = refrigeratorRepository.findByIdOrNull(refrigeratorId) ?: throw ModelNotFoundException("Refrigerator")
        val code = mailUtility.sendMail(email)
        val mail = mailRepository.save(Mail.toEntity(refrigerator, email, code))

        return MailResponse(message = "요청하신 이메일로 초대코드를 보냈습니다.", code = code)
    }
}
