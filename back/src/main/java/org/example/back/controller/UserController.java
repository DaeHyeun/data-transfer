package org.example.back.controller;

import org.example.back.model.ReqMessage;
import org.example.back.model.ReqUser;
import org.example.back.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")  // "/user" 경로로 들어오는 요청을 처리하는 컨트롤러
@CrossOrigin(origins = "http://localhost:3000")  // CORS 설정: 로컬 서버에서 오는 요청을 허용
public class UserController {

    private final UserServiceGrpc.UserServiceStub userServiceStub;  // 비동기 Stub
    private final UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;  // 동기 Stub
    private final UserServiceGrpc.UserServiceFutureStub userServiceFutureStub;

    @Autowired
    public UserController(UserServiceGrpc.UserServiceStub userServiceStub, UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub, UserServiceGrpc.UserServiceFutureStub userServiceFutureStub) {
        this.userServiceStub = userServiceStub;
        this.userServiceBlockingStub = userServiceBlockingStub;
        this.userServiceFutureStub = userServiceFutureStub;
    }

    // 아이디 중복 검사
    @PostMapping("/idchk")
    public String idchkvv(@RequestBody ReqUser reqUser) {
        User request = User.newBuilder()
                .setUsername(reqUser.getUsername())
                .setCategory("idChk")
                .build();
        UsergRpcResponse response = userServiceBlockingStub.userIdChkAndJoinAndLogin(request);
        return "" + response.getValidate();
    }

    // 회원 가입 처리
    @PostMapping("/join")
    public String join(@RequestBody ReqUser reqUser) throws InterruptedException {
        User request = User.newBuilder()
                .setUsername(reqUser.getUsername())
                .setPassword(reqUser.getPassword())
                .setCategory("join")
                .build();
        UsergRpcResponse response = userServiceBlockingStub.userIdChkAndJoinAndLogin(request);
        return "" + response.getValidate();
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@RequestBody ReqUser reqUser) throws InterruptedException {
        User request = User.newBuilder()
                .setUsername(reqUser.getUsername())
                .setPassword(reqUser.getPassword())
                .setCategory("login")
                .build();

        UsergRpcResponse response = userServiceBlockingStub.userIdChkAndJoinAndLogin(request);
        return response.getMessage();
    }

    //유저 리스트 출력
    @PostMapping("/getUserList")
    public List<String> getuserList() throws InterruptedException {
        List<String> userList = new ArrayList<>();
        User request = User.newBuilder().setCategory("getUserList").build();
        UsergRpcResponse response = userServiceBlockingStub.userIdChkAndJoinAndLogin(request);
        userList = Arrays.stream(response.getMessage().split(",")).toList();
        return userList;
    }





}
