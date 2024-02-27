package team.b2.bingojango.domain.user.dto

data class SignupRequest(
    val name: String,
    val password: String,
    val rePassword: String,
    val nickname: String,
    val phone: String,
    val email: String,
)
