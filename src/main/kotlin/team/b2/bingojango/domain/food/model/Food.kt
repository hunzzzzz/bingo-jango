package team.b2.bingojango.domain.food.model

import jakarta.persistence.*
import team.b2.bingojango.domain.groupbuying.model.GroupBuying
import team.b2.bingojango.domain.refrigerator.model.Refrigerator
import team.b2.bingojango.domain.refrigerator.model.RefrigeratorStatus
import team.b2.bingojango.global.entity.BaseEntity
import java.time.ZonedDateTime

@Entity
@Table(name = "Foods")
class Food(
    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    val category: RefrigeratorStatus,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "expiration_date", nullable = false)
    val expirationDate: ZonedDateTime,

    @ManyToOne
    val groupBuying: GroupBuying?,

    @ManyToOne
    val refrigerator: Refrigerator?
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}