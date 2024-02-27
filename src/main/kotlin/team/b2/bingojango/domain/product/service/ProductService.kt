package team.b2.bingojango.domain.product.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.b2.bingojango.domain.food.model.Food
import team.b2.bingojango.domain.product.model.Product
import team.b2.bingojango.domain.product.repository.ProductRepository
import team.b2.bingojango.domain.refrigerator.model.Refrigerator

@Service
@Transactional
class ProductService(
    private val productRepository: ProductRepository
) {
    // [내부 메서드] Product 객체 생성 (FoodService > getProduct 에서만 사용되는 메서드)
    fun addProduct(food: Food, refrigerator: Refrigerator) =
        productRepository.save(
            Product(food = food, refrigerator = refrigerator)
        )
}