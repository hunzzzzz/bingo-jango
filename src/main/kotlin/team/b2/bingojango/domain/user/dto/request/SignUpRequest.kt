package team.b2.bingojango.domain.user.dto.request

data class SignUpRequest(
    val name: String,
    val nickname: String,
    val email: String,
    val phone: String,
    val password: String,
    val passwordConfirm: String
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
}