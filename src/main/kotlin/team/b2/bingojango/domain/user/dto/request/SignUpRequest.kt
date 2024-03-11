package team.b2.bingojango.domain.user.dto.request

data class SignUpRequest(
    val name: String,
    val nickname: String,
    val email: String,
    val phone: String,
    val password: String,
    val passwordConfirm: String
)