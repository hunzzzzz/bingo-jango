package team.b2.bingojango.domain.purchase_product.dto.response

import team.b2.bingojango.domain.purchase_product.model.PurchaseProduct

data class PurchaseProductResponse(
    // TODO : 추후 공동구매를 신청한 회원의 이름도 리턴 예정
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