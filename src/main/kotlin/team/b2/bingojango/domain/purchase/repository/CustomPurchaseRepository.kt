package team.b2.bingojango.domain.purchase.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import team.b2.bingojango.domain.purchase.model.Purchase
import team.b2.bingojango.domain.purchase.model.PurchaseStatus

@Repository
interface CustomPurchaseRepository {
    fun searchPurchase(
        status: PurchaseStatus?,
        pageable: Pageable
    ): Page<Purchase>
}