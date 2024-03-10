package team.b2.bingojango.global.oauth.api.oauth2login.service

import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Service
import team.b2.bingojango.global.oauth.client.oauth2.OAuth2ClientService
import team.b2.bingojango.global.oauth.domain.entity.OAuth2Provider
import team.b2.bingojango.global.security.jwt.JwtPlugin
import team.b2.bingojango.global.security.util.CookieUtil
import team.b2.bingojango.global.security.util.TokenUtil

@Service
class OAuth2LoginService(
    private val oAuth2ClientService: OAuth2ClientService,
    private val socialMemberService: SocialMemberService,
    private val jwtPlugin: JwtPlugin,
    private val tokenUtil: TokenUtil
) {
    // Service = 위임 서비스
    // 1. 인가코드 -> 액세스 토큰 (카카오 출입증) 발급
    // 2. 액세스 토큰 (카카오 출입증)으로 사용자정보 조회
    // 3. 사용자정보로 SocialMember 있으면 조회 없으면 회원가입
    // 4. SocialMember 를 토대로 우리쪽 액세스 토큰(빙고 출입증) 발급후 응답
    fun login(provider: OAuth2Provider, response: HttpServletResponse, authorizationCode: String): String {
        val userInfo = oAuth2ClientService.login(provider, authorizationCode)
        val user = socialMemberService.registerIfAbsent(userInfo)

        //RefreshToken 생성 후 쿠키 반환, DB 저장
        val refreshToken = jwtPlugin.generateRefreshToken(user.id.toString(), user.email, user.role.name)
        val cookieExpirationHour = 24 * 60 * 60 // 쿠키유효시간(24시간, 초 단위)
        CookieUtil.addCookie(response,"refreshToken",refreshToken, cookieExpirationHour) //RefreshToken 쿠키 반환
        tokenUtil.storeToken(user, refreshToken) //RefreshToken DB 저장

        //AccessToken 생성 후 반환
        val accessToken = jwtPlugin.generateAccessToken(user.id.toString(), user.email, user.role.name)
        return accessToken

    }
}