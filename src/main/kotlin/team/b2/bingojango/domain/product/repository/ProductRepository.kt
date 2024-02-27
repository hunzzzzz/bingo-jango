package team.b2.bingojango.domain.product.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.b2.bingojango.domain.food.model.Food
import team.b2.bingojango.domain.product.model.Product
import team.b2.bingojango.domain.refrigerator.model.Refrigerator

@Repository
interface ProductRepository : JpaRepository<Product, Long> {
    fun findByFoodAndRefrigerator(food: Food, refrigerator: Refrigerator): Product?
}