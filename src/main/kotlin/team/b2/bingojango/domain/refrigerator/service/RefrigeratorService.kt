package team.b2.bingojango.domain.refrigerator.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.b2.bingojango.domain.member.model.Member
import team.b2.bingojango.domain.member.repository.MemberRepository
import team.b2.bingojango.domain.refrigerator.dto.AddRefrigeratorRequest
import team.b2.bingojango.domain.refrigerator.dto.RefrigeratorResponse
import team.b2.bingojango.domain.refrigerator.model.Refrigerator
import team.b2.bingojango.domain.refrigerator.repository.RefrigeratorRepository
import team.b2.bingojango.domain.user.repository.UserRepository
import team.b2.bingojango.global.exception.cases.ModelNotFoundException
import team.b2.bingojango.global.security.UserPrincipal

@Service
class RefrigeratorService(
    private val refrigeratorRepository: RefrigeratorRepository,
    private val memberRepository: MemberRepository,
    private val userRepository: UserRepository,
) {
    fun getRefrigerator(userPrincipal: UserPrincipal): List<RefrigeratorResponse> {
        val member = memberRepository.findAll().filter{ it.id == userPrincipal.id }
        return member.map{ it.refrigerator.toResponse() }
    }

    @Transactional
    fun addRefrigerator(userPrincipal: UserPrincipal, request: AddRefrigeratorRequest): RefrigeratorResponse {
        val user = userRepository.findByIdOrNull(userPrincipal.id) ?: throw ModelNotFoundException("User")
        val refrigerator = refrigeratorRepository.save(Refrigerator.toEntity(request))
        val member = memberRepository.save(Member.toEntity(user,refrigerator))
        return refrigerator.toResponse()
    }
}