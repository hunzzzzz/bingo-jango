package team.b2.bingojango.global.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import team.b2.bingojango.global.exception.cases.ModelNotFoundException

@RestControllerAdvice
class GlobalExceptionHandler {

    // 특정 엔터티 조회 실패
    @ExceptionHandler(ModelNotFoundException::class)
    fun handleModelNotFoundException(e: ModelNotFoundException)
            : ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse(e.message))
    }

    // 잘못된 요청
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleModelNotFoundException(e: IllegalArgumentException)
            : ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(e.message))
    }

}