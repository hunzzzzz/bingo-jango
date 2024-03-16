package team.b2.bingojango.domain.chatting.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.b2.bingojango.domain.chatting.dto.ChatRequest
import team.b2.bingojango.domain.chatting.dto.ChatResponse
import team.b2.bingojango.domain.chatting.model.ChatRoom
import team.b2.bingojango.domain.chatting.service.ChatRoomService
import team.b2.bingojango.domain.chatting.service.ChatService
import team.b2.bingojango.global.security.jwt.JwtAuthenticationToken
import team.b2.bingojango.global.security.jwt.JwtPlugin
import team.b2.bingojango.global.security.util.UserPrincipal

@RestController
//@RequestMapping("/api/v1/refrigerator")
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
    @MessageMapping("/chatroom/sendMessage")
//    @SendTo("/sub/chatroom/{chatRoomId}")
    fun sendMessage(
        @Payload chatRequest: ChatRequest,
//        @DestinationVariable chatRoomId: Long,
        accessor: StompHeaderAccessor,
    ): ChatResponse {
        val userPrincipal = (accessor.user as JwtAuthenticationToken).principal
//        val userPrincipal= infoFromMessageHeader(accessor)
//        messageTemplate.convertAndSend("/sub/chatroom/${chatRequest.chatRoomId}", chatRequest)
        return chatService.sendMessage(userPrincipal, chatRequest)
    }

    @MessageMapping("api/v2/chatroom/sendMessage2")
    fun sendMessage2(
        chatRequest: ChatRequest,
        accessor: StompHeaderAccessor,
    ): ChatResponse {
        val userPrincipal = (accessor.user as JwtAuthenticationToken).principal
        return chatService.sendMessage2(userPrincipal, chatRequest)
    }

//        @MessageMapping("/chatroom/enterRoom")
//        fun enterRoom(
//            @Payload chatRequest: ChatRequest,
//            @AuthenticationPrincipal userPrincipal: UserPrincipal,
//            session: SimpMessageSendingOperations,
//            headerAccessor: SimpMessageHeaderAccessor
//        ) {
//            val userUUID = userPrincipal.id
//            headerAccessor.sessionAttributes?.put("userUUID", userUUID)
//            headerAccessor.sessionAttributes?.put("roomId", chatRequest.chatRoomId)
//
//            val message = ChatResponse(
//                chatRequest.chatRoomId.toLong(),
//                "system",
//                "$userUUID 님이 입장했습니다.",
//                ChatStatus.JOIN,
//                ZonedDateTime.now()
//            )
//            messageTemplate.convertAndSend("sub/chatroom/${chatRequest.chatRoomId}", message)
//        }

//        @EventListener
//        fun webSocketDisconnectListener(event: SessionDisconnectEvent) {
//            val headerAccessor = StompHeaderAccessor.wrap(event.message)
//            val userUUID = headerAccessor.sessionAttributes?.get("userUUID")
//            val roomId = headerAccessor.sessionAttributes?.get("roomId")
//
//        }

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
        @AuthenticationPrincipal userPrincipal: UserPrincipal
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

    // 테스트용 채팅방 목록 호출
    @GetMapping("/v1/chatroom")
    fun getAllChatRoom(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
    ): ResponseEntity<List<ChatRoom>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(chatService.getAllChatRoom(userPrincipal))
    }

//    private fun infoFromMessageHeader(accessor: StompHeaderAccessor): UserPrincipal {
//        val token = accessor.getFirstNativeHeader("Authorization")
//            ?: throw AccessDeniedException("invalid token")
//        jwtPlugin.validateToken(token)
//            .onSuccess {
//                val userId = it.payload.subject.toLong()
//                val email = it.payload.get("email", String::class.java)
//                val role = it.payload.get("role", String::class.java)
//
//                return UserPrincipal(
//                    id = userId,
//                    email = email,
//                    roles = setOf(role)
//                )
//            }.onFailure {
//                throw AccessDeniedException("invalid token")
//            }.getOrThrow()
//        return UserPrincipal(id = -1, email = "-1", roles = setOf("User"))
//    }
}