package team.b2.bingojango.global.security.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import team.b2.bingojango.global.security.util.UserPrincipal

@Component
class JwtAuthenticationFilter(
    private val jwtPlugin: JwtPlugin
) : OncePerRequestFilter() {

    companion object {
        private val BEARER_PATTERN = Regex("^Bearer (.+?)$")
    }

    //[API] JWT 인증 필터
    //1. 토큰 추출 : 헤더에서 "Bearer"토큰을 추출함.
    //2. 토큰 유효성 검사 : 토큰에서 사용자의 정보를 추출함.
    //3. 인증 정보 설정 : UserPrincipal 객체를 통해 Spring Security 에 인증 정보를 저장
    //4. 다음 요청이나 필터로 전달함.
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val jwt = request.getBearerToken()
        if (jwt != null) {
            jwtPlugin.validateToken(jwt)
                .onSuccess {
                    val userId = it.payload.subject.toLong()
                    val role = it.payload.get("role", String::class.java)
                    val email = it.payload.get("email", String::class.java)

                    val principal = UserPrincipal(
                        id = userId,
                        email = email,
                        roles = setOf(role)
                    )

                    val authentication = JwtAuthenticationToken(
                        principal = principal,
                        details =  WebAuthenticationDetailsSource().buildDetails(request)
                    )

                    SecurityContextHolder.getContext().authentication = authentication
                }
        }
        filterChain.doFilter(request, response)
    }

    private fun HttpServletRequest.getBearerToken(): String? {
        val headerValue = this.getHeader(HttpHeaders.AUTHORIZATION) ?: return null

        return BEARER_PATTERN.find(headerValue)?.groupValues?.get(1)
    }
}