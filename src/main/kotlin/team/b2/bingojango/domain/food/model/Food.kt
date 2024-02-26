package team.b2.bingojango.domain.food.model

import jakarta.persistence.*
import team.b2.bingojango.domain.purchase.model.Purchase
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
    @JoinColumn(name = "purchase_id")
    val purchase: Purchase?,

    @ManyToOne
    @JoinColumn(name = "refrigerator_id")
    val refrigerator: Refrigerator?
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_id", nullable = false)
    val id: Long? = null
}