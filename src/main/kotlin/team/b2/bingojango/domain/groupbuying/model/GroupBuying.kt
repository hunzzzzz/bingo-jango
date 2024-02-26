package team.b2.bingojango.domain.groupbuying.model

import jakarta.persistence.*
import team.b2.bingojango.domain.refrigerator.model.Refrigerator
import team.b2.bingojango.global.entity.BaseEntity

@Entity
@Table(name = "Group_Buying")
class GroupBuying(
    @ManyToOne
    val refrigerator: Refrigerator?
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}