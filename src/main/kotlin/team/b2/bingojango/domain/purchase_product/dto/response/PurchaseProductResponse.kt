package team.b2.bingojango.domain.purchase_product.dto.response

import team.b2.bingojango.domain.purchase_product.model.PurchaseProduct

data class PurchaseProductResponse(
    val foodName: String,
    val foodCount: Int
) {
    companion object {
        fun from(purchaseProduct: PurchaseProduct) = PurchaseProductResponse(
            foodName = purchaseProduct.product.food.name,
            foodCount = purchaseProduct.count
        )
    }
}