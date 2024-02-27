package team.b2.bingojango.domain.purchase_food.model

import jakarta.persistence.*
import team.b2.bingojango.domain.food.model.Food
import team.b2.bingojango.domain.purchase.model.Purchase

@Entity
@Table(name = "Purchase_Food")
class PurchaseFood(
    @Column(name = "count", nullable = false)
    val count: Int,

    @ManyToOne
    @JoinColumn(name = "purchase_id")
    val purchase: Purchase,

    @ManyToOne
    @JoinColumn(name = "food_id")
    val food: Food
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_food_id")
    val id: Long? = null
}