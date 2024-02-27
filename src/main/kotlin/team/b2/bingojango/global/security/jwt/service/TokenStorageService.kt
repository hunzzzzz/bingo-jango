package team.b2.bingojango.global.security.jwt.service

import org.springframework.stereotype.Service
import team.b2.bingojango.global.security.jwt.model.UserToken
import team.b2.bingojango.global.security.jwt.repository.TokenRepository
import java.time.LocalDateTime

@Service
class TokenStorageService(
    private val tokenRepository: TokenRepository
) {
    //토큰 저장
    fun storeToken(userId: Long, token: String, expiryDate: LocalDateTime) {
        val userToken = UserToken(userId = userId, token = token, expiryDate = expiryDate)
        tokenRepository.save(userToken)
    }

    //토큰 유효 확인
    fun validateToken(userId: Long, token: String): Boolean {
        val userToken = tokenRepository.findById(userId).orElse(null) ?: return false
        return userToken.token == token && userToken.expiryDate.isAfter(LocalDateTime.now())
    }

    //토큰 무효화
    fun invalidateToken(userId: Long) {
        tokenRepository.deleteById(userId)
    }
}
