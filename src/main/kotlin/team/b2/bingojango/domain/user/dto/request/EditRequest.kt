package team.b2.bingojango.domain.user.dto.request

data class EditRequest(
    val name: String,
    val nickname: String,
    val phone: String,
    val email: String,
)