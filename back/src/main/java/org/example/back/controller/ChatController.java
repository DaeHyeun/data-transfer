package org.example.back.controller;

import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")  // CORS 설정: 로컬 서버에서 오는 요청을 허용
@RequestMapping("/api")
public class ChatController {

    private Map<String, String> names = new HashMap<>();

    @PostMapping("/save-username")
    public ResponseEntity<String> saveUsername(@RequestBody Map<String, String> data) {
        String username = data.get("username");
        // 사용자 이름을 저장 (예: 데이터베이스)

        System.out.println("Username saved: " + username);
        names.put(username, username);
        return ResponseEntity.ok("Username saved");
    }

    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> handleChatMessage(@RequestBody Map<String, String> message) {
        // 메시지 처리 후, 필요한 경우 수정하여 반환
        System.out.println("Received message from " + message.get("user") + ": " + message.get("message"));
        return ResponseEntity.ok(message);
    }
}
