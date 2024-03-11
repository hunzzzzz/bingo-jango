package team.b2.bingojango.global.oauth.client.oauth2

import team.b2.bingojango.global.oauth.domain.entity.OAuth2Provider

interface OAuth2Client {
    fun generateLoginPageUrl(): String
    fun getAccessToken(authorizationCode: String): String
    fun retrieveUserInfo(accessToken: String): OAuth2LoginUserInfo
    fun supports(provider: OAuth2Provider): Boolean
}