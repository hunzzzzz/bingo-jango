package team.b2.bingojango

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import team.b2.bingojango.domain.food.model.Food
import team.b2.bingojango.domain.food.model.FoodCategory
import team.b2.bingojango.domain.food.repository.FoodRepository
import team.b2.bingojango.domain.refrigerator.model.Refrigerator
import team.b2.bingojango.domain.refrigerator.repository.RefrigeratorRepository
import team.b2.bingojango.domain.user.model.User
import team.b2.bingojango.domain.user.model.UserStatus
import team.b2.bingojango.domain.user.repository.UserRepository
import java.time.ZonedDateTime

@SpringBootTest
class BingoJangoApplicationTests {
    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var foodRepository: FoodRepository

    @Autowired
    lateinit var refrigeratorRepository: RefrigeratorRepository

    @Test
    fun 유저임시등록() {
        userRepository.save(
            User(
                name = "허훈",
                nickname = "hun819",
                phone = "010-2939-8746",
                email = "hunzz.study@gmail.com",
                password = passwordEncoder.encode("1234"),
                status = UserStatus.NORMAL
            )
        )

        userRepository.save(
            User(
                name = "두부",
                nickname = "dubu",
                phone = "010-1234-5678",
                email = "doubleh819@naver.com",
                password = passwordEncoder.encode("1234"),
                status = UserStatus.NORMAL
            )
        )
    }

    @Test
    fun 냉장고임시등록() {
        refrigeratorRepository.save(
            Refrigerator(name = "임시 냉장고", password = "1234")
        )
    }

    @Test
    fun 음식임시등록() {
        foodRepository.save(
            Food(
                category = FoodCategory.FRUIT,
                name = "사과",
                expirationDate = ZonedDateTime.now().plusDays(7),
                refrigerator = refrigeratorRepository.findByIdOrNull(1) ?: throw Exception("")
            )
        )

        foodRepository.save(
            Food(
                category = FoodCategory.DRINK,
                name = "우유",
                expirationDate = ZonedDateTime.now().plusDays(3),
                refrigerator = refrigeratorRepository.findByIdOrNull(1) ?: throw Exception("")
            )
        )
    }

}