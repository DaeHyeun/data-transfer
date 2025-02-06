package org.example.back.controller;

import com.google.protobuf.ByteString;
import org.checkerframework.checker.units.qual.A;
import org.example.back.entity.FileEntity;
import org.example.back.model.File;
import org.example.back.model.ReqMessage;
import org.example.back.repositorry.FileRepository;
import org.example.back.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/message")  // "/user" 경로로 들어오는 요청을 처리하는 컨트롤러
@CrossOrigin(origins = "http://localhost:3000")  // CORS 설정: 로컬 서버에서 오는 요청을 허용
public class MessageController {

    @Autowired
    private FileRepository fileRepository;

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
    public ResponseEntity<Map<String, String>> file(@RequestPart("tosend") String tosend, @RequestPart("file") MultipartFile file, @RequestPart("sender") String sender) {
        List<String> receiveLsit = List.of(tosend.substring(2, tosend.length()-2).split("\",\""));
        try {
            // MultipartFile을 바이트 배열로 변환
            byte[] fileBytes = file.getBytes();
            System.out.println("파일 바이트 배열 길이: " + fileBytes.length);  // 파일 크기 출력

            // 바이트 배열을 ByteString으로 변환
            ByteString fileByteString = ByteString.copyFrom(fileBytes);

            // ByteString을 사용하여 gRPC 메시지를 생성하고 전송할 수 있습니다.
            // 예시: gRPC 클라이언트에 전송하는 코드
            TransFile request = TransFile.newBuilder()
                    .addAllReceiveLsit(receiveLsit)
                    .setSender(sender)
                    .setOriFileName(file.getOriginalFilename())
                    .setExt(file.getContentType())
                    .setSize(""+file.getSize())
                    .setFile(fileByteString)  // ByteString을 사용하여 파일 전송
                    .build();
            UsergRpcResponse response = userServiceBlockingStub.transFile(request);
            if (response.getValidate()){
                // 처리 후 JSON 응답 반환
                Map<String, String> successResult = new HashMap<>();
                successResult.put("sender", sender);
                successResult.put("message", response.getMessage());
                successResult.put("receiverList", String.join(",", receiveLsit));
                return ResponseEntity.ok(successResult);  // 성공 시 원래 요청 메시지 반환
            }
            else{
                // 실패 시 실패 메시지 반환
                Map<String, String> errorResult = new HashMap<>();
                errorResult.put("message", "실패");
                errorResult.put("errorCode", "500");  // 예시로 에러 코드 추가
                errorResult.put("details", "메시지 전송 중 오류 발생");  // 상세 오류 메시지 추가

                return ResponseEntity.status(500).body(errorResult);  // 실패 시 500 상태 코드 반환
            }


        } catch (IOException e) {
            e.printStackTrace();
            Map<String, String> exceptionResult = new HashMap<>();
            exceptionResult.put("message", "실패: " + e.getMessage());  // 예외 메시지 반환
            exceptionResult.put("errorCode", "500");  // 500 상태 코드와 오류 코드 추가
            exceptionResult.put("details", "예외 발생: " + e.getClass().getSimpleName());  // 예외 클래스 이름 추가

            return ResponseEntity.status(500).body(exceptionResult);  // 500 상태 코드와 함께 예외 처리된 메시지 반환
        }
    }

    //다운로드
    @PostMapping("/downLoad")
    public ResponseEntity<byte[]> downLoad(@RequestBody File fileName) {
        // 파일 이름에서 UUID를 추출 (파일명에서 확장자 제외)
        String uuid = fileName.getFileName().substring(0, fileName.getFileName().lastIndexOf("."));

        // UUID에 해당하는 파일을 데이터베이스에서 찾기
        FileEntity findFile = fileRepository.findByFileUuid(uuid);
        if (findFile == null) {
            System.out.println("파일을 찾을 수 없습니다.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            // 파일 경로와 파일 UUID를 이용하여 파일을 찾아서 읽어오기
            String filePath = findFile.getFilePath();  // 기본 경로
            String fileUuid = findFile.getFileUuid();  // UUID
            String originalFileName = findFile.getOriginalFileName();  // 원본 파일 이름

            // 전체 파일 경로 생성
            String fullFilePath = filePath + fileUuid + "." + originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
            System.out.println("File Path to be downloaded: " + fullFilePath);

            // 파일을 바이트 배열로 읽어오기
            byte[] fileBytes = Files.readAllBytes(Paths.get(fullFilePath));

            // UTF-8로 인코딩된 파일 이름 생성
            String encodedFileName = URLEncoder.encode(originalFileName, "UTF-8").replaceAll("\\+", "%20");

            // 응답 헤더 설정 (파일 다운로드를 위해서)
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            headers.setContentDispositionFormData("attachment", encodedFileName);

            // 파일 바이트 배열을 ResponseEntity로 반환
            return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
