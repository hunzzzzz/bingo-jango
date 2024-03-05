package team.b2.bingojango.domain.purchase_product.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.b2.bingojango.domain.product.model.Product
import team.b2.bingojango.domain.purchase.model.Purchase
import team.b2.bingojango.domain.purchase_product.model.PurchaseProduct
import team.b2.bingojango.domain.refrigerator.model.Refrigerator

@Repository
interface PurchaseProductRepository : JpaRepository<PurchaseProduct, Long> {
    fun findAllByPurchase(purchase: Purchase): List<PurchaseProduct>

    fun findByRefrigeratorAndProductAndPurchase(refrigerator: Refrigerator, product: Product, purchase: Purchase): PurchaseProduct?
}