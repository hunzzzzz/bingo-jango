package team.b2.bingojango.domain.chatting.repository

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import team.b2.bingojango.domain.chatting.model.Chat

@Repository
interface CustomChatRepository {
    fun findFirstPage(chatRoomId: Long, pageable: Pageable): List<Chat>

    fun findNextPage(chatRoomId: Long, cursor: Long, pageable: Pageable): List<Chat>
}