package org.example.back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")  // CORS 설정: 로컬 서버에서 오는 요청을 허용
@RequestMapping("/api")
public class ChatController {

    private Map<String, String> names = new HashMap<>();

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/save-username")
    public ResponseEntity<String> saveUsername(@RequestBody Map<String, String> data) {
        String username = data.get("username");

        System.out.println("Username saved: " + username);
        names.put(username, username);
        return ResponseEntity.ok("Username saved");
    }

    @PostMapping("/send-message")
    public ResponseEntity<String> sendMessage(@RequestBody Map<String, String> data) {
        String message = data.get("message");
        System.out.println("Message sent to: " + message);

        // 메시지를 Express 서버로 전달
        String expressServerUrl = "http://localhost:3000/api/receive-message";
        HttpEntity<Map<String, String>> request = new HttpEntity<>(data);
        ResponseEntity<String> response = restTemplate.exchange(expressServerUrl, HttpMethod.POST, request, String.class);

        return ResponseEntity.ok("Message sent to Express server");
    }
    @PostMapping("/one-to-one")
    public ResponseEntity<String> oneToOne(@RequestBody Map<String, String> data) {
        System.out.println(data);
        String message = data.get("message");
        String expressServerUrl = "http://localhost:3000/api/one-to-one";
        HttpEntity<Map<String, String>> request = new HttpEntity<>(data);
        restTemplate.exchange(expressServerUrl, HttpMethod.POST, request, String.class);
        return ResponseEntity.ok("Message sent to Express server");
    }

}
