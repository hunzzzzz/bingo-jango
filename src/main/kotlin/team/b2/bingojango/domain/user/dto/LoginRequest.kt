package team.b2.bingojango.domain.user.dto

data class LoginRequest (
    val email: String,
    val password: String,
)