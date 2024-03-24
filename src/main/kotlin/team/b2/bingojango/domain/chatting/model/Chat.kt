package team.b2.bingojango.domain.chatting.model

import jakarta.persistence.*
import team.b2.bingojango.domain.member.model.Member
import team.b2.bingojango.global.entity.BaseEntity

@Entity
@Table(name = "chat")
class Chat(

    @Column(name = "content")
    val content: String,

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    val status: ChatStatus,

    @ManyToOne
    @JoinColumn(name = "chatroom_id")
    val chatRoom: ChatRoom,

    @ManyToOne
    @JoinColumn(name = "member_id")
    val member: Member,

    ) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    val id: Long? = null
}