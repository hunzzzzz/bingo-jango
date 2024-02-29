package team.b2.bingojango.domain.vote.model

import jakarta.persistence.*
import team.b2.bingojango.domain.member.model.Member
import team.b2.bingojango.domain.purchase.model.Purchase
import team.b2.bingojango.domain.refrigerator.model.Refrigerator
import team.b2.bingojango.global.entity.BaseEntity
import java.time.ZonedDateTime

@Entity
@Table(name = "Votes")
class Vote(
    @Column(name = "description", nullable = true)
    val description: String?,

    @Column(name = "due_date", nullable = false)
    val dueDate: ZonedDateTime,

    @OneToOne
    @JoinColumn(name = "purchase_id")
    val purchase: Purchase,

    @ManyToOne
    @JoinColumn(name = "refrigerator_id")
    val refrigerator: Refrigerator,

    @ManyToMany
    val voters: MutableSet<Member>
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_id", nullable = false)
    val id: Long? = null

    fun updateVote(member: Member) {
        voters.add(member)
    }
}