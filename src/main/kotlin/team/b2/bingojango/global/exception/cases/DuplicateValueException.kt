package team.b2.bingojango.global.exception.cases

data class DuplicateValueException(
    override val message: String
) : RuntimeException(message)