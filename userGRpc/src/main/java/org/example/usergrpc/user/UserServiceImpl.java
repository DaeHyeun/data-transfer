package org.example.usergrpc.user;


import io.grpc.stub.StreamObserver;
import org.example.usergrpc.entity.UserEntity;
import org.example.usergrpc.repositorry.UserRepository;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@GRpcService
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    @Autowired
    private UserRepository userRepository;  // UserRepository 주입

    @Override
    public void userIdChkAndJoinAndLogin(User request, StreamObserver<UsergRpcResponse> responseObserver) {
        switch (request.getCategory()) {
            case "idChk":
                String username = request.getUsername();
                boolean validate = userRepository.existsByUsername(username);
                String message = validate ? "아이디가 중복되었습니다." : "아이디 사용 가능합니다.";

                UsergRpcResponse idChkResponse = UsergRpcResponse.newBuilder()
                        .setValidate(validate)
                        .setMessage(message)
                        .build();

                responseObserver.onNext(idChkResponse);  // 응답을 전송
                responseObserver.onCompleted();    // 처리 완료
                break;
            case "login":
                try {
                    UserEntity user = userRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword());
                    UsergRpcResponse loginResponse = UsergRpcResponse.newBuilder()
                            .setValidate(true)
                            .setMessage(user.getUsername())
                            .build();
                    responseObserver.onNext(loginResponse);
                    responseObserver.onCompleted();
                } catch (Exception e) {
                    UsergRpcResponse loginResponse = UsergRpcResponse.newBuilder()
                            .setValidate(false)
                            .setMessage("로그인 실패")
                            .build();
                    responseObserver.onNext(loginResponse);
                    responseObserver.onCompleted();
                }
                break;
            case "join":
                try {
                    UserEntity user = new UserEntity(request.getUsername(), request.getPassword(), request.getCategory());
                    userRepository.save(user);
                    UsergRpcResponse joinResponse = UsergRpcResponse.newBuilder()
                            .setValidate(true)
                            .setMessage("회원가입 성공")
                            .build();
                    responseObserver.onNext(joinResponse);
                    responseObserver.onCompleted();
                } catch (Exception e) {
                    UsergRpcResponse joinResponse = UsergRpcResponse.newBuilder()
                            .setValidate(false)
                            .setMessage("회원가입 실패")
                            .build();
                    responseObserver.onNext(joinResponse);
                    responseObserver.onCompleted();
                }
                break;
            case "getUserList":
                List<UserEntity> userList = userRepository.findAll();
                String userStrList = "";
                for (UserEntity user : userList) {
                    if(userStrList!= ""){
                    userStrList += user.getUsername()+",";
                    }else {
                        userStrList += user.getUsername();
                    }
                }
                UsergRpcResponse UserListResponse = UsergRpcResponse.newBuilder()
                        .setValidate(true)
                        .setMessage(userStrList)
                        .build();
                responseObserver.onNext(UserListResponse);
                responseObserver.onCompleted();
                break;
            default:
                UsergRpcResponse defaultResponse = UsergRpcResponse.newBuilder()
                        .setValidate(false)
                        .setMessage("해당 카테고리가 없습니다. ")
                        .build();

        }
    }

}
