package team.b2.bingojango.domain.refrigerator.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.b2.bingojango.domain.refrigerator.model.Refrigerator

@Repository
interface RefrigeratorRepository : JpaRepository<Refrigerator, Long> {
}