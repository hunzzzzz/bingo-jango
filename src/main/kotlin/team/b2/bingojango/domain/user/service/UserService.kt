package team.b2.bingojango.domain.user.service

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import team.b2.bingojango.domain.mail.service.MailService
import team.b2.bingojango.domain.user.dto.request.*
import team.b2.bingojango.domain.user.dto.response.FindEmailResponse
import team.b2.bingojango.domain.user.dto.response.LoginResponse
import team.b2.bingojango.domain.user.dto.response.UploadImageResponse
import team.b2.bingojango.domain.user.dto.response.UserResponse
import team.b2.bingojango.domain.user.model.User
import team.b2.bingojango.domain.user.model.User.Companion.toResponse
import team.b2.bingojango.domain.user.model.UserStatus
import team.b2.bingojango.domain.user.repository.UserRepository
import team.b2.bingojango.global.aws.S3Service
import team.b2.bingojango.global.exception.cases.InvalidCredentialException
import team.b2.bingojango.global.exception.cases.ModelNotFoundException
import team.b2.bingojango.global.security.TokenGenerator
import team.b2.bingojango.global.security.jwt.JwtPlugin
import team.b2.bingojango.global.security.util.CookieUtil
import team.b2.bingojango.global.security.util.UserPrincipal
import team.b2.bingojango.global.util.EntityFinder
import java.time.ZonedDateTime

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtPlugin: JwtPlugin,
    private val mailService: MailService,
    private val tokenGenerator: TokenGenerator,
    private val s3Service: S3Service,
    private val entityFinder: EntityFinder,
    @Value("\${app.cookie.expiry}") private val cookieExpirationTime: Int,
) {
    /*
       [API] 로그인
          - 로그인 정보 검증 (이메일, 비밀번호)
          - 유저 검색
          - 엑세스 토큰 및 리프레시 토큰 생성
          - 리프레시 토큰 생성 후 쿠키와 DB에 저장
          - 엑세스 토큰 및 리프레시 토큰 반환
    */
    fun login(request: LoginRequest, response: HttpServletResponse): LoginResponse {
        checkLoginInfo(request.email, request.password)
        val user = entityFinder.getUserByEmail(request.email)
        val accessToken = jwtPlugin.generateAccessToken(user.id.toString(), user.email, user.role.name)
        val refreshToken = jwtPlugin.generateRefreshToken(user.id.toString(), user.email, user.role.name)
        jwtPlugin.storeToken(user, refreshToken)
        CookieUtil.addCookie(response, "refreshToken", refreshToken, cookieExpirationTime)
        return LoginResponse(accessToken, refreshToken)
    }

    /*
       [API] 로그아웃
          - DB 에서 refresh Token 을 삭제
          - 쿠키 에서 refreshToken 을 삭제
    */
    @Transactional
    fun logout(
        userPrincipal: UserPrincipal,
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {
        jwtPlugin.deleteToken(userPrincipal)
        CookieUtil.deleteCookie(request, response, "refreshToken")
    }

    /*
       [API] 회원가입
          - 이메일 중복 검사
          - 닉네임 중복 검사
          - 비밀번호 확인과의 일치 확인
    */
    fun signUp(signUpRequest: SignUpRequest): UserResponse {
        if (userRepository.existsByEmail(signUpRequest.email)) throw IllegalArgumentException("이미 등록된 이메일 주소입니다.")
        if (userRepository.existsByNickname(signUpRequest.nickname)) throw IllegalArgumentException("이미 사용 중인 닉네임입니다.")
        if (signUpRequest.password != signUpRequest.passwordConfirm) throw IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.")
        return userRepository.save(User(
                name = signUpRequest.name,
                nickname = signUpRequest.nickname,
                email = signUpRequest.email,
                phone = signUpRequest.phone,
                password = passwordEncoder.encode(signUpRequest.password),
                provider = null,
                providerId = null,
                image = null
            )).toResponse()
    }

    /*
       [API] 이메일 찾기
          - 실명과 폰 번호로 사용자를 찾습니다.
          - 사용자의 이메일을 가져와 마스킹하여 반환합니다.
    */
    fun findEmail(request: FindEmailRequest): FindEmailResponse {
        val user = entityFinder.getUserByNameAndPhone(request.name, request.phone)
        val maskedEmail = maskEmail(user.email)
        return FindEmailResponse(maskedEmail)
    }

    /*
       [API] 비밀번호 찾기
          - 임시 비밀번호 생성
          - 비밀번호 업데이트
          - 사용자 정보 저장 (비밀번호 업데이트)
          - 이메일로 임시 비밀번호 전송
    */
    fun findPassword(request: FindPasswordRequest) {
        val user = userRepository.findByEmail(request.email) ?: throw ModelNotFoundException("유저")
        val newPassword = tokenGenerator.generateToken()
        user.password = newPassword
        userRepository.save(user)
        val subject = "임시 비밀번호 발급 안내"
        val body = "안녕하세요, ${user.email}님.\n\n새로운 임시 비밀번호는 다음과 같습니다: $newPassword\n\n로그인 후 비밀번호를 변경해주세요."
        mailService.sendEmail(user.email, subject, body)
    }

    /*
       [API] 본인 프로필 조회
    */
    fun getProfileByMe(userPrincipal: UserPrincipal): UserResponse {
        val user = entityFinder.getUser(userPrincipal.id)
        return user.toResponse()
    }

    /*
       [API] 타인 프로필 조회
    */
    fun getProfileById(userId: Long): UserResponse {
        val user = entityFinder.getUser(userId)
        return user.toResponse()
    }

    /*
       [API]프로필 수정
          - 닉네임 중복 불가
          - 이메일 중복 불가
    */
    @Transactional
    fun updateProfile(userPrincipal: UserPrincipal, request: ProfileUpdateRequest) {
        val user = entityFinder.getUser(userPrincipal.id)
        if (userRepository.existsByNickname(request.nickname) && request.nickname != user.nickname)
            throw IllegalArgumentException("존재하는 닉네임이에요.")
        if (userRepository.existsByEmail(request.email) && request.email != user.email)
            throw IllegalArgumentException("존재하는 이메일이에요.")

        user.updateProfileSupport(request)
        userRepository.save(user)
    }

    /*
        [API] 유저 비밀번호 수정
            - 기존의 비밀번호 확인
            - 새 비밀번호 및 새 비밀번호 재작성 일치 확인
    */
    @Transactional
    fun updatePassword(userPrincipal: UserPrincipal, request: PasswordRequest) {
        val user = entityFinder.getUser(userPrincipal.id)
        if (!passwordEncoder.matches(user.password, request.password)
        ) throw IllegalArgumentException("기존의 비밀번호가 일치하지 않아요.")
        if (request.newPassword != request.reNewPassword) throw IllegalArgumentException("새로운 비밀번호과 비밀번호 확인이 일치하지 않아요.")

        user.password = passwordEncoder.encode(request.newPassword)
    }

    /*
        [API] 프로필 이미지 업로드
    */
    @Transactional
    fun uploadImage(multipartFile: MultipartFile, userPrincipal: UserPrincipal): UploadImageResponse {
        val user = userRepository.findByIdOrNull(userPrincipal.id) ?: throw ModelNotFoundException("User")
        val url = s3Service.upload(multipartFile)
        user.image = url
        userRepository.save(user)
        return UploadImageResponse(url)
    }

    /*
        [API] 유저 탈퇴
            - Soft Delete (논리적삭제)
            - 탈퇴한 유저 영구 삭제 (90일 유예), 매일 04시에 확인 (스케줄링)
    */
    @Transactional
    fun withdrawUser(request: WithdrawRequest, userPrincipal: UserPrincipal) {
        val user = entityFinder.getUser(userPrincipal.id)
        if (!passwordEncoder.matches(user.password, request.password)) throw IllegalArgumentException("비밀번호가 일치하지 않아요.")

        user.status = UserStatus.WITHDRAWN

        userRepository.save(user)
    }

    @Scheduled(cron = "0 0 4 * * *", zone = "Asia/Seoul")
    fun deleteWithdrawnUser() {
        val daysOver = ZonedDateTime.now().minusDays(90)
        val users = userRepository.findAllByStatus(UserStatus.WITHDRAWN)
            .filter { it.updatedAt.isAfter(daysOver) }
        if (users.isNotEmpty()) {
            for (user in users) {
                userRepository.delete(user)
            }
        }
    }


    // [내부 메서드] 로그인 정보를 확인
    private fun checkLoginInfo(email: String, password: String) {
        InvalidCredentialException("이메일 또는 비밀번호를 확인해주세요.")
            .let {
                (userRepository.findByEmail(email) ?: throw it)
                    .let { user -> if (!passwordEncoder.matches(password, user.password)) throw it }
            }
    }

    // [내부 메서드] 이메일 마스킹 함수
    private fun maskEmail(email: String): String {
        val atIndex = email.indexOf('@')
        if (atIndex == -1) {
            // @ 문자가 없는 이메일 형식일 경우 그대로 반환
            return email
        }

        val username = email.substring(0, atIndex)
        val domain = email.substring(atIndex)
        val maskedUsername = maskString(username, 5) // 아이디 부분을 5글자로 숨김 처리

        return "$maskedUsername$domain"
    }

    // [내부 메서드] 문자열 마스킹 함수
    fun maskString(str: String, visibleChars: Int): String {
        val maskedLength = str.length - visibleChars
        if (maskedLength <= 0) {
            // 숨길 문자가 없는 경우 그대로 반환
            return str
        }

        val maskedChars = "*".repeat(maskedLength)
        val visibleCharsStr = str.substring(0, visibleChars)

        return "$visibleCharsStr$maskedChars"
    }
}