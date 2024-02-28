package team.b2.bingojango.domain.user.dto.request

data class LoginRequest (
    val email: String,
    val password: String,
)