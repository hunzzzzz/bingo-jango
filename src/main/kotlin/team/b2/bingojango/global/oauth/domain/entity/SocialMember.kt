package team.b2.bingojango.global.oauth.domain.entity

import jakarta.persistence.*

//소셜로그인 성공했을 때, 새로운 회원을 저장시키기 위한 테이블
@Entity
class SocialMember(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "social_member_id")
    var id: Long? = null,

    @Enumerated(EnumType.STRING)
    val provider: OAuth2Provider,
    val providerId: String,
    val nickname: String
)