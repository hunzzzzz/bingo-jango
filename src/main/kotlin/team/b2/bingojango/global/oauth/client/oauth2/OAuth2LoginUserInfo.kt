package team.b2.bingojango.global.oauth.client.oauth2

import team.b2.bingojango.global.oauth.domain.entity.OAuth2Provider

//provider마다 유저정보에 대한 응답이 다르게 올 수 있기 때문에 open하여 부모객체를 만든다.
open class OAuth2LoginUserInfo (
    val provider: OAuth2Provider,
    val id: String,
    val nickname: String,
    val email: String,
)