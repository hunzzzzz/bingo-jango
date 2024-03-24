package team.b2.bingojango.global.exception.cases

data class InvalidCredentialException(
    override val message: String? = "The credential is invalid"
) : RuntimeException(message)