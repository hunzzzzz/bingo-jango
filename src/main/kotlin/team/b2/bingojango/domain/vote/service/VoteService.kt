package team.b2.bingojango.domain.vote.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.b2.bingojango.domain.member.model.MemberRole
import team.b2.bingojango.domain.member.repository.MemberRepository
import team.b2.bingojango.domain.purchase.model.PurchaseStatus
import team.b2.bingojango.domain.purchase.repository.PurchaseRepository
import team.b2.bingojango.domain.purchase_product.repository.PurchaseProductRepository
import team.b2.bingojango.domain.vote.dto.request.VoteRequest
import team.b2.bingojango.domain.vote.dto.response.VoteResponse
import team.b2.bingojango.domain.vote.repository.VoteRepository
import team.b2.bingojango.global.exception.cases.DuplicatedVoteException
import team.b2.bingojango.global.exception.cases.InvalidRoleException
import team.b2.bingojango.global.exception.cases.NoCurrentPurchaseException
import team.b2.bingojango.global.exception.cases.UnableToStartVoteException
import team.b2.bingojango.global.security.UserPrincipal
import team.b2.bingojango.global.util.EntityFinder

@Service
@Transactional
class VoteService(
    private val memberRepository: MemberRepository,
    private val purchaseRepository: PurchaseRepository,
    private val purchaseProductRepository: PurchaseProductRepository,
    private val voteRepository: VoteRepository,
    private val entityFinder: EntityFinder
) {
    // [API] 현재 공동구매 목록에 대한 투표 현황 조회
    fun showVote(refrigeratorId: Long, voteId: Long) =
        VoteResponse.from(
            vote = entityFinder.getVote(voteId),
            member = entityFinder.getMember(
                userId = getCurrentPurchase().proposedBy,
                refrigeratorId = refrigeratorId
            ),
            numberOfStaff = getNumberOfStaff()
        )

    /*
        [API] 현재 공동구매 목록에 대한 투표 시작
            - 검증 조건 1 : 공동구매를 신청한 회원 본인만 투표를 시작할 수 있음
            - 검증 조건 2 : 공동구매 목록에 물품이 존재하지 않는 경우, 투표를 시작할 수 없음
            - 검증 조건 3 : STAFF 의 수가 1명인 경우, 투표 과정을 생략하고 현재 Purchase 의 status 를 APPROVED 로 변경
     */
    fun startVote(userPrincipal: UserPrincipal, refrigeratorId: Long, voteRequest: VoteRequest) {
        if (getCurrentPurchase().proposedBy != userPrincipal.id) throw InvalidRoleException()
        else if (purchaseProductRepository.findAllByPurchase(getCurrentPurchase())
                .isEmpty()
        ) throw UnableToStartVoteException()
        else if (getNumberOfStaff() == 1L) getCurrentPurchase().updateStatus(PurchaseStatus.APPROVED)

        voteRepository.save(
            voteRequest.to(
                request = voteRequest,
                refrigerator = entityFinder.getRefrigerator(refrigeratorId),
                member = entityFinder.getMember(userPrincipal.id, refrigeratorId),
                purchase = getCurrentPurchase()
            )
        )
    }

    /*
        [API] 투표 실시
            - 검증 조건 1 : 관리자(STAFF)만 투표를 할 수 있음
            - 검증 조건 2 : 이미 투표한 경우, 투표 결과를 수정할 수 없음
            - 검증 조건 3-1 : 찬성에 투표한 경우, voters 에 해당 Member 객체를 add
            - 검증 조건 3-2 : 만장일치가 완성된 경우, 투표를 종료하고 현재 Purchase 의 status 를 APPROVED 로 변경
            - 검증 조건 4 : 반대에 투표한 경우, 투표를 종료하고 현재 Purchase 의 status 를 REJECTED 로 변경
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
                            getCurrentPurchase().updateStatus(PurchaseStatus.APPROVED)
                    } else
                        getCurrentPurchase().updateStatus(PurchaseStatus.REJECTED)
                }
            }

    // [내부 메서드] 현재 진행 중인(status 가 ACTIVE 한) Purchase 를 리턴 (없으면 예외 처리)
    private fun getCurrentPurchase() =
        purchaseRepository.findAll().firstOrNull { it.status == PurchaseStatus.ACTIVE }
            ?: throw NoCurrentPurchaseException()

    // [내부 메서드] 현재 Refrigerator 내 관리자(STAFF) 의 수
    private fun getNumberOfStaff() = memberRepository.countByRole(MemberRole.STAFF)
}