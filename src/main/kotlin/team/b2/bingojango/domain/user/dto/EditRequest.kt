package team.b2.bingojango.domain.user.dto

data class EditRequest(
    val name: String,
    val nickname: String,
    val phone: String,
    val password: String,
    val newPassword: String,
    val reNewPassword: String,
)