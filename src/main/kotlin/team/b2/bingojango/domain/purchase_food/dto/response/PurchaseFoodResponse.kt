package team.b2.bingojango.domain.purchase_food.dto.response

import team.b2.bingojango.domain.purchase_food.model.PurchaseFood

data class PurchaseFoodResponse(
    // TODO : 추후 공동구매를 신청한 회원의 이름도 리턴 예정
    val foodName: String,
    val foodCount: Int
) {
    companion object {
        fun from(purchaseFood: PurchaseFood) = PurchaseFoodResponse(
            foodName = purchaseFood.food.name,
            foodCount = purchaseFood.count
        )
    }
}