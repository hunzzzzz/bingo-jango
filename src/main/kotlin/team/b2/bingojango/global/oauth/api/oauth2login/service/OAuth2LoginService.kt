package team.b2.bingojango.global.oauth.api.oauth2login.service

import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import team.b2.bingojango.domain.user.dto.response.LoginResponse
import team.b2.bingojango.global.oauth.client.oauth2.OAuth2ClientService
import team.b2.bingojango.global.oauth.domain.entity.OAuth2Provider
import team.b2.bingojango.global.security.jwt.JwtPlugin
import team.b2.bingojango.global.security.util.CookieUtil

@Service
class OAuth2LoginService(
    private val oAuth2ClientService: OAuth2ClientService,
    private val socialMemberService: SocialMemberService,
    private val jwtPlugin: JwtPlugin,
    @Value("\${app.cookie.expiry}") private val cookieExpirationTime: Int,
) {
    /*
        [API] OAuth2 핵심 로직 (위임 서비스)
            - 인가코드 -> 액세스 토큰(카카오 출입증) 발급
            - 액세스 토큰(카카오 출입증)으로 사용자정보 조회
            - 사용자정보로 User 있으면 조회 없으면 회원가입
            - 액세스 토큰(빙고 출입증) 및 리프레시 토큰(빙고 출입증) 생성
            - 리프레시 토큰(빙고 출입증)은 쿠키와 DB에 저장
            - 리프레시 토큰(빙고 출입증)과 액세스 토큰(빙고 출입증) 반환
    */
    fun login(provider: OAuth2Provider, response: HttpServletResponse, authorizationCode: String): LoginResponse {
        val userInfo = oAuth2ClientService.login(provider, authorizationCode)
        val user = socialMemberService.registerIfAbsent(userInfo)
        val accessToken = jwtPlugin.generateAccessToken(user.id.toString(), user.email, user.role.name)
        val refreshToken = jwtPlugin.generateRefreshToken(user.id.toString(), user.email, user.role.name)
        CookieUtil.addCookie(response, "refreshToken", refreshToken, cookieExpirationTime)
        jwtPlugin.storeToken(user, refreshToken)
        return LoginResponse(accessToken, refreshToken, user.id!!)

    }
}