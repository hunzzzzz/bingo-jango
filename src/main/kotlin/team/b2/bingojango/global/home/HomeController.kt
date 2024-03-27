package team.b2.bingojango.global.home

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.view.RedirectView

@Controller
class HomeController {
    @GetMapping("/")
    fun home() = RedirectView().let {
        it.url = "http://3.37.41.30:9090/" // TODO : 너무 꼼수인 방법이라 ㅎㅎ... 추후 수정 예정
        it
    }
}