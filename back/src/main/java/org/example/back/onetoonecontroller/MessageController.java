package org.example.back.onetoonecontroller;

import org.example.back.dto.ChatMessage; // 분리된 DTO 클래스 import
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = "http://localhost:3000")
public class MessageController {

    // HashMap으로 메시지 저장
    private final Map<String, List<Map<String, String>>> chatMessages = new HashMap<>();

    // 메시지 전송 및 저장
    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendMessage(@RequestBody ChatMessage chatMessage) {
        // 메시지 저장
        String receiveId = chatMessage.getReceiveId();
        Map<String, String> messageData = new HashMap<>();
        messageData.put("sendId", chatMessage.getSendId());
        messageData.put("message", chatMessage.getMessage());

        chatMessages.putIfAbsent(receiveId, new ArrayList<>()); // 수신자 ID로 초기화
        chatMessages.get(receiveId).add(messageData); // 메시지 추가

        System.out.println("Message stored for " + receiveId + ": " + messageData);

        // JSON 응답 생성
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", messageData);

        return ResponseEntity.ok(response);
    }

    // 특정 유저의 메시지 기록 반환
    @GetMapping("/history/{receiveId}")
    public ResponseEntity<Map<String, Object>> getChatHistory(@PathVariable String receiveId) {
        // 저장된 메시지를 반환
        List<Map<String, String>> messages = chatMessages.getOrDefault(receiveId, new ArrayList<>());

        System.out.println("Messages for " + receiveId + ": " + messages);

        // JSON 응답 생성
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("messages", messages);

        return ResponseEntity.ok(response);
    }
}
