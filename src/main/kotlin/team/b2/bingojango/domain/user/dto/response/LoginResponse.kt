package team.b2.bingojango.domain.user.dto.response

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val userId: Long
)