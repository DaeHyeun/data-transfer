package org.example.usergrpc.user;

import io.grpc.stub.StreamObserver;
import org.example.usergrpc.entity.UserEntity;
import org.example.usergrpc.repositorry.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    @Autowired
    private UserRepository userRepository;  // UserRepository 주입

    @Override
    public void idChk(User request, StreamObserver<IdCheckResponse> responseObserver) {
        String username = request.getUsername();


        // 아이디 중복 검사 (DB에서 아이디 조회)
        boolean isDuplicate = userRepository.existsByUsername(username);
        String message = isDuplicate ? "아이디가 중복되었습니다." : "아이디 사용 가능합니다.";

        IdCheckResponse response = IdCheckResponse.newBuilder()
                .setIsDuplicate(isDuplicate) // 중복 여부 설정
                .setMessage(message)         // 메시지 설정
                .build();

        // 응답 전송
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


    // MQ에서 받은 데이터를 처리하고 DB에 저장하는 메서드
    public void handleUserMessage(User request, StreamObserver<JoinUser> responseObserver) {
        try {
            // User 객체 생성
            UserEntity user = new UserEntity(request.getUsername(), request.getPassword(), request.getCategory());

            try {
                UserEntity savedUser = userRepository.save(user);  // 저장 시도
                System.out.println("저장 성공: " + savedUser);  // 성공 시 출력
                responseObserver.onNext(JoinUser.newBuilder().setJoinvalidate(true).setMessage("회원가입 성공").build());
            } catch (Exception e) {
                System.out.println("실패");
                responseObserver.onNext(JoinUser.newBuilder().setJoinvalidate(true).setMessage("회원가입 실패").build());
            }

            System.out.println("User saved to DB: " + user);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error saving user to DB.");
        }
    }

    public void login(User request, StreamObserver<LoginUser> responseObserver) {
        try{
            UserEntity user = userRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword());
            responseObserver.onNext(LoginUser.newBuilder().setLogin(true).setMessage(user.getUsername()).build());
        }catch (Exception e){
            System.out.println("예외");
            System.out.println(e.getMessage());

        }

    }
}
