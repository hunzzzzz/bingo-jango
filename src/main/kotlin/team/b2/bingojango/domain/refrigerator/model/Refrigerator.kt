package team.b2.bingojango.domain.refrigerator.model

import jakarta.persistence.*
import team.b2.bingojango.global.entity.BaseEntity

@Entity
@Table(name = "Refrigerator")
class Refrigerator(
    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    val status: RefrigeratorStatus
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refrigerator_id", nullable = false)
    val id: Long? = null
}