package team.b2.bingojango.domain.purchase_food.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.b2.bingojango.domain.purchase_food.model.PurchaseFood

@Repository
interface PurchaseFoodRepository : JpaRepository<PurchaseFood, Long> {
}