import org.springframework.stereotype.Service

@Service
class VerificationService {

    fun generateVerificationCode(): String {
        val random = kotlin.random.Random
        return (100000..999999).random(random).toString() // 6자리 무작위 숫자 생성
    }
}