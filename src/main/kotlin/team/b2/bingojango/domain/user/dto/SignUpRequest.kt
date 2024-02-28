package team.b2.bingojango.domain.user.dto

import team.b2.bingojango.domain.user.model.UserRole
import team.b2.bingojango.domain.user.model.UserStatus

data class SignUpRequest (
    val name: String,
    val nickname: String,
    val email: String,
    val phone: String,
    val password: String,
    val password2: String,
    val role: UserRole,
    val status: UserStatus
)