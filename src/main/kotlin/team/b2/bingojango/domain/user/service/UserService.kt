package team.b2.bingojango.domain.user.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import team.b2.bingojango.domain.user.dto.LoginRequest
import team.b2.bingojango.domain.user.dto.LoginResponse
import team.b2.bingojango.domain.user.dto.SignUpRequest
import team.b2.bingojango.domain.user.dto.SignUpResponse
import team.b2.bingojango.domain.user.model.User
import team.b2.bingojango.domain.user.repository.UserRepository
import team.b2.bingojango.global.exception.InvalidCredentialException
import team.b2.bingojango.global.security.jwt.JwtPlugin
import java.time.LocalDateTime

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtPlugin: JwtPlugin,
    ) {

    fun login(request: LoginRequest): LoginResponse {
        // 유효성 검사
        validatePassword(request.password, request.passwordConfirm)

        // 확인사항1: 이메일 존재 여부 확인
        val user = userRepository.findByEmail(request.email)
            ?: throw InvalidCredentialException("이메일 또는 비밀번호를 확인해주세요.")

        // 확인사항2: 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(request.password, user.password))
            throw InvalidCredentialException("이메일 또는 비밀번호를 확인해주세요.")

        return LoginResponse(
            accessToken = jwtPlugin.generateAccessToken(
                subject = user.id.toString(),
                email = user.email,
                role = user.role.name
            )
        )
    }

    fun signUp(signUpRequest: SignUpRequest): SignUpResponse {
        try {
            // 회원가입 요청에서 받은 정보를 사용하여 회원가입 처리를 수행
            // 회원가입이 성공적으로 완료되었다고 가정
            val createdUserId = create(signUpRequest)

            //회원가입이 성공적으로 완료된 경우
            val success = true
            val message = "회원가입이 성공적으로 완료되었습니다."

            // 회원가입 요청에서 받은 정보를 사용하여 SignUpResponse 객체를 생성
            val signUpResponse = SignUpResponse(
                role = signUpRequest.role.toString(),
                name = signUpRequest.name,
                nickname = signUpRequest.nickname,
                email = signUpRequest.email,
                phone = signUpRequest.phone,
                status = signUpRequest.status.toString(),
                createdAt = LocalDateTime.now(), // 현재 시각을 생성일로 설정하거나, DB에 저장된 시간을 사용할 수 있음
                updatedAt = LocalDateTime.now(), // 생성 시각과 동일하게 설정하거나, 회원 정보 수정 시각을 사용할 수 있음
                id = createdUserId, // 실제 생성된 회원의 id 값
                success = success,
                message = message
            )
            return signUpResponse
        } catch (e: Exception) {
            // 회원가입이 실패한 경우
            val success = false
            val message = "회원가입에 실패하였습니다. 잠시 후 다시 시도해주세요."

            val signUpResponse = SignUpResponse(
                role = signUpRequest.role.toString(),
                name = signUpRequest.name,
                nickname = signUpRequest.nickname,
                email = signUpRequest.email,
                phone = signUpRequest.phone,
                status = signUpRequest.status.toString(),
                createdAt = LocalDateTime.now(), // 현재 시각을 생성일로 설정하거나, DB에 저장된 시간을 사용할 수 있음
                updatedAt = LocalDateTime.now(), // 생성 시각과 동일하게 설정하거나, 회원 정보 수정 시각을 사용할 수 있음
                id = 0,
                success = success,
                message = message
            )

            return signUpResponse
        }
    }

    // 비밀번호 유효성 검사 메서드 추가
    private fun validatePassword(password: String, passwordConfirm: String) {
        // 비밀번호 길이 확인
        if (password.length !in 8..16) {
            throw IllegalArgumentException("비밀번호는 8~16자 이어야 합니다.")
        }

        // 비밀번호 패턴 확인
        val passwordPattern = Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$")
        if (!password.matches(passwordPattern)) {
            throw IllegalArgumentException("비밀번호는 알파벳 대소문자, 숫자, 특수문자를 포함해야 합니다.")
        }

        // 비밀번호 확인과의 일치 확인
        if (password != passwordConfirm) {
            throw IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.")
        }
    }
    fun create(signUpRequest: SignUpRequest): Long {
        //회원가입 요청에서 받은 정보로 User 객체 생성
        val newUser = User(
            role = signUpRequest.role,
            name = signUpRequest.name,
            nickname = signUpRequest.nickname,
            email = signUpRequest.email,
            phone = signUpRequest.phone,
            password = passwordEncoder.encode(signUpRequest.password),
            status = signUpRequest.status
        )

        // 사용자 저장
        val savedUser = userRepository.save(newUser)

// 저장된 사용자의 ID 반환
        return savedUser.id ?: throw IllegalStateException("Failed to create user")
    }
}