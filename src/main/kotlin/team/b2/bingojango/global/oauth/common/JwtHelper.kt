package team.b2.bingojango.global.oauth.common

import org.springframework.stereotype.Component

@Component
class JwtHelper {

    fun generateAccessToken(id: Long): String {
        return "SampleAccessToken $id"
    }
}