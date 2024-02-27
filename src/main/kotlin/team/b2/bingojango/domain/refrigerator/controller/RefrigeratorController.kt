package team.b2.bingojango.domain.refrigerator.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import team.b2.bingojango.domain.refrigerator.dto.AddRefrigeratorRequest
import team.b2.bingojango.domain.refrigerator.dto.RefrigeratorResponse
import team.b2.bingojango.domain.refrigerator.service.RefrigeratorService
import team.b2.bingojango.global.security.UserPrincipal

@RestController
@RequestMapping("/api/v1/refrigerator")
class RefrigeratorController(
    private val refrigeratorService: RefrigeratorService
) {
    @Operation(summary = "냉장고 목록 조회")
    @GetMapping
    fun getRefrigerator(
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<List<RefrigeratorResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(refrigeratorService.getRefrigerator(userPrincipal))
    }

    @Operation(summary = "냉장고 생성")
    @PutMapping
    fun addRefrigerator(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestBody request: AddRefrigeratorRequest
    ): ResponseEntity<RefrigeratorResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(refrigeratorService.addRefrigerator(userPrincipal, request))
    }
}