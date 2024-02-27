package team.b2.bingojango.domain.food.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import team.b2.bingojango.domain.food.repository.FoodRepository
import team.b2.bingojango.domain.purchase.model.PurchaseStatus
import team.b2.bingojango.domain.purchase.repository.PurchaseRepository
import team.b2.bingojango.domain.purchase.service.PurchaseService
import team.b2.bingojango.domain.purchase_food.model.PurchaseFood
import team.b2.bingojango.domain.purchase_food.repository.PurchaseFoodRepository

@Service
class FoodService(
    private val foodRepository: FoodRepository,
    private val purchaseService: PurchaseService,
    private val purchaseRepository: PurchaseRepository,
    private val purchaseFoodRepository: PurchaseFoodRepository
) {
    // [API] 해당 식품을 n개 만큼 공동구매 신청
    fun addFoodToPurchase(refrigeratorId: Long, foodId: Long, count: Int) =
        getCurrentPurchase(refrigeratorId).let {
            checkAlreadyInPurchase() // TODO : 현재 공동구매 진행 중인 식품은 추가할 수 없음을 검증해야 함
            purchaseFoodRepository.save(
                PurchaseFood(
                    count = count,
                    purchase = it,
                    food = getFood(foodId)
                )
            )
            "${getFood(foodId).name} ${count}개가 공동구매 신청되었습니다." // TODO: 추후 분리 예정
        }

    /*
        [내부 메서드] 현재 진행 중인 Purchase (공동구매)를 리턴
            - status 가 ON_VOTE (투표 진행중) 인 Purchase 확인
            - status 가 ON_VOTE (투표 진행중) 인 Purchase 가 없다면, 새로운 Purchase 객체를 생성 후 리턴
            * TODO : 추후 조회 과정 리팩토링 필요
     */
    private fun getCurrentPurchase(refrigeratorId: Long) =
        purchaseRepository.findAll().firstOrNull { it.status == PurchaseStatus.ON_VOTE }
            ?: purchaseService.makePurchase(refrigeratorId)

    /*
        [내부 메서드] 현재 공동구매 진행 중인 식품은 공동구매 목록에 추가할 수 없음을 검증
            * TODO : 추후 구현 예정
     */
    private fun checkAlreadyInPurchase() {}

    // [내부 메서드] id로 Food 객체 가져오기
    private fun getFood(foodId: Long) =
        foodRepository.findByIdOrNull(foodId) ?: throw Exception("") // TODO
}