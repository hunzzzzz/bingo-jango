package team.b2.bingojango.global.security.util

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

object CookieUtil {

    fun getCookie(request: HttpServletRequest, name: String): Cookie? {
        val cookies = request.cookies ?: return null
        return cookies.firstOrNull { it.name == name }
    }

    fun addCookie(response: HttpServletResponse, name: String, value: String, maxAge: Int) {
        val cookie = Cookie(name, value).apply {
            isHttpOnly = true // JavaScript를 통한 접근 방지
            secure = true // HTTPS를 통해서만 쿠키 전송
            path = "/" // 쿠키를 전송할 요청 경로
            setMaxAge(maxAge) // 쿠키 유효 시간
        }
        response.addCookie(cookie)
    }

    fun deleteCookie(request: HttpServletRequest, response: HttpServletResponse, name: String) {
        val cookie = getCookie(request, name) ?: return
        cookie.apply {
            value = ""
            path = "/"
            maxAge = 0
        }
        response.addCookie(cookie)
    }
}