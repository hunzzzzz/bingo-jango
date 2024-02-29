package team.b2.bingojango.domain.purchase.dto.response

import team.b2.bingojango.domain.member.model.Member
import team.b2.bingojango.domain.purchase.model.Purchase
import team.b2.bingojango.domain.purchase.model.PurchaseStatus
import team.b2.bingojango.domain.purchase_product.dto.response.PurchaseProductResponse

data class PurchaseResponse(
    val memberNickName: String,
    val status: PurchaseStatus,
    val purchaseProductList: List<PurchaseProductResponse>
) {
    companion object {
        fun from(purchase: Purchase, member: Member, purchaseProductList: List<PurchaseProductResponse>) =
            PurchaseResponse(
                memberNickName = member.user.nickname,
                status = purchase.status,
                purchaseProductList = purchaseProductList
            )
    }
}