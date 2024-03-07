package team.b2.bingojango.domain.member.model

import jakarta.persistence.*
import team.b2.bingojango.domain.chatting.model.ChatRoom
import team.b2.bingojango.domain.refrigerator.model.Refrigerator
import team.b2.bingojango.domain.user.model.User
import team.b2.bingojango.global.entity.BaseEntity

@Entity
@Table(name = "Members")
class Member(
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    val role: MemberRole,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne
    @JoinColumn(name = "refrigerator_id")
    val refrigerator: Refrigerator,

    @ManyToOne
    @JoinColumn(name = "chat_id")
    val chatRoom: ChatRoom,
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    val id: Long? = null

    companion object {
        fun toEntity(user: User, role:MemberRole, refrigerator: Refrigerator, chatRoom: ChatRoom): Member {
            return Member(
                role = role,
                user = user,
                refrigerator = refrigerator,
                chatRoom = chatRoom
            )
        }
    }
}