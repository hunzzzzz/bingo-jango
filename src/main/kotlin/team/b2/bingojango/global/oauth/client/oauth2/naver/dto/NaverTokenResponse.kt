package team.b2.bingojango.global.oauth.client.oauth2.naver.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class NaverTokenResponse(
    val accessToken: String
)