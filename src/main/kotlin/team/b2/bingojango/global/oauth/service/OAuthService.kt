package team.b2.bingojango.global.oauth.service

import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.b2.bingojango.domain.user.model.User
import team.b2.bingojango.domain.user.model.UserRole
import team.b2.bingojango.domain.user.model.UserStatus
import team.b2.bingojango.domain.user.repository.UserRepository
import team.b2.bingojango.global.security.jwt.JwtDto
import team.b2.bingojango.global.security.jwt.JwtPlugin
import java.util.UUID

@Service
class OAuthService(
    private val userRepository: UserRepository,
    private val jwtPlugin: JwtPlugin
) {
    @Transactional
    fun login(oAuth2User: OAuth2User) : JwtDto {
        println(oAuth2User.attributes)
        //회원이 아니라면 회원 가입을 시켜준다.
        if(!userRepository.existsByEmail(oAuth2User.attributes["email"] as String)) {
            val user = User(
                email = oAuth2User.attributes["email"] as String,
                role = UserRole.USER,
                name = oAuth2User.attributes["name"] as String,
                nickname = UUID.randomUUID().toString().substring(0,6),
                phone = "",
                password = "",
                status = UserStatus.NORMAL
            )
            userRepository.save(user)
        }

        //token 생성 후 반환
        return jwtPlugin.generateTokenDto(oAuth2User)
    }
}