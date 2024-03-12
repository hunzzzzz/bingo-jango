package team.b2.bingojango.domain.chatting.handler

import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class SocketHandler(
    private val sessionList: ArrayList<WebSocketSession> = ArrayList()
) : TextWebSocketHandler() {

    // 클라이언트로부터 메시지 도착시 호출
    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        sessionList.forEach { webSocketSession ->
            if (webSocketSession.isOpen) {
                webSocketSession.sendMessage(TextMessage(message.payload))
            }
        }
    }

    // 클라이언트와 서버 연결시 호출
    override fun afterConnectionEstablished(session: WebSocketSession) {
        sessionList.add(session)
        println("새 클라이언트와 연결됨")
    }

    // 클라이언트와 서버 단절시 호출
    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessionList.remove(session)
        println("클라이언트와 연결 해제됨")
    }

}