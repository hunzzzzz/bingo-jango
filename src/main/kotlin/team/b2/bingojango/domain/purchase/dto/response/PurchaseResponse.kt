package team.b2.bingojango.domain.purchase.dto.response

import team.b2.bingojango.domain.member.model.Member
import team.b2.bingojango.domain.purchase_product.dto.response.PurchaseProductResponse

data class PurchaseResponse(
    val memberNickName: String,
    val purchaseProductList: List<PurchaseProductResponse>
) {
    companion object {
        fun from(member: Member, purchaseProductList: List<PurchaseProductResponse>) = PurchaseResponse(
            memberNickName = member.user.nickname,
            purchaseProductList = purchaseProductList
        )
    }
}