package team.b2.bingojango.domain.chatting.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.b2.bingojango.domain.chatting.model.Chat

@Repository
interface ChatRepository : JpaRepository<Chat, Long>, CustomChatRepository {

    fun findAllByChatRoomId(chatRoomId: Long): List<Chat>
}