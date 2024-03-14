package team.b2.bingojango.domain.refrigerator.model

import jakarta.persistence.*
import team.b2.bingojango.domain.refrigerator.dto.request.AddRefrigeratorRequest
import team.b2.bingojango.domain.refrigerator.dto.response.RefrigeratorResponse
import team.b2.bingojango.global.entity.BaseEntity

@Entity
@Table(name = "Refrigerator")
class Refrigerator(
    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "password", nullable = false)
    val password: String,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    val status: RefrigeratorStatus = RefrigeratorStatus.NORMAL
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refrigerator_id", nullable = false)
    val id: Long? = null

    companion object {
        fun toEntity(request: AddRefrigeratorRequest): Refrigerator {
            return Refrigerator(
                name = request.name,
                password = request.password,
                status = RefrigeratorStatus.NORMAL
            )
        }
    }

    fun toResponse(): RefrigeratorResponse {
        return RefrigeratorResponse(
            id = id!!,
            name = name
        )
    }
}