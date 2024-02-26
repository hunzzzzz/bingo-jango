package team.b2.bingojango.domain.food.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.b2.bingojango.domain.food.service.FoodService

@RestController
@RequestMapping("/")
class FoodController(
    private val foodService: FoodService
) {
}