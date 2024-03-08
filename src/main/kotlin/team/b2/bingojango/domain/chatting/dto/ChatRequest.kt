package team.b2.bingojango.domain.chatting.dto

import team.b2.bingojango.domain.chatting.model.ChatStatus

data class ChatRequest(
    val chatRoomId: String,
//    val sender: String,
    val content: String = "",
//    val status: ChatStatus,
)
