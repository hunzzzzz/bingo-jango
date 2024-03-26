package team.b2.bingojango.domain.user.model

import jakarta.persistence.*
import team.b2.bingojango.domain.user.dto.request.ProfileUpdateRequest
import team.b2.bingojango.domain.user.dto.response.SignUpResponse
import team.b2.bingojango.global.entity.BaseEntity
import team.b2.bingojango.global.oauth.domain.entity.OAuth2Provider
import java.time.LocalDateTime
import java.time.ZonedDateTime

@Entity
@Table(name = "Users")
class User(
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    var role: UserRole = UserRole.USER,

    @Column(name = "name", nullable = true)
    var name: String?,

    @Column(name = "nickname", nullable = false)
    var nickname: String,

    @Column(name = "phone", nullable = true)
    var phone: String?,

    @Column(name = "email", nullable = false)
    var email: String,

    @Column(name = "password", nullable = true)
    var password: String?,

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = true)
    val provider: OAuth2Provider?,

    @Column(name = "provider_id", nullable = true)
    val providerId: String?,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    var status: UserStatus = UserStatus.NORMAL,

    @Column(name = "image", nullable = true)
    var image: String?
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    val id: Long? = null


    fun updateProfileSupport(request: ProfileUpdateRequest){
        this.name = request.name
        this.nickname = request.nickname
        this.email = request.email
        this.phone = request.phone
    }

    companion object {
        fun User.toResponse(): SignUpResponse {
            return SignUpResponse(
                id = this.id!!,
                name = this.name!!,
                nickname = this.nickname,
                email = this.email,
                phone = this.phone!!,
                createdAt = ZonedDateTime.now(),
            )
        }
    }
}

