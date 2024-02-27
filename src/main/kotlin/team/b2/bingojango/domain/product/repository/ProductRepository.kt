package team.b2.bingojango.domain.product.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.b2.bingojango.domain.product.model.Product

@Repository
interface ProductRepository : JpaRepository<Product, Long> {
}