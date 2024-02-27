package team.b2.bingojango.domain.member.dto

data class LoginRequest (
    val email: String,
    val password: String,
)