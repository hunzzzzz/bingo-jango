package team.b2.bingojango.domain.purchase_product.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.b2.bingojango.domain.purchase_product.model.PurchaseProduct

@Repository
interface PurchaseProductRepository : JpaRepository<PurchaseProduct, Long> {
}