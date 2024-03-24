package team.b2.bingojango.global.security.jwt

import org.springframework.data.jpa.repository.JpaRepository
import team.b2.bingojango.domain.user.model.User

interface TokenRepository : JpaRepository<RefreshToken, Long> {
    fun findByUser(user: User): RefreshToken?
}