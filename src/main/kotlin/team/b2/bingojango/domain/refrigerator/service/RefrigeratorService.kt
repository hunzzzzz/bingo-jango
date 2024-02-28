package team.b2.bingojango.domain.refrigerator.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.b2.bingojango.domain.member.model.Member
import team.b2.bingojango.domain.member.repository.MemberRepository
import team.b2.bingojango.domain.refrigerator.dto.RefrigeratorRequest
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
    //냉장고 목록 조회
    fun getRefrigerator(userPrincipal: UserPrincipal): List<RefrigeratorResponse> {
        val member = memberRepository.findAll().filter{ it.id == userPrincipal.id }
        return member.map{ it.refrigerator.toResponse() }
    }

    //신규 냉장고 생성
    @Transactional
    fun addRefrigerator(userPrincipal: UserPrincipal, request: RefrigeratorRequest): RefrigeratorResponse {
        val user = userRepository.findByIdOrNull(userPrincipal.id) ?: throw ModelNotFoundException("User")
        val refrigerator = refrigeratorRepository.save(Refrigerator.toEntity(request))
        val member = memberRepository.save(Member.toEntity(user,refrigerator))
        return refrigerator.toResponse()
    }

    //기존 냉장고 참여
    @Transactional
    fun joinRefrigerator(userPrincipal: UserPrincipal, request: RefrigeratorRequest): RefrigeratorResponse {
        val user = userRepository.findByIdOrNull(userPrincipal.id) ?: throw ModelNotFoundException("User")

        //확인사항1: 냉장고 존재 유무
        val refrigerator = refrigeratorRepository.findByName(request.name) ?: throw ModelNotFoundException("Refrigerator")

        //확인사항2: 비밀번호 일치 여부
        if (refrigerator.password == request.password)
            memberRepository.save(Member.toEntity(user,refrigerator))
            else throw IllegalArgumentException("냉장고의 비밀번호가 일치하지 않습니다.")

        return refrigerator.toResponse()
    }