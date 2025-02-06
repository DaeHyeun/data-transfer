package org.example.usergrpc.user;


import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.example.usergrpc.entity.FileEntity;
import org.example.usergrpc.entity.MessageEntity;
import org.example.usergrpc.entity.UserEntity;
import org.example.usergrpc.repositorry.FileRepository;
import org.example.usergrpc.repositorry.MessageRepository;
import org.example.usergrpc.repositorry.UserRepository;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

@GRpcService
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    @Autowired
    private UserRepository userRepository;  // UserRepository 주입
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private FileRepository fileRepository;

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
            //uuid
            //경로 : "C:\\Users\\HCNC\\Desktop\\chatFile\\"
            String oriFileName = request.getOriFileName();  // 예시로 파일 이름 가져오기
            String sender = request.getSender();
            List<String> receiveLsitList = request.getReceiveLsitList();
            String ext = request.getExt();
            String size = request.getSize();

            // 파일명 디코딩
            String decodedFileName = URLDecoder.decode(oriFileName, "UTF-8");
            String uuid = generateUUIDWithoutHyphens();

            System.out.println("=======================================");
            System.out.println(generateUUIDWithoutHyphens());
            System.out.println("=======================================");

            //message table 저장
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setMessage("file::" + uuid + oriFileName.substring(oriFileName.lastIndexOf(".")));
            messageEntity.setSender(userRepository.findByUsername(sender));
            messageEntity.setReceiveList(String.join(",", receiveLsitList));
            messageRepository.save(messageEntity);

            //file table 저장
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileExtension(ext);
            fileEntity.setFileSize(size);
            fileEntity.setOriginalFileName(decodedFileName);
            fileEntity.setFilePath("C:\\Users\\HCNC\\Desktop\\chatFile\\");
            fileEntity.setFileUuid(uuid);
            fileEntity.setReceiveList(String.join(",", receiveLsitList));
            fileEntity.setMessage(messageRepository.findByMessage("file::" + uuid + oriFileName.substring(oriFileName.lastIndexOf("."))));
            fileEntity.setSender(userRepository.findByUsername(sender));
            fileRepository.save(fileEntity);

            //uuid이름으로 파일 저장
            saveFile(fileData, uuid+oriFileName.substring(oriFileName.lastIndexOf(".")));
            // 파일 처리 로직 구현 (파일을 디스크에 저장하거나 DB에 저장 등)

            // 응답 생성
            UsergRpcResponse response = UsergRpcResponse.newBuilder()
                    .setValidate(true)
                    .setMessage("file::" + uuid+oriFileName.substring(oriFileName.lastIndexOf(".")))
                    .build();

            // 응답 보내기
            responseObserver.onNext(response);
            // 스트림을 정상적으로 종료
            responseObserver.onCompleted();
        } catch (Exception e) {
            // 예외 발생 시 스트림에 오류 전송
            e.printStackTrace();
            responseObserver.onError(Status.INTERNAL.withDescription("파일 처리 중 오류 발생").asRuntimeException());
            UsergRpcResponse response = UsergRpcResponse.newBuilder()
                    .setValidate(false)
                    .setMessage("뭔가 실패")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    //파일저장 method
    public void saveFile(byte[] fileData, String fileName) throws IOException {

        // 파일 저장 경로 지정
        String savePath = "C:\\Users\\HCNC\\Desktop\\chatFile\\" + fileName;
        // File 객체 생성
        File file = new File(savePath);

        // 파일 경로가 존재하지 않으면 디렉토리 생성
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();  // 상위 디렉토리 생성
        }

        // FileOutputStream을 사용하여 파일을 저장
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(fileData);  // 파일 데이터 저장
            System.out.println("파일이 성공적으로 저장되었습니다: " + savePath);
        } catch (IOException e) {
            System.err.println("파일 저장 중 오류가 발생했습니다: " + e.getMessage());
            throw e;
        }
    }
    //uuid 생성
    public static String generateUUIDWithoutHyphens() {
        // UUID 생성
        UUID uuid = UUID.randomUUID();
        // 하이픈 제거
        return uuid.toString().replace("-", "");
    }
}
