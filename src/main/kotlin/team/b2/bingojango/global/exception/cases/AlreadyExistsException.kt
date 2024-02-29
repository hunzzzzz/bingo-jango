package team.b2.bingojango.global.exception.cases

class AlreadyExistsException(val name: String) : RuntimeException("이미 존재하는 $name 입니다.")