object MaskUtils {

    // 이메일 마스킹 함수
    fun maskEmail(email: String): String {
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

    // 문자열 마스킹 함수
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