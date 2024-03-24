package team.b2.bingojango.domain.mail.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.b2.bingojango.domain.mail.dto.MailResponse
import team.b2.bingojango.domain.mail.service.MailService

@Tag(name = "mail", description = "메일")
@RestController
class MailController(
    private val mailService: MailService
) {
    @Operation(summary = "냉장고 초대코드 발송하기")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/refrigerator/{refrigeratorId}/member")
    fun sendInvitationCode(
        @PathVariable refrigeratorId: Long,
        @RequestParam email: String
    ): ResponseEntity<MailResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(mailService.sendInvitationCode(refrigeratorId, email))
    }
}