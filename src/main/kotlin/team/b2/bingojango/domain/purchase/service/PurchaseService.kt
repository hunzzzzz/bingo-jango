package team.b2.bingojango.domain.purchase.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.b2.bingojango.domain.purchase.model.Purchase
import team.b2.bingojango.domain.purchase.model.PurchaseStatus
import team.b2.bingojango.domain.purchase.repository.PurchaseRepository
import team.b2.bingojango.domain.purchase_product.dto.response.PurchaseProductResponse
import team.b2.bingojango.domain.purchase_product.repository.PurchaseProductRepository
import team.b2.bingojango.domain.refrigerator.model.Refrigerator
import team.b2.bingojango.domain.refrigerator.repository.RefrigeratorRepository

@Service
@Transactional
class PurchaseService(
    private val purchaseRepository: PurchaseRepository,
    private val purchaseProductRepository: PurchaseProductRepository,
    private val refrigeratorRepository: RefrigeratorRepository
) {

    // [API] 현재 진행 중인 Purchase 목록을 출력
    // TODO : 추후 조회 과정 리팩토링 필요
    fun showPurchase(refrigeratorId: Long) =
        purchaseProductRepository.findAll()
            .filter { it.purchase.status == PurchaseStatus.ON_VOTE }
            .map { PurchaseProductResponse.from(it) }

    // [내부 메서드] Purchase 객체 생성 (FoodService > getCurrentPurchase 에서만 사용되는 메서드)
    fun makePurchase(refrigerator: Refrigerator) =
        purchaseRepository.save(
            Purchase(
                status = PurchaseStatus.ON_VOTE,
                refrigerator = refrigerator
            )
        )
}