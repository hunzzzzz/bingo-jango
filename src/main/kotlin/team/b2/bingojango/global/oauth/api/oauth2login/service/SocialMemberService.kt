package team.b2.bingojango.global.oauth.api.oauth2login.service

import org.springframework.stereotype.Service
import team.b2.bingojango.domain.user.model.User
import team.b2.bingojango.domain.user.model.UserRole
import team.b2.bingojango.domain.user.model.UserStatus
import team.b2.bingojango.domain.user.repository.UserRepository
import team.b2.bingojango.global.oauth.client.oauth2.OAuth2LoginUserInfo

@Service
class SocialMemberService(
    private val userRepository: UserRepository
) {
    // OAuth2LoginUserInfo를 회원가입 시키는 역할
    fun registerIfAbsent(userInfo: OAuth2LoginUserInfo): User {
        return if (!userRepository.existsByProviderAndProviderId(userInfo.provider, userInfo.id)) {
            if (userRepository.existsByEmail(userInfo.email)) throw IllegalArgumentException("이미 사용 중인 이메일입니다.")
            val user = User(
                role = UserRole.USER,
                name = null,
                nickname = userInfo.nickname,
                phone = null,
                email = userInfo.email,
                password = null,
                provider = userInfo.provider,
                providerId = userInfo.id,
                status = UserStatus.NORMAL,
                image = null
            )
            userRepository.save(user)
        } else {
            userRepository.findByProviderAndProviderId(userInfo.provider, userInfo.id)
        }
    }
}