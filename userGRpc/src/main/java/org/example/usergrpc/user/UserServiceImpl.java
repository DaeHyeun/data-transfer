package org.example.usergrpc.user;


import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.example.usergrpc.entity.MessageEntity;
import org.example.usergrpc.entity.UserEntity;
import org.example.usergrpc.repositorry.MessageRepository;
import org.example.usergrpc.repositorry.UserRepository;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

@GRpcService
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    @Autowired
    private UserRepository userRepository;  // UserRepository 주입
    @Autowired
    private MessageRepository messageRepository;

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
                String userStrList = userList.stream()
                        .map(UserEntity::getUsername)
                        .collect(Collectors.joining(","));
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

    @Override
    public void sendMessage(Message request, StreamObserver<Message> responseObserver) {
        try {
            // 사용자 정보 가져오기
            UserEntity user1 = userRepository.findByUsername(request.getSender());

            if (user1 == null) {
                // 만약 sender가 유효하지 않다면 실패 메시지 반환
                Message response = Message.newBuilder()
                        .setMessage("실패: 유효하지 않은 sender")
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                return;
            }

            // MessageEntity 객체 생성
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setSender(user1);
            messageEntity.setMessage(request.getMessage());

            // receiveList를 쉼표로 구분된 문자열로 변환
            String strmessage = String.join(",", request.getReceiveLsitList());
            messageEntity.setReceiveList(strmessage);

            // 데이터베이스에 메시지 저장
            messageRepository.save(messageEntity);

            // 성공 응답 반환
            Message response = Message.newBuilder()
                    .setMessage("성공")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            // 예외 발생 시 실패 메시지 반환
            Message response = Message.newBuilder()
                    .setMessage("실패: " + e.getMessage())  // 예외 메시지를 클라이언트에게 전달
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void savedMessage(Message request, StreamObserver<SavedMessage> responseObserver) {
        int len = request.getReceiveLsitList().size();
        List<MessageEntity> result = messageRepository.findAll();
        List<Message> messageList = new ArrayList<>();

        for (int i = 0; i < result.size(); i++) {
            String[] temp = result.get(i).getReceiveList().split(",");
            List<String> receiveList = Arrays.asList(temp);
            if (len > 2) {  // 전체 메세지
                if (temp.length > 2) {
                    messageList.add(
                            Message.newBuilder()
                                    .setMessage(result.get(i).getMessage())
                                    .setSender(result.get(i).getSender().getUsername())
                                    .addAllReceiveLsit(receiveList)  // 메서드 이름 수정
                                    .build()  // 불필요한 세미콜론 제거
                    );
                }
            } else {//1:1 메세지
                if (temp.length <= 2) {
                    if (new HashSet<>(request.getReceiveLsitList()).equals(new HashSet<>(receiveList))) {
                        messageList.add(
                                Message.newBuilder()
                                        .setMessage(result.get(i).getMessage())
                                        .setSender(result.get(i).getSender().getUsername())
                                        .addAllReceiveLsit(receiveList)  // 메서드 이름 수정
                                        .build()  // 불필요한 세미콜론 제거
                        );
                    } else {
                        System.out.println("두 리스트의 내용이 다릅니다.");
                    }

                }
            }
        }

        SavedMessage response = SavedMessage.newBuilder()
                .addAllMessageList(messageList)
                .build();
        System.out.println(response);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    //첨부파일 메세지
    @Override
    public void transFile(TransFile request, StreamObserver<UsergRpcResponse> responseObserver) {
        try {
            System.out.println("첨부파일 grpc");

            // TransFile 객체에서 필요한 정보 추출
            byte[] fileData = request.getFile().toByteArray();  // 예시로 파일 데이터를 byte 배열로 가져옴
            String fileName = request.getOriFileName();  // 예시로 파일 이름 가져오기

            System.out.println("파일 이름: " + fileName);
            System.out.println("파일 데이터 크기: " + fileData.length);

            // 파일 처리 로직 구현 (파일을 디스크에 저장하거나 DB에 저장 등)

            // 응답 생성
            UsergRpcResponse response = UsergRpcResponse.newBuilder()
                    .setMessage("파일 처리 성공")
                    .build();

            // 응답 보내기
            responseObserver.onNext(response);

            // 스트림을 정상적으로 종료
            responseObserver.onCompleted();

        } catch (Exception e) {
            // 예외 발생 시 스트림에 오류 전송
            e.printStackTrace();
            responseObserver.onError(Status.INTERNAL.withDescription("파일 처리 중 오류 발생").asRuntimeException());
        }
    }
}
