package team.b2.bingojango.domain.purchase.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.b2.bingojango.domain.purchase.service.PurchaseService

@RestController
@RequestMapping("/")
class PurchaseController(
    private val purchaseService: PurchaseService
) {
}