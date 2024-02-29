package team.b2.bingojango.domain.refrigerator.dto.request

data class AddRefrigeratorRequest(
    val name: String,
    val password: String,
    val rePassword: String
)