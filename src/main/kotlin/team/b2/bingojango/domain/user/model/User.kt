package team.b2.bingojango.domain.user.model

import jakarta.persistence.*
import team.b2.bingojango.global.entity.BaseEntity

@Entity
@Table(name = "Users")
class User(
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    val role: UserRole = UserRole.USER,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "nickname", nullable = false)
    val nickname: String,

    @Column(name = "phone", nullable = false)
    val phone: String,

    @Column(name = "email", nullable = false)
    val email: String,

    @Column(name = "password", nullable = false)
    val password: String,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    val status: UserStatus = UserStatus.NORMAL
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    val id: Long? = null
}