package team.b2.bingojango.global.oauth.api.oauth2login.service

import org.springframework.stereotype.Service
import team.b2.bingojango.global.oauth.client.oauth2.OAuth2ClientService
import team.b2.bingojango.global.oauth.common.JwtHelper
import team.b2.bingojango.global.oauth.domain.entity.OAuth2Provider

@Service
class OAuth2LoginService(
    private val oAuth2ClientService: OAuth2ClientService,
    private val socialMemberService: SocialMemberService,
    private val jwtHelper: JwtHelper
) {
    // Service = 위임 서비스
    // 1. 인가코드 -> 액세스 토큰 (카카오 출입증) 발급
    // 2. 액세스 토큰 (카카오 출입증)으로 사용자정보 조회
    // 3. 사용자정보로 SocialMember 있으면 조회 없으면 회원가입
    // 4. SocialMember 를 토대로 우리쪽 액세스 토큰(빙고 출입증) 발급후 응답
    fun login(provider: OAuth2Provider, authorizationCode: String): String {
        val userInfo = oAuth2ClientService.login(provider, authorizationCode)
        val socialMember = socialMemberService.registerIfAbsent(userInfo)
        val accessToken = jwtHelper.generateAccessToken(socialMember.id!!)
        return accessToken

    }
}