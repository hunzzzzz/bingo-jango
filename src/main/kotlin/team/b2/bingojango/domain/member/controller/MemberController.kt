package team.b2.bingojango.domain.member.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.b2.bingojango.domain.member.service.MemberService

@RestController
@RequestMapping("/")
class MemberController(
    private val memberService: MemberService
) {
}