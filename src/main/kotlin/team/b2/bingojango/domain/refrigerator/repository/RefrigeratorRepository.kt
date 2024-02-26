package team.b2.bingojango.domain.refrigerator.repository

import org.springframework.data.jpa.repository.JpaRepository
import team.b2.bingojango.domain.refrigerator.model.Refrigerator

interface RefrigeratorRepository : JpaRepository<Refrigerator, Long> {
}