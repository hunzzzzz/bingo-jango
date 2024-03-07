package team.b2.bingojango.global.oauth.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.b2.bingojango.global.security.jwt.JwtDto
import team.b2.bingojango.global.oauth.service.OAuthService

@RestController
@RequestMapping("/auth")
class OAuthController(
    private val oAuthService: OAuthService
) {
     //token 생성해서 보내주기
    @GetMapping("/google")
    fun login(): ResponseEntity<Unit> {
        println(SecurityContextHolder.getContext().authentication)
//        return ResponseEntity.ok(oAuthService.login(oAuth2User))
         return ResponseEntity.ok().build()
    }
}