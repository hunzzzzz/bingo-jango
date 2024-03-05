package team.b2.bingojango.domain.chatting.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.b2.bingojango.domain.chatting.dto.ChatRequest
import team.b2.bingojango.domain.chatting.dto.ChatResponse
import team.b2.bingojango.domain.chatting.service.ChatRoomService
import team.b2.bingojango.domain.chatting.service.ChatService
import team.b2.bingojango.global.security.UserPrincipal

@RestController
//@RequestMapping("/api/v1/refrigerator/{refrigeratorId}")
class ChatController(
    private val chatService: ChatService,
    private val chatRoomService: ChatRoomService,
) {

    //웹소켓 작동 실험용
//    @MessageMapping("/chat.sendMessage")
//    @SendTo("/topic/public")
//    fun sendMessage(@Payload chatResponse: ChatResponse): ChatResponse{
//        return chatResponse
//    }

    // 채팅 전송
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    fun sendMessage(
        @Payload chatRequest: ChatRequest,
        @DestinationVariable chatRoomId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
    ): ChatResponse {
        return chatService.sendMessage(userPrincipal, chatRoomId, chatRequest)
    }

    // 채팅방 멤버 확인
    @GetMapping("/v1/{chatRoomId}/member")
    fun getChatRoomMember(
        @PathVariable chatRoomId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
    ): ResponseEntity<List<String>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(chatRoomService.getChatRoomMember(chatRoomId, userPrincipal))
    }

    // 채팅 목록 가져오기 v1 싹 다 가져옴
    @GetMapping("/v1/{chatRoomId}")
    fun getAllChat(
        @PathVariable chatRoomId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
    ): ResponseEntity<List<ChatResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(chatService.getAllChat(userPrincipal, chatRoomId))
    }

    // 채팅 목록 가져오기 v2 커서 페이지네이션 적용, cursor는 불러온 메시지 중 가장 첫번째 id값
    @GetMapping("/v2/{chatRoomId}")
    fun getAllChat2(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable chatRoomId: Long,
        @RequestParam(name = "cursor", required = false) cursor: Long?,
        @RequestParam(name = "size") size: Int,
    ): ResponseEntity<List<ChatResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(chatService.getAllChat2(userPrincipal, chatRoomId, cursor, size))
    }

}