package team.b2.bingojango.global.oauth.api.oauth2login.service

import org.springframework.stereotype.Service
import team.b2.bingojango.global.oauth.client.oauth2.OAuth2LoginUserInfo
import team.b2.bingojango.global.oauth.domain.entity.SocialMember
import team.b2.bingojango.global.oauth.domain.repository.SocialMemberRepository

@Service
class SocialMemberService(
    private val socialMemberRepository: SocialMemberRepository
) {
    // OAuth2LoginUserInfo를 회원가입 시키는 역할
    fun registerIfAbsent(userInfo: OAuth2LoginUserInfo): SocialMember {
        return if (!socialMemberRepository.existsByProviderAndProviderId(userInfo.provider, userInfo.id)) {
            val socialMember = SocialMember(
                provider = userInfo.provider,
                providerId = userInfo.id,
                nickname = userInfo.nickname
            )
            socialMemberRepository.save(socialMember)
        } else {
            socialMemberRepository.findByProviderAndProviderId(userInfo.provider, userInfo.id)
        }
    }
}