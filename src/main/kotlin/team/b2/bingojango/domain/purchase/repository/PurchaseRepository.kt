package team.b2.bingojango.domain.purchase.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.b2.bingojango.domain.purchase.model.Purchase
import team.b2.bingojango.domain.purchase.model.PurchaseStatus

@Repository
interface PurchaseRepository : JpaRepository<Purchase, Long>, CustomPurchaseRepository {
    fun existsByStatus(status: PurchaseStatus): Boolean
}