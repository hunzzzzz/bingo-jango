package team.b2.bingojango.domain.vote.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.b2.bingojango.domain.vote.model.Vote

@Repository
interface VoteRepository : JpaRepository<Vote, Long> {
}