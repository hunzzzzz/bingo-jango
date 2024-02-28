package team.b2.bingojango.domain.user.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.b2.bingojango.domain.user.dto.request.EditRequest
import team.b2.bingojango.domain.user.dto.request.LoginRequest
import team.b2.bingojango.domain.user.dto.response.LoginResponse
import team.b2.bingojango.domain.user.dto.request.PasswordRequest
import team.b2.bingojango.domain.user.model.UserStatus
import team.b2.bingojango.domain.user.repository.UserRepository
import team.b2.bingojango.global.exception.cases.InvalidCredentialException
import team.b2.bingojango.global.exception.cases.ModelNotFoundException
import team.b2.bingojango.global.security.UserPrincipal
import team.b2.bingojango.global.security.jwt.JwtPlugin
import team.b2.bingojango.global.security.jwt.service.TokenStorageService
import java.time.LocalDateTime
import java.time.ZonedDateTime

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtPlugin: JwtPlugin,
    private val tokenStorageService: TokenStorageService
) {
    fun login(request: LoginRequest
    ): LoginResponse {

        //확인사항1: 이메일 존재 여부 확인
        val user = userRepository.findByEmail(request.email)
            ?: throw InvalidCredentialException("이메일 또는 비밀번호를 확인해주세요.")

        //확인사항2: 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(request.password, user.password))
            throw InvalidCredentialException("이메일 또는 비밀번호를 확인해주세요.")

        //토큰 생성
        val token = jwtPlugin.generateAccessToken(
            subject = user.id.toString(),
            email = user.email,
            role = user.role.name
        )

        //토큰 저장 (1시간 후 만료)
        tokenStorageService.storeToken(user.id!!, token, LocalDateTime.now().plusHours(1))

        return LoginResponse(token)
    }

    @Transactional
    fun logout(
        userPrincipal: UserPrincipal
    ){
        //토큰 무효화
        tokenStorageService.invalidateToken(userPrincipal.id)
    }

    // 유저 프로필 수정
    @Transactional
    fun updateUserProfile(request: EditRequest, userPrincipal: UserPrincipal){
        val user=getUserInfo(userPrincipal)
        if (userRepository.existsByNickname(request.nickname)) throw IllegalArgumentException ("존재하는 닉네임이에요.")
        if (userRepository.existsByEmail(request.email)) throw IllegalArgumentException ("존재하는 이메일이에요.")

        user.name= request.name
        user.nickname= request.nickname
        user.email= request.email
        user.phone= request.phone

        userRepository.save(user)
    }

    // 유저 비밀번호 수정
    @Transactional
    fun updateUserPassword(request: PasswordRequest, userPrincipal: UserPrincipal){
        val user=getUserInfo(userPrincipal)
        if (!passwordEncoder.matches(user.password,request.password)) throw IllegalArgumentException("기존의 비밀번호가 일치하지 않아요.")
        if (request.newPassword != request.reNewPassword) throw IllegalArgumentException("새로운 비밀번호과 비밀번호 확인이 일치하지 않아요.")

        user.password= passwordEncoder.encode(request.newPassword)

        userRepository.save(user)
    }

    // 유저 탈퇴
    @Transactional
    fun withdrawUser(request: PasswordRequest, userPrincipal: UserPrincipal){
        val user=getUserInfo(userPrincipal)
        if (!passwordEncoder.matches(user.password,request.password)) throw IllegalArgumentException("비밀번호가 일치하지 않아요.")

        user.status= UserStatus.WITHDRAWN

        userRepository.save(user)
    }

    //탈퇴한 유저 영구 삭제 (90일 유예), 매일 04시에 확인
    @Scheduled(cron = "0 0 4 * * *", zone = "Asia/Seoul")
    fun deleteWithdrawnUser(){
        val daysOver= ZonedDateTime.now().minusDays(90)
        val users= userRepository.findAllByStatus(UserStatus.WITHDRAWN)
            .filter { it.updatedAt.isAfter(daysOver) }
        if (users.isNotEmpty()){
            for (user in users){
                userRepository.delete(user)
            }
        }
    }

    private fun getUserInfo(userPrincipal: UserPrincipal)= userRepository.findByIdOrNull(userPrincipal.id) ?:throw ModelNotFoundException("id")

}