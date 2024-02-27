package team.b2.bingojango.domain.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.b2.bingojango.domain.user.model.User

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(email:String) : User?
}