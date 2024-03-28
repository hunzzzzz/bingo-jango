package team.b2.bingojango.global.home

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.view.RedirectView

@Controller
@PropertySource("classpath:application.yml")
class HomeController(
    @Value("\${root.front}") private val frontRootURI: String,
) {
    @GetMapping("/")
    fun home() = RedirectView().let {
        it.url = frontRootURI // TODO : 추후 수정 예정
        it
    }
}