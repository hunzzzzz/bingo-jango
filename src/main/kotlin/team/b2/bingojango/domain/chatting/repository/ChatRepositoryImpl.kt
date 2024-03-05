package team.b2.bingojango.domain.chatting.repository

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import team.b2.bingojango.domain.chatting.model.Chat
import team.b2.bingojango.domain.chatting.model.QChat
import team.b2.bingojango.global.querydsl.QueryDslSupport

@Repository
class ChatRepositoryImpl : QueryDslSupport(), CustomChatRepository {
    private val chat = QChat.chat

    override fun findFirstPage(chatRoomId: Long, pageable: Pageable): List<Chat> {
        val query = queryFactory
            .selectFrom(chat)
            .where(chat.chatRoom.id.eq(chatRoomId))
            .orderBy(chat.id.asc())
            .limit(pageable.pageSize.toLong())
        return query.fetch()
    }

    override fun findNextPage(chatRoomId: Long, cursor: Long, pageable: Pageable): List<Chat> {
        val query = queryFactory
            .selectFrom(chat)
            .where(
                chat.chatRoom.id.eq(chatRoomId),
                chat.id.lt(cursor)
            )
            .orderBy(chat.id.asc())
            .limit(pageable.pageSize.toLong())
        return query.fetch()
    }
}