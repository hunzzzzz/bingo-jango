package team.b2.bingojango.global.security.jwt.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.b2.bingojango.domain.user.model.User
import team.b2.bingojango.domain.user.repository.UserRepository
import team.b2.bingojango.global.exception.cases.ModelNotFoundException
import team.b2.bingojango.global.security.UserPrincipal
import team.b2.bingojango.global.security.jwt.model.RefreshToken
import team.b2.bingojango.global.security.jwt.repository.TokenRepository

@Service
class TokenUtil(
    private val tokenRepository: TokenRepository,
    private val userRepository: UserRepository
) {
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

    //토큰 유효 확인
//    fun validateToken(userPrincipal: UserPrincipal, token: String): Boolean {
//        val userToken = tokenRepository.findById(userPrincipal.id).orElse(null) ?: return false
//        return userToken.token == token && userToken.expiryDate.isAfter(ZonedDateTime.now())
//    }
}
