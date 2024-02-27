package team.b2.bingojango.domain.purchase.model

import jakarta.persistence.*
import team.b2.bingojango.domain.refrigerator.model.Refrigerator
import team.b2.bingojango.global.entity.BaseEntity

@Entity
@Table(name = "Purchase")
class Purchase(
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    var status: PurchaseStatus = PurchaseStatus.ACTIVE,

    @Column(name = "proposed_by", nullable = false)
    val proposedBy: Long,

    @ManyToOne
    @JoinColumn(name = "refrigerator_id")
    val refrigerator: Refrigerator?
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_id", nullable = false)
    val id: Long? = null

    fun updateStatus(status: PurchaseStatus) {
        this.status = status
    }
}