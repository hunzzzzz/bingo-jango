package team.b2.bingojango.domain.image

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import team.b2.bingojango.global.aws.S3Service

@Tag(name = "image", description = "이미지")
@RequestMapping("/api/images")
@RestController
class ImageController(
        private val s3Service: S3Service
) {
    @Operation(summary = "이미지 업로드")
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun uploadImage(@RequestParam("image") multipartFile: MultipartFile): ResponseEntity<UploadImageResponse> {
        return ResponseEntity
                .ok(UploadImageResponse(url=s3Service.upload(multipartFile)))
    }
}