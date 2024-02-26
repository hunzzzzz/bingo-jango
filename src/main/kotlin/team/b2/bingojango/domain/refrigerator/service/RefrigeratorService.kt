package team.b2.bingojango.domain.refrigerator.service

import org.springframework.stereotype.Service
import team.b2.bingojango.domain.refrigerator.repository.RefrigeratorRepository

@Service
class RefrigeratorService(
    private val refrigeratorRepository: RefrigeratorRepository
) {
}