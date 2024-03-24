package team.b2.bingojango.domain.mail.model

import jakarta.persistence.*
import team.b2.bingojango.domain.refrigerator.model.Refrigerator

@Entity
@Table(name = "mails")
class Mail(
    @ManyToOne
    @JoinColumn(name = "refrigerator_id")
    val refrigerator: Refrigerator,

    @Column(name = "email", nullable = false)
    val email: String,

    @Column(name = "code", nullable = false)
    var code: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    companion object {
        fun toEntity(refrigerator: Refrigerator, email: String, invitationCode: String): Mail {
            return Mail(
                refrigerator = refrigerator,
                email = email,
                code = invitationCode,
            )
        }
    }
}