package team.b2.bingojango.global.security.jwt.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "user_tokens")
class UserToken(
    @Id
    val userId: Long,
    val token: String,
    val expiryDate: LocalDateTime
)