package team.b2.bingojango.global.security.jwt

data class JwtDto(
    val accessToken: String,
    val refreshToken: String,
)