package team.b2.bingojango.domain.purchase.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.b2.bingojango.domain.food.model.Food
import team.b2.bingojango.domain.member.model.MemberRole
import team.b2.bingojango.domain.member.repository.MemberRepository
import team.b2.bingojango.domain.product.model.Product
import team.b2.bingojango.domain.product.repository.ProductRepository
import team.b2.bingojango.domain.purchase.dto.response.PurchaseResponse
import team.b2.bingojango.domain.purchase.model.Purchase
import team.b2.bingojango.domain.purchase.model.PurchaseStatus
import team.b2.bingojango.domain.purchase.repository.PurchaseRepository
import team.b2.bingojango.domain.purchase_product.dto.response.PurchaseProductResponse
import team.b2.bingojango.domain.purchase_product.model.PurchaseProduct
import team.b2.bingojango.domain.purchase_product.repository.PurchaseProductRepository
import team.b2.bingojango.domain.refrigerator.model.Refrigerator
import team.b2.bingojango.domain.vote.dto.request.VoteRequest
import team.b2.bingojango.domain.vote.repository.VoteRepository
import team.b2.bingojango.global.exception.cases.*
import team.b2.bingojango.global.security.UserPrincipal
import team.b2.bingojango.global.util.EntityFinder

@Service
@Transactional
class PurchaseService(
    private val memberRepository: MemberRepository,
    private val purchaseRepository: PurchaseRepository,
    private val purchaseProductRepository: PurchaseProductRepository,
    private val productRepository: ProductRepository,
    private val voteRepository: VoteRepository,
    private val entityFinder: EntityFinder
) {
    /*
    [API] 해당 식품을 n개 만큼 공동구매 신청
        - 검증 조건 1 : 관리자(STAFF)만 공동구매를 신청할 수 있음
        - 검증 조건 2 : 다른 관리자가 시작한 공동구매가 있는 경우 신청할 수 없음
        - 검증 조건 3 : 현재 공동구매 중인 식품은 추가할 수 없음
*/
    fun addFoodToPurchase(userPrincipal: UserPrincipal, refrigeratorId: Long, foodId: Long, count: Int) =
        getCurrentPurchase(userPrincipal, refrigeratorId).let {
            if (entityFinder.getMember(userPrincipal.id, refrigeratorId).role != MemberRole.STAFF)
                throw InvalidRoleException()
            else if (purchaseRepository.existsByStatus(PurchaseStatus.ACTIVE) && getCurrentPurchase().proposedBy != userPrincipal.id)
                throw AlreadyHaveActivePurchaseException()
            else if (purchaseProductRepository.findAllByPurchase(getCurrentPurchase())
                    .map { purchaseProduct -> purchaseProduct.product.food }
                    .contains(entityFinder.getFood(foodId))
            ) throw AlreadyInPurchaseException()

            purchaseProductRepository.save(
                PurchaseProduct(
                    count = count,
                    purchase = it,
                    product = getProduct(foodId, refrigeratorId),
                    refrigerator = entityFinder.getRefrigerator(refrigeratorId)
                )
            )
            "${entityFinder.getFood(foodId).name} ${count}개가 공동구매 신청되었습니다." // TODO: 추후 분리 예정
        }

    /*
        [API] 공동구매 목록에서 특정 식품 삭제
            - 검증 조건 1: 해당 공동구매를 올린 사람만 수정을 할 수 있음
            - 검증 조건 2: 현재 공동구매에 존재하는 식품만 수정할 수 있음
    */
    fun updateFoodInPurchase(userPrincipal: UserPrincipal, refrigeratorId: Long, foodId: Long, count: Int) {
        if (getCurrentPurchase().proposedBy != userPrincipal.id)
            throw InvalidRoleException()
        (purchaseProductRepository.findByRefrigeratorAndProduct(
            refrigerator = entityFinder.getRefrigerator(refrigeratorId),
            product = getProduct(foodId, refrigeratorId)
        ) ?: throw ModelNotFoundException("식품")).updateCount(count)
    }

    /*
        [API] 공동구매 목록에서 특정 식품 삭제
            - 검증 조건 1: 해당 공동구매를 올린 사람만 삭제를 할 수 있음
            - 검증 조건 2: 현재 공동구매에 존재하는 식품만 삭제할 수 있음
     */
    fun deleteFoodFromPurchase(userPrincipal: UserPrincipal, refrigeratorId: Long, foodId: Long) {
        if (getCurrentPurchase().proposedBy != userPrincipal.id)
            throw InvalidRoleException()
        purchaseProductRepository.delete(
            purchaseProductRepository.findByRefrigeratorAndProduct(
                refrigerator = entityFinder.getRefrigerator(refrigeratorId),
                product = getProduct(foodId, refrigeratorId)
            ) ?: throw ModelNotFoundException("식품")
        )
    }

    // [API] 현재 진행 중인 Purchase 목록을 출력
    fun showPurchase(refrigeratorId: Long) =
        getCurrentPurchase()
            .let {
                PurchaseResponse.from(
                    member = entityFinder.getMember(it.proposedBy, refrigeratorId),
                    purchaseProductList = purchaseProductRepository.findAllByPurchase(it)
                        .map { purchaseProduct -> PurchaseProductResponse.from(purchaseProduct) }
                )
            }

    /*
        [API] 현재 공동구매 목록에 대한 투표 시작
            - 검증 조건 1: 공동구매를 신청한 회원 본인만 투표를 시작할 수 있음
            - 검증 조건 2: STAFF 의 수가 1명인 경우, 투표 과정을 생략하고 현재 Purchase 의 status 를 FINISHED 로 변경
     */
    fun startVote(userPrincipal: UserPrincipal, refrigeratorId: Long, voteRequest: VoteRequest) {
        if (getCurrentPurchase().proposedBy != userPrincipal.id) throw InvalidRoleException()
        else if (getNumberOfStaff() == 1L) getCurrentPurchase().updateStatus(PurchaseStatus.FINISHED)

        voteRepository.save(
            voteRequest.to(
                request = voteRequest,
                refrigerator = entityFinder.getRefrigerator(refrigeratorId),
                member = entityFinder.getMember(userPrincipal.id, refrigeratorId)
            )
        )
    }

    /*
        [API] 투표 실시
            - 검증 조건 1: 관리자(STAFF)만 투표를 할 수 있음
            - 검증 조건 2: 이미 투표한 경우, 투표 결과를 수정할 수 없음
            - 검증 조건 3-1: 찬성에 투표한 경우, voters 에 해당 Member 객체를 add
            - 검증 조건 3-2: 만장일치가 완성된 경우, 투표를 종료하고 현재 Purchase 의 status 를 FINISHED 로 변경
            - 검증 조건 4: 반대에 투표한 경우, 투표를 종료하고 현재 Purchase 의 status 를 REJECTED 로 변경
     */
    fun vote(userPrincipal: UserPrincipal, refrigeratorId: Long, voteId: Long, isAccepted: Boolean) =
        entityFinder.getMember(userPrincipal.id, refrigeratorId)
            .let {
                if (it.role != MemberRole.STAFF)
                    throw InvalidRoleException()
                else if (entityFinder.getVote(voteId).voters.contains(it))
                    throw DuplicatedVoteException()

                entityFinder.getVote(voteId).let { vote ->
                    if (isAccepted) {
                        vote.updateVote(entityFinder.getMember(userPrincipal.id, refrigeratorId))
                        if (getNumberOfStaff() == vote.voters.size.toLong())
                            getCurrentPurchase().updateStatus(PurchaseStatus.FINISHED)
                    } else
                        getCurrentPurchase().updateStatus(PurchaseStatus.REJECTED)
                }
            }

    // [내부 메서드] 현재 진행 중인(status 가 ACTIVE 한) Purchase 를 리턴 (없으면 예외 처리)
    private fun getCurrentPurchase() =
        purchaseRepository.findAll().firstOrNull { it.status == PurchaseStatus.ACTIVE }
            ?: throw NoCurrentPurchaseException()

    // [내부 메서드] 현재 진행 중인(status 가 ACTIVE 한) Purchase 를 리턴 (없으면 새로운 Purchase 객체 생성 후 리턴)
    private fun getCurrentPurchase(userPrincipal: UserPrincipal, refrigeratorId: Long) =
        purchaseRepository.findAll().firstOrNull { it.status == PurchaseStatus.ACTIVE }
            ?: makePurchase(userPrincipal, entityFinder.getRefrigerator(refrigeratorId))

    // [내부 메서드] Purchase 객체 생성
    private fun makePurchase(userPrincipal: UserPrincipal, refrigerator: Refrigerator) =
        purchaseRepository.save(
            Purchase(
                status = PurchaseStatus.ACTIVE,
                proposedBy = userPrincipal.id,
                refrigerator = refrigerator
            )
        )

    // [내부 메서드] 현재 냉장고 내에 등록된 (해당 식품에 대한) Product 를 리턴 (없으면 새로운 Product 객체 생성 후 리턴)
    private fun getProduct(foodId: Long, refrigeratorId: Long): Product =
        productRepository.findByFoodAndRefrigerator(
            entityFinder.getFood(foodId),
            entityFinder.getRefrigerator(refrigeratorId)
        )
            ?: addProduct(entityFinder.getFood(foodId), entityFinder.getRefrigerator(refrigeratorId))

    // [내부 메서드] Product 객체 생성
    fun addProduct(food: Food, refrigerator: Refrigerator) =
        productRepository.save(
            Product(food = food, refrigerator = refrigerator)
        )

    // [내부 메서드] 현재 Refrigerator 내 관리자(STAFF) 의 수
    private fun getNumberOfStaff() = memberRepository.countByRole(MemberRole.STAFF)
}