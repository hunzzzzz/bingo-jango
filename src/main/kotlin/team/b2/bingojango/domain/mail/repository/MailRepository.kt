package team.b2.bingojango.domain.mail.repository

import org.springframework.data.jpa.repository.JpaRepository
import team.b2.bingojango.domain.mail.model.Mail

interface MailRepository: JpaRepository<Mail, Long>