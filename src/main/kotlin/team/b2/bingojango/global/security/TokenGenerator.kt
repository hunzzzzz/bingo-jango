package team.b2.bingojango.global.security

import org.springframework.stereotype.Service

@Service
class TokenGenerator {

    fun generateToken(): String {
        val tokenLength = 10
        val tokenChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        val random = java.util.Random()
        return (1..tokenLength)
            .map { tokenChars[random.nextInt(tokenChars.length)] }
            .joinToString("")
    }
}