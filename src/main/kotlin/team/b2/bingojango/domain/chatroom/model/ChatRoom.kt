package team.b2.bingojango.domain.chatroom.model

import jakarta.persistence.*
import team.b2.bingojango.domain.refrigerator.model.Refrigerator
import team.b2.bingojango.global.entity.BaseEntity

@Entity
@Table(name = "chat")
class ChatRoom(

    @Column(name = "name", nullable = false)
    var name: String,

    @OneToOne
    @JoinColumn(name = "refrigerator")
    val refrigerator: Refrigerator,

    @Column
    @Enumerated(EnumType.STRING)
    var chatRoomStatus: ChatRoomStatus

) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id", nullable = false)
    val id: Long? = null
}