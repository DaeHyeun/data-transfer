package org.example.usergrpc.mq;

import io.grpc.stub.StreamObserver;
import lombok.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.example.usergrpc.user.IdCheckResponse;
import org.example.usergrpc.user.JoinUser;
import org.example.usergrpc.user.User;
import org.example.usergrpc.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.Enumeration;

@Component  // 스프링 빈으로 등록
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Consumer implements Runnable, ExceptionListener {

    private String category;
    private String username;
    private String password;

    @Autowired
    private UserServiceImpl userService;  // UserServiceImpl 주입

    public void run() {
        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            Connection connection = connectionFactory.createConnection();
            connection.start();
            connection.setExceptionListener(this);

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("restToGrpc");
            MessageConsumer consumer = session.createConsumer(destination);

            while (true) {
                Message message = consumer.receive();
                if (message instanceof MapMessage) {
                    MapMessage mapMessage = (MapMessage) message;

                    // 메시지에서 데이터 추출
                    for (Enumeration<?> e = mapMessage.getMapNames(); e.hasMoreElements(); ) {
                        String key = (String) e.nextElement();
                        System.out.println(key + ": " + mapMessage.getObject(key));
                        try {
                            if (mapMessage.itemExists(key)) {
                                if ("category".equals(key)) {
                                    this.category = mapMessage.getString(key);
                                } else if ("username".equals(key)) {
                                    this.username = mapMessage.getString(key);
                                } else if ("password".equals(key)) {
                                    this.password = mapMessage.getString(key);
                                }
                            }
                        } catch (JMSException e1) {
                            e1.printStackTrace();
                        }
                    }

                    // MQ에서 받은 데이터를 gRPC 서비스 메서드로 전달
                    // userService.handleUserMessage(this.username, this.password, this.category);

                    // MQ에서 받은 데이터를 gRPC 서비스 메서드로 전달
                    // StreamObserver를 생성하여 gRPC 메서드에 전달
                    //아이디 중복검사
                    StreamObserver<IdCheckResponse> responseObserver = new StreamObserver<IdCheckResponse>() {
                        @SneakyThrows
                        @Override
                        public void onNext(IdCheckResponse value) {
                                System.out.println("Received response from gRPC server:");
                                System.out.println("Is Duplicate: " + value.getIsDuplicate());
                                System.out.println("Message: " + value.getMessage());
                                System.out.println("Response: " + value.getMessage());
                                String str = "" + value.getIsDuplicate();

                                Producer queueProducer = new Producer(str);
                                Thread thread = new Thread(queueProducer);
                                thread.start();
                                //thread.interrupt();
                        }

                        @Override
                        public void onError(Throwable t) {
                            // 에러 처리
                            System.err.println("Error occurred: " + t.getMessage());
                        }

                        @Override
                        public void onCompleted() {
                            // 응답이 완료되었을 때 호출
                            System.out.println("ID check operation completed.");
                        }
                    };
                    //회원가입
                    StreamObserver<JoinUser> joinUserStreamObserver = new StreamObserver<JoinUser>() {
                        @SneakyThrows
                        @Override
                        public void onNext(JoinUser joinUser) {
                            String str = "" + joinUser.getJoinvalidate();
                            Producer queueProducer = new Producer(str);
                            Thread thread = new Thread(queueProducer);
                            thread.start();
                            //thread.interrupt();

                        }

                        @Override
                        public void onError(Throwable throwable) {

                        }

                        @Override
                        public void onCompleted() {

                        }
                    };

                    if (category.equals("idchk")) {
                        // `idChk` 메서드 호출 (StreamObserver 전달)
                        User request = User.newBuilder().setUsername(this.username).build();
                        userService.idChk(request, responseObserver);
                    } else if (category.equals("join")) {
                        User request = User.newBuilder().setUsername(this.username).setPassword(this.password).setCategory(this.category).build();
                        userService.handleUserMessage(request, joinUserStreamObserver);
                    } else if (category.equals("login")) {

                    }

                    // 로그 출력
                    System.out.println("Received Message and passed to gRPC service: " + this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void onException(JMSException ex) {
        System.out.println("JMS Exception occurred. Shutting down client.");
    }
}
