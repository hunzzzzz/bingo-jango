package team.b2.bingojango.domain.chatting.dto

data class ChatRequest(
    val chatRoomId: String,
    val content: String = "",
//    val status: ChatStatus,
)
