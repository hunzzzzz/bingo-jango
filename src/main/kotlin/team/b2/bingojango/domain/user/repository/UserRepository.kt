package team.b2.bingojango.domain.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.b2.bingojango.domain.user.model.User
import team.b2.bingojango.domain.user.model.UserStatus
import team.b2.bingojango.global.oauth.domain.entity.OAuth2Provider

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(email: String): User?

    fun existsByNickname(nickname: String): Boolean

    fun existsByEmail(email: String): Boolean

    fun findAllByStatus(status: UserStatus): List<User>

    fun findByNameAndPhone(name: String, phone: String): User?

    fun existsByProviderAndProviderId(kakao: OAuth2Provider, toString: String): Boolean

    fun findByProviderAndProviderId(kakao: OAuth2Provider, toString: String): User
}