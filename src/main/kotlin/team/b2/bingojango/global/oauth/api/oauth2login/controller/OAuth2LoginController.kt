package team.b2.bingojango.global.oauth.api.oauth2login.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.b2.bingojango.domain.user.dto.response.LoginResponse
import team.b2.bingojango.global.oauth.api.oauth2login.service.OAuth2LoginService
import team.b2.bingojango.global.oauth.client.oauth2.OAuth2ClientService
import team.b2.bingojango.global.oauth.domain.entity.OAuth2Provider

@Tag(name = "oauth2", description = "소셜로그인")
@RestController
class OAuth2LoginController(
    private val oAuth2LoginService: OAuth2LoginService,
    private val oAuth2ClientService: OAuth2ClientService
) {
    @Operation(summary = "로그인 페이지로 Redirect 해주기")
    @PreAuthorize("isAnonymous()")
    @GetMapping("/oauth2/login/{provider}")
    fun redirectLoginPage(@PathVariable provider: OAuth2Provider, response: HttpServletResponse) {
        val loginPageUrl = oAuth2ClientService.generateLoginPageUrl(provider)
        response.sendRedirect(loginPageUrl)
    }


    @Operation(summary = "provider 인증 후 AccessToken 반환")
    @PreAuthorize("isAnonymous()")
    @GetMapping("/oauth2/callback/{provider}")
    fun callback(
        @PathVariable provider: OAuth2Provider, response: HttpServletResponse,
        @RequestParam(name = "code") authorizationCode: String
    ): LoginResponse {
        return oAuth2LoginService.login(provider, response, authorizationCode)
    }
}