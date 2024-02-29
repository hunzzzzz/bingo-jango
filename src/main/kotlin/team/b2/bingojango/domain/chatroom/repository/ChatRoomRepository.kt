package team.b2.bingojango.domain.chatroom.repository

import org.springframework.data.jpa.repository.JpaRepository
import team.b2.bingojango.domain.chatroom.model.ChatRoom
import team.b2.bingojango.domain.refrigerator.model.Refrigerator

interface ChatRoomRepository:JpaRepository<ChatRoom, Long> {

    fun findByRefrigerator(refrigerator: Refrigerator) : ChatRoom?
}