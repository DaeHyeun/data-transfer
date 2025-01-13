package org.example.back.controller;

import org.example.back.model.Message;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.ls.LSException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/multi")  // "/multi" 경로로 들어오는 요청을 처리하는 컨트롤러
@CrossOrigin(origins = "http://localhost:3000")  // CORS 설정: 로컬 서버에서 오는 요청을 허용
public class MultiController {

    // RestTemplate 객체를 재사용하기 위해 클래스 레벨에서 선언
    private RestTemplate restTemplate = new RestTemplate();

    // HTTP 요청 헤더를 재사용하기 위해 클래스 레벨에서 선언
    private HttpHeaders headers = new HttpHeaders();

    private List<String> userList = new ArrayList<>();

    private List<String> messageList = new ArrayList<>();
    // 생성자에서 기본 헤더 설정
    public MultiController() {
        headers.set("Content-Type", "application/json");  // JSON 형식으로 요청을 보낼 때 필요한 헤더 설정
    }

    // 유저를 추가하는 API
    @PostMapping("/addUser")
    public String addUser(@RequestBody HashMap<String, String > userId) {
        System.out.println("adduser");

        String id = userId.get("userId"); // userId에서 아이디 값을 꺼냄

        // 유저 아이디가 이미 존재하는지 확인
        if (userList.contains(id)) {
            // 중복되는 아이디가 있으면 "중복" 리턴
            return "중복";
        } else {
            // 신규 아이디일 경우 userList에 추가
            userList.add(id);
            System.out.println(userList);
            System.out.println(userList.size());
            return "신규";
        }
    }

    // 메시지를 수신하고 모든 유저에게 전달하는 API
    @PostMapping("/multiChat")
    public String multiChat(@RequestBody Message message) {
        messageList.add(message.getSenderId() + " : " + message.getMessage());
        System.out.println("multichat");
        String url = "http://localhost:3000/message";  // 각 유저에 맞는 URL로 메시지 전송
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("result", "신규");
        requestBody.put("sessionId", message.getSenderId());
        // HTTP 요청 엔티티 생성
        HttpEntity<HashMap<String, String>> entity = new HttpEntity<>(requestBody, headers);

        // RestTemplate을 사용해 HTTP POST 요청 보내기
        try {
            restTemplate.postForObject(url, entity, String.class);
        } catch (Exception e) {
            // 예외 처리 (예: 요청 실패 시)
            System.out.println("Error sending message to user " + ": " + e.getMessage());
        }

        return "통신완";
    }

    // 세션 ID와 메시지를 수신하는 API
    @PostMapping("/getMessage")
    public HashMap<String, Object> getMessage(@RequestBody HashMap<String, String> requestData) {
        // requestData에서 sessionId와 result 값을 받습니다.
        String sessionId = requestData.get("sessionId");

        // sessionId와 result 값을 출력하거나 처리하는 로직을 추가할 수 있습니다.
        System.out.println("Received sessionId: " + sessionId);

        // 필요에 따라 sessionId를 기반으로 추가 작업을 처리할 수 있습니다.
        HashMap<String, Object> responseData = new HashMap<>();
        List<String> sessionIdList = new ArrayList<>();
        sessionIdList.add(sessionId);
        responseData.put("sessionId", sessionId);
        responseData.put("messages", messageList);

        return responseData;
    }


}
