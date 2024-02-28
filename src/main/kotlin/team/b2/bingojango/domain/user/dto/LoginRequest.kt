package team.b2.bingojango.domain.user.dto

data class LoginRequest (
    val email: String,
    val password: String,
    val passwordConfirm: String // 비밀번호 확인 필드 추가
)