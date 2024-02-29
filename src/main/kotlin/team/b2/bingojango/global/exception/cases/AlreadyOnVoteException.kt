package team.b2.bingojango.global.exception.cases

class AlreadyOnVoteException(value: String) : RuntimeException("이미 구매 여부에 대한 투표가 시작되어 식품을 ${value}할 수 없습니다.")