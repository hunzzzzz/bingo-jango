package team.b2.bingojango.domain.user.dto
import java.time.LocalDateTime

data class SignUpResponse (
    val role: String,
    val name: String,
    val nickname: String,
    val email: String,
    val phone: String,
    val status: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val id: Long,
    val success: Boolean,
    val message: String
)