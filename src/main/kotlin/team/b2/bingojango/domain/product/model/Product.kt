package team.b2.bingojango.domain.product.model

import jakarta.persistence.*
import team.b2.bingojango.domain.food.model.Food

@Entity
@Table(name = "Products")
class Product(
    @OneToOne
    @JoinColumn(name = "food_id")
    val food: Food
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    val id: Long? = null
}