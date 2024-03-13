package team.b2.bingojango.global.oauth.client.oauth2.google.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import team.b2.bingojango.global.oauth.client.oauth2.OAuth2LoginUserInfo
import team.b2.bingojango.global.oauth.domain.entity.OAuth2Provider

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
class GoogleLoginUserInfoResponse(
    id: String,
    name: String,
    email: String
) : OAuth2LoginUserInfo(
    provider = OAuth2Provider.GOOGLE,
    id = id,
    nickname = name,
    email = email
)