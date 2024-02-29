package team.b2.bingojango.domain.mail.repository

import org.springframework.data.jpa.repository.JpaRepository
import team.b2.bingojango.domain.mail.model.Mail
import team.b2.bingojango.domain.refrigerator.model.Refrigerator

interface MailRepository: JpaRepository<Mail, Long> {
    fun findByCode(invitationCode: String): Mail?
}