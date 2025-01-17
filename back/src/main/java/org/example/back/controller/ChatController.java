package org.example.back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@CrossOrigin(origins = "http://localhost:3000")  // CORS 설정: 로컬 서버에서 오는 요청을 허용
@RequestMapping("/api")
public class ChatController {

    private ConcurrentHashMap<String, List<String>> chatRooms = new ConcurrentHashMap<>();
    //private ConcurrentHashMap<String, Map<String, List<String>>> chat = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, List<String>> userRooms = new ConcurrentHashMap<>();


    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/join")
    public ResponseEntity<Map<String, List<String>>> saveUsername(@RequestBody Map<String, String> data) {
        String username = data.get("username");
        String room = data.get("room");

        System.out.println("Username saved: " + username);
        // 채팅방 생성 또는 기존 방 가져오기
        chatRooms.putIfAbsent(room, new ArrayList<>());

        // 사용자 접속 이력 확인 및 추가
        userRooms.putIfAbsent(username, new ArrayList<>());
        List<String> rooms = userRooms.get(username);
        System.out.println("Rooms saved: " + rooms);

        // 이전에 접속한 방인지 확인
        if (rooms.contains(room)) {
            Map<String, List<String>> response = new HashMap<>();
            System.out.println("chat: " + chatRooms.get(room));
            response.put("message", chatRooms.get(room));
            // 이전에 접속한 방이면 채팅 내역 반환
            return ResponseEntity.ok(response);
        } else {
            // 새로 접속한 방이면 사용자 이력에 추가
            rooms.add(room);
            Map<String, List<String>> response = new HashMap<>();
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/send-message")
    public ResponseEntity<Map<String, String>> sendMessage(@RequestBody Map<String, String> data) {
        String username = data.get("user");
        String room = data.get("room");
        String receiver = data.get("receiver");
        String message = data.get("message");
        String base64File = data.get("file");
        String fileName = data.get("fileName");
        String fileType = data.get("filetype");

        // 로그 출력
        System.out.println("Message sent: " + message);
        System.out.println("Username sent: " + username);
        System.out.println("Room sent: " + room);
        System.out.println("fileType : " + fileType);

        // 채팅 메시지 저장
        chatRooms.putIfAbsent(room, new ArrayList<>());
        String fullMessage = (fileName != null) ? username + ": " + message + ", " + fileName + ": " + fileType : username + ": " + message;
        chatRooms.get(room).add(fullMessage);

        System.out.println("chatHistory: " + chatRooms.get(room));

        // 메시지를 Express 서버로 전달
        String expressServerUrl = "http://localhost:3000/api/receive-message";
        HttpEntity<Map<String, String>> request = new HttpEntity<>(data);
        restTemplate.exchange(expressServerUrl, HttpMethod.POST, request, String.class);

        // 응답 생성
        Map<String, String> response = new HashMap<>();
        response.put("message", "ok");
        return ResponseEntity.ok(response);
    }



    @PostMapping("/one-to-one")
    public ResponseEntity<Map<String, String>> oneToOne(@RequestBody Map<String, String> data) {
        System.out.println(data);
        String message = data.get("message");
        String expressServerUrl = "http://localhost:3000/api/one-to-one";
        HttpEntity<Map<String, String>> request = new HttpEntity<>(data);
        restTemplate.exchange(expressServerUrl, HttpMethod.POST, request, String.class);

        Map<String, String> response = new HashMap<>();
        response.put("message", "ok");
        return ResponseEntity.ok(response);
    }

}