package team.b2.bingojango.global.security.jwt.repository

import org.springframework.data.jpa.repository.JpaRepository
import team.b2.bingojango.global.security.jwt.model.UserToken

interface TokenRepository : JpaRepository<UserToken, Long> {
}