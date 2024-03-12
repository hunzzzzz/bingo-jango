package team.b2.bingojango.domain.chatting.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.b2.bingojango.domain.chatting.model.ChatRoom
import team.b2.bingojango.domain.refrigerator.model.Refrigerator
import team.b2.bingojango.domain.user.model.User

@Repository
interface ChatRoomRepository : JpaRepository<ChatRoom, Long> {

    fun findByRefrigerator(refrigerator: Refrigerator): ChatRoom?

    // 테스트용, 추후 삭제 필요
    fun findAllByRefrigerator(refrigerator: Refrigerator): List<ChatRoom>
}