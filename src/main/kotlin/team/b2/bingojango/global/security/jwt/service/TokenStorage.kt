package team.b2.bingojango.global.security.jwt.service

import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

//TODO: 추후 캐싱에 이용할 예정
@Component
class TokenStorage {

    private val tokensStorage = ConcurrentHashMap<Long, String>()

    //토큰 무효화
    fun invalidateUserTokens(userId: Long) {
        tokensStorage.remove(userId)
    }

    //토큰 저장
    fun storeToken(userId: Long, token: String) {
        tokensStorage[userId] = token
    }

    //토큰 유효 확인
    fun isTokenValid(userId: Long, token: String): Boolean {
        return tokensStorage.getOrDefault(userId, "") == token
    }
}