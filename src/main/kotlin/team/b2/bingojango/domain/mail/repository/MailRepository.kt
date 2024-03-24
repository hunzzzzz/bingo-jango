package team.b2.bingojango.domain.mail.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.b2.bingojango.domain.mail.model.Mail

@Repository
interface MailRepository : JpaRepository<Mail, Long> {
    fun findByCode(invitationCode: String): Mail?
}