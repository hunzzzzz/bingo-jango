package team.b2.bingojango.domain.purchase.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.b2.bingojango.domain.member.model.MemberRole
import team.b2.bingojango.domain.member.repository.MemberRepository
import team.b2.bingojango.domain.purchase.model.Purchase
import team.b2.bingojango.domain.purchase.model.PurchaseStatus
import team.b2.bingojango.domain.purchase.repository.PurchaseRepository
import team.b2.bingojango.domain.purchase_product.dto.response.PurchaseProductResponse
import team.b2.bingojango.domain.purchase_product.repository.PurchaseProductRepository
import team.b2.bingojango.domain.refrigerator.model.Refrigerator
import team.b2.bingojango.domain.refrigerator.repository.RefrigeratorRepository
import team.b2.bingojango.domain.user.repository.UserRepository
import team.b2.bingojango.domain.vote.dto.request.VoteRequest
import team.b2.bingojango.domain.vote.dto.response.VoteResponse
import team.b2.bingojango.domain.vote.model.Vote
import team.b2.bingojango.domain.vote.repository.VoteRepository
import team.b2.bingojango.global.security.UserPrincipal

@Service
@Transactional
class PurchaseService(
    private val userRepository: UserRepository,
    private val memberRepository: MemberRepository,
    private val purchaseRepository: PurchaseRepository,
    private val purchaseProductRepository: PurchaseProductRepository,
    private val voteRepository: VoteRepository,
    private val refrigeratorRepository: RefrigeratorRepository
) {
    // [API] 현재 진행 중인 Purchase 목록을 출력
    // TODO : 추후 조회 과정 리팩토링 필요
    fun showPurchase(refrigeratorId: Long) =
        purchaseProductRepository.findAll()
            .filter { it.purchase.status == PurchaseStatus.ACTIVE }
            .map { PurchaseProductResponse.from(it) }

    /*
        [API] 현재 공동구매 목록에 대한 투표 시작
            - TODO : 공동구매를 신청한 사람만 투표를 시작할 수 있음
            - TODO : 현재 ACTIVE 한 공동구매가 없는 경우 투표가 진행되지 않음
     */
    fun startVote(userPrincipal: UserPrincipal, refrigeratorId: Long, voteRequest: VoteRequest) =
        VoteResponse.from(
            vote = getMember(userPrincipal.id).let {
                voteRepository.save(
                    voteRequest.to(
                        request = voteRequest,
                        refrigerator = getRefrigerator(refrigeratorId),
                        member = it
                    )
                )
            },
            numberOfStaff = getNumberOfStaff()
        )

    /*
        [API] 투표 실시
            - 찬성인 경우, voters 에 해당 Member 객체를 add
            - 만장일치가 완성된 경우, 투표를 종료하고 현재 ACTIVE 상태의 Purchase 를 FINISHED 로 변경
            - 반대가 하나라도 있는 경우, 투표를 종료하고 현재 ACTIVE 상태의 Purchase 를 REJECTED 로 변경
            * TODO : 해당 멤버가 투표 권한이 있는지 확인
     */
    fun vote(userPrincipal: UserPrincipal, refrigeratorId: Long, voteId: Long, isAccepted: Boolean) {
        getVote(voteId).let {
            if (isAccepted) {
                it.vote(getMember(userPrincipal.id))
                if (isCompletedVote(it))
                    getCurrentPurchase(refrigeratorId).updateStatus(PurchaseStatus.FINISHED)
            } else
                getCurrentPurchase(refrigeratorId).updateStatus(PurchaseStatus.REJECTED)
        }
    }

    // [내부 메서드] Purchase 객체 생성 (FoodService > getCurrentPurchase 에서만 사용되는 메서드)
    fun makePurchase(refrigerator: Refrigerator) =
        purchaseRepository.save(
            Purchase(
                status = PurchaseStatus.ACTIVE,
                refrigerator = refrigerator
            )
        )

    // [내부 메서드] 만장일치를 받았는지 확인 (냉장고 내 관리자의 수 == 찬성한 인원 수)
    private fun isCompletedVote(vote: Vote) =
        getNumberOfStaff() == vote.voters.size.toLong()

    // [내부 메서드] 현재 Refrigerator 내 관리자(STAFF) 의 수
    private fun getNumberOfStaff() = memberRepository.countByRole(MemberRole.STAFF)

    /*
    [내부 메서드] 현재 진행 중인 Purchase (공동구매)를 리턴
        - status 가 ACTIVE (투표 진행중) 인 Purchase 확인
        - status 가 ACTIVE (투표 진행중) 인 Purchase 가 없다면, 새로운 Purchase 객체를 생성 후 리턴
        * TODO : 추후 조회 과정 리팩토링 필요
    */
    private fun getCurrentPurchase(refrigeratorId: Long) =
        purchaseRepository.findAll().firstOrNull { it.status == PurchaseStatus.ACTIVE }
            ?: makePurchase(getRefrigerator(refrigeratorId))

    private fun getVote(voteId: Long) =
        voteRepository.findByIdOrNull(voteId) ?: throw Exception("") // TODO

    private fun getMember(userId: Long) =
        memberRepository.findByUser(getUser(userId)) ?: throw Exception("") // TODO

    private fun getUser(userId: Long) =
        userRepository.findByIdOrNull(userId) ?: throw Exception("") // TODO

    private fun getRefrigerator(refrigeratorId: Long) =
        refrigeratorRepository.findByIdOrNull(refrigeratorId) ?: throw Exception("") // TODO
}