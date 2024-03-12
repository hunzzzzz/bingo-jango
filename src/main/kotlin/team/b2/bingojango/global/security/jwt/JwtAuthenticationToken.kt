package team.b2.bingojango.global.security.jwt

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.web.authentication.WebAuthenticationDetails
import team.b2.bingojango.global.security.util.UserPrincipal
import java.io.Serializable

class JwtAuthenticationToken(
    private val principal: UserPrincipal,
    details: WebAuthenticationDetails?,
) : AbstractAuthenticationToken(principal.authorities), Serializable {

    init {
        super.setAuthenticated(true)
        if (details != null)super.setDetails(details)
    }
    override fun getPrincipal() = principal
    override fun getCredentials() = null
    override fun isAuthenticated(): Boolean {
        return true
    }

}