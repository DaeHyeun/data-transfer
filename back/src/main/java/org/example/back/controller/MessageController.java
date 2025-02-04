package org.example.back.controller;

import com.google.protobuf.ByteString;
import org.example.back.model.ReqMessage;
import org.example.back.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/message")  // "/user" 경로로 들어오는 요청을 처리하는 컨트롤러
@CrossOrigin(origins = "http://localhost:3000")  // CORS 설정: 로컬 서버에서 오는 요청을 허용
public class MessageController {

    private final UserServiceGrpc.UserServiceStub userServiceStub;  // 비동기 Stub
    private final UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;  // 동기 Stub
    private final UserServiceGrpc.UserServiceFutureStub userServiceFutureStub;

    @Autowired
    public MessageController(UserServiceGrpc.UserServiceStub userServiceStub, UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub, UserServiceGrpc.UserServiceFutureStub userServiceFutureStub) {
        this.userServiceStub = userServiceStub;
        this.userServiceBlockingStub = userServiceBlockingStub;
        this.userServiceFutureStub = userServiceFutureStub;
    }

    //메세지 전송
    @PostMapping("/sendMessage")
    public ResponseEntity<Map<String, String>> sendMessage(@RequestBody ReqMessage reqMessage) {
        List<String> aaa = reqMessage.getReceiverList();
        try {
            // gRPC 요청 메시지 생성
            Message request = Message.newBuilder()
                    .setSender(reqMessage.getSender())
                    .setMessage(reqMessage.getMessage())
                    .addAllReceiveLsit(reqMessage.getReceiverList())
                    .build();

            // gRPC 서버에 메시지 전송
            Message response = userServiceBlockingStub.sendMessage(request);

            // gRPC 응답 처리
            if (response.getMessage().equals("성공")) {
                // 처리 후 JSON 응답 반환
                Map<String, String> successResult = new HashMap<>();
                successResult.put("sender", reqMessage.getSender());
                successResult.put("message", reqMessage.getMessage());
                successResult.put("receiverList", String.join(",", reqMessage.getReceiverList()));
                return ResponseEntity.ok(successResult);  // 성공 시 원래 요청 메시지 반환
            } else {
                // 실패 시 실패 메시지 반환
                Map<String, String> errorResult = new HashMap<>();
                errorResult.put("message", "실패");
                errorResult.put("errorCode", "500");  // 예시로 에러 코드 추가
                errorResult.put("details", "메시지 전송 중 오류 발생");  // 상세 오류 메시지 추가

                return ResponseEntity.status(500).body(errorResult);  // 실패 시 500 상태 코드 반환
            }

        } catch (Exception e) {
            // 예외 발생 시 실패 메시지 반환
            // 예외가 발생하면 적절한 오류 메시지를 반환
            Map<String, String> exceptionResult = new HashMap<>();
            exceptionResult.put("message", "실패: " + e.getMessage());  // 예외 메시지 반환
            exceptionResult.put("errorCode", "500");  // 500 상태 코드와 오류 코드 추가
            exceptionResult.put("details", "예외 발생: " + e.getClass().getSimpleName());  // 예외 클래스 이름 추가

            return ResponseEntity.status(500).body(exceptionResult);  // 500 상태 코드와 함께 예외 처리된 메시지 반환
        }
    }

    //이전 메세지 가지고 오기
    @PostMapping("/savedMessage")
    public ResponseEntity<List<Map<String, String>>> savedMessage(@RequestBody ReqMessage reqMessage)  {
        // gRPC 요청 메시지 생성
        Message request = Message.newBuilder()
                .addAllReceiveLsit(reqMessage.getReceiverList())
                .build();

        // gRPC 서버에 메시지 전송
        SavedMessage response = userServiceBlockingStub.savedMessage(request);

        List<Map<String, String>> successResult = new ArrayList<>();
        for (int i = 0; i < response.getMessageListList().size(); i++) {
            HashMap<String, String> tempMap = new HashMap<>();
            tempMap.put("sender",response.getMessageListList().get(i).getSender());
            tempMap.put("message",response.getMessageListList().get(i).getMessage());
            tempMap.put("receiveLsit", response.getMessageListList().get(i).getReceiveLsitList().stream().collect(Collectors.joining(",")));
            successResult.add(tempMap);
        }
        return ResponseEntity.ok(successResult);
    }

    @PostMapping("/file")  // POST 메서드로 파일을 받음
    public String file(@RequestPart("tosend") String tosend, @RequestPart("file") MultipartFile file) {
        System.out.println("스프링 부트");
        System.out.println("tosend: " + tosend);  // 받은 tosend
        System.out.println("file: " + file.getOriginalFilename());  // 받은 파일의 이름
        System.out.println("=========================file class");
        System.out.println(tosend);
        System.out.println("file: " + file.getClass());
        System.out.println(file.getOriginalFilename());//dddd.jpf
        System.out.println(file.getName());//file
        System.out.println(file.getResource());//MultipartFile resource [file]
        System.out.println(file.getContentType());//image/jpeg
        System.out.println(file.getSize());//48361
        try {
            System.out.println(file.getBytes());//[B@5df75c06
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("=========================file class");

        try {
            // MultipartFile을 바이트 배열로 변환
            byte[] fileBytes = file.getBytes();
            System.out.println("파일 바이트 배열 길이: " + fileBytes.length);  // 파일 크기 출력

            // 바이트 배열을 ByteString으로 변환
            ByteString fileByteString = ByteString.copyFrom(fileBytes);

            // ByteString을 사용하여 gRPC 메시지를 생성하고 전송할 수 있습니다.
            // 예시: gRPC 클라이언트에 전송하는 코드
            TransFile request = TransFile.newBuilder()

                    .setFile(fileByteString)  // ByteString을 사용하여 파일 전송
                    .build();
            UsergRpcResponse response = userServiceBlockingStub.transFile(request);
            System.out.println(response.getMessage());

        } catch (IOException e) {
            e.printStackTrace();
            return "파일 처리 중 오류 발생";
        }


        return "success";
    }


}
