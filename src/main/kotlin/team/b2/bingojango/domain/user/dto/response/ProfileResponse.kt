package team.b2.bingojango.domain.user.dto.response

import team.b2.bingojango.domain.refrigerator.dto.response.RefrigeratorResponse
import team.b2.bingojango.domain.user.model.User
import team.b2.bingojango.global.util.ZonedDateTimeConverter.convertZonedDateTimeFromStringDateTime

data class ProfileResponse(
    var name: String?,
    val nickname: String,
    val email: String,
    var phone: String?,
    val refrigerators: List<RefrigeratorResponse>,
    val createdAt: String
) {
    companion object {
        fun getProfile(user: User, refrigerators: List<RefrigeratorResponse>) = ProfileResponse(
            name = null,
            nickname = user.nickname,
            email = user.email,
            phone = null,
            refrigerators = refrigerators,
            createdAt = convertZonedDateTimeFromStringDateTime(user.createdAt)
        )
    }

    fun updateMyProfile(user: User) {
        this.name = user.name
        this.phone = user.phone
    }
}