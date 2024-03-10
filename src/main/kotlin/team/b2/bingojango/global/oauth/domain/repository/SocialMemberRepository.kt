package team.b2.bingojango.global.oauth.domain.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.b2.bingojango.global.oauth.domain.entity.OAuth2Provider
import team.b2.bingojango.global.oauth.domain.entity.SocialMember

@Repository
interface SocialMemberRepository : CrudRepository<SocialMember, Long> {
    fun existsByProviderAndProviderId(kakao: OAuth2Provider, toString: String): Boolean
    fun findByProviderAndProviderId(kakao: OAuth2Provider, toString: String): SocialMember
}