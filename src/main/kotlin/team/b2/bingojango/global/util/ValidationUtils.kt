object ValidationUtils {

    // 이메일 형식 검사 함수
    fun isValidEmail(email: String): Boolean {
        val emailPattern = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}\$")
        return emailPattern.matches(email)
    }

    // 전화번호 형식 검사 함수
    fun isValidPhoneNumber(phone: String): Boolean {
        val phonePattern = Regex("^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}\$")
        return phonePattern.matches(phone)
    }
}