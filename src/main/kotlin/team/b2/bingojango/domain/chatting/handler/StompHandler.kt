package team.b2.bingojango.domain.chatting.handler

import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import team.b2.bingojango.global.security.jwt.JwtAuthenticationFilter
import team.b2.bingojango.global.security.jwt.JwtAuthenticationToken
import team.b2.bingojango.global.security.jwt.JwtPlugin
import team.b2.bingojango.global.security.util.UserPrincipal

@Component
class StompHandler(
    private val jwtPlugin: JwtPlugin,
):ChannelInterceptor {
    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
        val accessor = StompHeaderAccessor.wrap(message)

//        if (accessor.command == StompCommand.CONNECT) {
            val token = accessor.getFirstNativeHeader("Authorization") ?: throw AccessDeniedException("")
//            if (!jwtPlugin.validateToken(token).isSuccess) throw AccessDeniedException("")
            jwtPlugin.validateToken(token)
                .onSuccess {
                    val userId = it.payload.subject.toLong()
                    val role = it.payload.get("role", String::class.java)
                    val email = it.payload.get("email", String::class.java)

                    val principal = UserPrincipal(
                        id = userId,
                        email = email,
                        roles = setOf(role)
                    )

                    val authentication = JwtAuthenticationToken(
                        principal = principal,
                        details = null
                    )
                    accessor.user = authentication

                }.onFailure {
                    throw AccessDeniedException("invalid token")
                }.getOrThrow()

//        }
        return message
    }
}