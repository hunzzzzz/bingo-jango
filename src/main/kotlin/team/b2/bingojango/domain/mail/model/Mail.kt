package team.b2.bingojango.domain.mail.model

import jakarta.persistence.*
import team.b2.bingojango.domain.refrigerator.model.Refrigerator
import java.time.LocalDateTime

@Entity
@Table(name = "mails")
class Mail(
    @ManyToOne
    @JoinColumn(name = "refrigerator_id")
    val refrigerator: Refrigerator,

    @Column(name="email", nullable = false)
    val email: String,

    @Column(name="code", nullable = false)
    var code: String,

    @Column(name="verification_code", nullable = true)
    var verificationCode: String? = null,

    @Column(name="verification_code_created_at", nullable = true)
    var verificationCodeCreatedAt: LocalDateTime? = null,

    @Column(name="is_verified", nullable = false)
    var isVerified: Boolean = false,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    companion object {
        fun toEntity(refrigerator: Refrigerator, email: String, code: String): Mail {
            return Mail(
                refrigerator = refrigerator,
                email = email,
                code = code,
            )
        }
    }

    fun generateVerificationCode() {
        this.verificationCode = generateRandomCode() // 이 코드는 무작위로 생성된 인증 코드를 할당하는 메소드입니다.
        this.verificationCodeCreatedAt = LocalDateTime.now()
        // 여기서 휴대폰 번호로 인증번호를 전송하는 로직을 추가할 수 있습니다.
    }

    fun verify(verificationCode: String): Boolean {
        // 저장된 인증번호와 사용자가 입력한 인증번호를 비교하여 확인합니다.
        if (this.verificationCode == verificationCode && !isExpired()) {
            this.isVerified = true
            return true
        }
        return false
    }

    private fun generateRandomCode(): String {
        val random = kotlin.random.Random
        return (100000..999999).random(random).toString() // 6자리 무작위 숫자 생성
    }

    private fun isExpired(): Boolean {
        // 인증번호 생성 시간과 현재 시간을 비교하여 인증번호의 유효시간이 만료되었는지 확인합니다.
        val expirationTime = this.verificationCodeCreatedAt?.plusMinutes(3) // 3분 후 만료됨
        return expirationTime?.isBefore(LocalDateTime.now()) ?: true
    }
}