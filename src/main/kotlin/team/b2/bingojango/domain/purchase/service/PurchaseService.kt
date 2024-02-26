package team.b2.bingojango.domain.purchase.service

import org.springframework.stereotype.Service
import team.b2.bingojango.domain.purchase.repository.PurchaseRepository

@Service
class PurchaseService(
    private val purchaseRepository: PurchaseRepository
) {
}