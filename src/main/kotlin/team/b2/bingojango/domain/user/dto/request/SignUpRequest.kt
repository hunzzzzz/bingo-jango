package team.b2.bingojango.domain.user.dto.request

import ValidationUtils
import jakarta.validation.constraints.NotBlank

data class SignUpRequest(
    @field:NotBlank val name: String,
    @field:NotBlank val nickname: String,
    @field:NotBlank val email: String,
    @field:NotBlank val phone: String,
    @field:NotBlank val password: String,
    @field:NotBlank val passwordConfirm: String
) {
    // 비밀번호 유효성 검사 메서드
    fun validatePassword() {
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

    // 필수 필드 및 형식 검사 메서드
    fun validateSignUpRequest(): ValidationResult {
        // 필수 필드 유효성 검사
        if (name.isNullOrBlank() || nickname.isNullOrBlank() || email.isNullOrBlank() || phone.isNullOrBlank() || password.isNullOrBlank()) {
            return ValidationResult(false, "모든 필수 필드를 입력해주세요.")
        }

        // 이메일 및 전화번호 형식 검사
        if (!ValidationUtils.isValidEmail(email)) {
            return ValidationResult(false, "올바른 이메일 형식이 아닙니다.")
        }
        if (!ValidationUtils.isValidPhoneNumber(phone)) {
            return ValidationResult(false, "올바른 전화번호 형식이 아닙니다.")
        }

        return ValidationResult(true, "유효성 검사를 통과했습니다.")
    }
}