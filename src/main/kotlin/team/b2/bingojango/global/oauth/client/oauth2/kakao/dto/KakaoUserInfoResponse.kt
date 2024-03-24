package team.b2.bingojango.global.oauth.client.oauth2.kakao.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import team.b2.bingojango.global.oauth.client.oauth2.OAuth2LoginUserInfo
import team.b2.bingojango.global.oauth.domain.entity.OAuth2Provider

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
class KakaoUserInfoResponse(
    id: Long,
    properties: KakaoUserPropertiesResponse,
    kakaoAccount: KakaoUserAccountResponse, //카카오로부터 이렇게 응답이 오면
) : OAuth2LoginUserInfo(
    provider = OAuth2Provider.KAKAO,
    id = id.toString(),
    nickname = properties.nickname, //부모객체의 모습으로 초기화됨 (업캐스팅)
    email = kakaoAccount.email
) {
}