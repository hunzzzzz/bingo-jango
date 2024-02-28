package team.b2.bingojango.domain.member.model

import jakarta.persistence.*
import team.b2.bingojango.domain.chatroom.model.ChatRoom
import team.b2.bingojango.domain.refrigerator.model.Refrigerator
import team.b2.bingojango.domain.user.model.User

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
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    val id: Long? = null

    companion object {
        fun toEntity(user: User, refrigerator: Refrigerator, chatRoom: ChatRoom): Member {
            return Member(
                role = MemberRole.STAFF,
                user = user,
                refrigerator = refrigerator,
                chatRoom = chatRoom
            )
        }
    }
}