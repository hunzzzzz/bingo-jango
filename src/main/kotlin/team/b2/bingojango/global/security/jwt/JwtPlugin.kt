package team.b2.bingojango.global.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import team.b2.bingojango.domain.user.model.User
import team.b2.bingojango.domain.user.repository.UserRepository
import team.b2.bingojango.global.exception.cases.ModelNotFoundException
import team.b2.bingojango.global.security.util.UserPrincipal
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.time.Instant
import java.util.*

@Component
@PropertySource("classpath:application.yml")
class JwtPlugin(
    @Value("\${auth.jwt.issuer}") private val issuer: String,
    @Value("\${auth.jwt.secret}") private val secret: String,
    @Value("\${auth.jwt.accessTokenExpirationHour}") private val accessTokenExpirationHour: Long,
    @Value("\${auth.jwt.refreshTokenExpirationHour}") private val refreshTokenExpirationHour: Long,
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
) {
    //AccessToken 생성
    fun generateAccessToken(subject: String, email: String, role: String): String {
        return generateToken(subject, email, role, Duration.ofHours(accessTokenExpirationHour))
    }

    //RefreshToken 생성
    fun generateRefreshToken(subject: String, email: String, role: String): String {
        return generateToken(subject, email, role, Duration.ofHours(refreshTokenExpirationHour))
    }

    //Token 생성
    private fun generateToken(subject: String, email: String, role: String, expirationPeriod: Duration): String {
        val claims: Claims = Jwts.claims()
            .add(mapOf("role" to role, "email" to email))
            .build()
        val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))
        val now = Instant.now()
        return Jwts.builder()
            .subject(subject)
            .issuer(issuer)
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plus(expirationPeriod)))
            .claims(claims)
            .signWith(key)
            .compact()
    }

    //Token Dto 생성
    fun generateTokenDto(oAuth2User: OAuth2User): JwtDto {
        val email = oAuth2User.attributes["email"] as String
        val user = userRepository.findByEmail(email) ?: throw ModelNotFoundException("User")
        val subject = user.id.toString()
        val role = user.role.toString()
        val refreshToken = generateRefreshToken(subject, email, role)
        val accessToken = generateAccessToken(subject, email, role)
        return JwtDto(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

    //토큰 검증
    fun validateToken(jwt: String): Result<Jws<Claims>>{
        return kotlin.runCatching {
            val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))
            Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt)
        }
    }

    //토큰 저장
    @Transactional
    fun storeToken(user: User, refreshToken: String) {
        tokenRepository.save(RefreshToken(user, refreshToken))
    }

    //토큰 삭제
    @Transactional
    fun deleteToken(userPrincipal: UserPrincipal) {
        val user = userRepository.findByIdOrNull(userPrincipal.id) ?: throw ModelNotFoundException ("User")
        val refreshToken = tokenRepository.findByUser(user) ?: throw ModelNotFoundException("RefreshToken")
        tokenRepository.delete(refreshToken)
    }
}