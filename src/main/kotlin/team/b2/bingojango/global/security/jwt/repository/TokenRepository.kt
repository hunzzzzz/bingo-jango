package team.b2.bingojango.global.security.jwt.repository

import org.springframework.data.jpa.repository.JpaRepository
import team.b2.bingojango.domain.user.model.User
import team.b2.bingojango.global.security.jwt.model.RefreshToken

interface TokenRepository : JpaRepository<RefreshToken, Long> {
    fun findByUser(user: User): RefreshToken?
}