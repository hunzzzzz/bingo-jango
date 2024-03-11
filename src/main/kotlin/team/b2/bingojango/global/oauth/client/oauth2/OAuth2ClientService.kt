package team.b2.bingojango.global.oauth.client.oauth2

import org.springframework.stereotype.Component
import team.b2.bingojango.global.oauth.domain.entity.OAuth2Provider

//OAuth2Client로 가는 요청이 있을 때, "어떤 provider에게 가야 할지 선택"하고, 선택한 애한테 flow 를 태우는 역할
@Component
class OAuth2ClientService(
    private val client: List<OAuth2Client>
) {
    fun login(provider: OAuth2Provider, authorizationCode: String): OAuth2LoginUserInfo {
        val client: OAuth2Client = this.selectClient(provider)
        return client.getAccessToken(authorizationCode)
            .let { client.retrieveUserInfo(it)}
    }

    fun generateLoginPageUrl(provider: OAuth2Provider): String {
        val client = this.selectClient(provider)
        return client.generateLoginPageUrl()
    }

    //담당하는 client를 찾는다.
    private fun selectClient(provider: OAuth2Provider): OAuth2Client {
        return client.find { it.supports(provider) }
            ?: throw RuntimeException("지원하지 않는 OAuth Provider 입니다.")
    }
}