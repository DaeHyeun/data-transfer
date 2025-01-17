package org.example.usergrpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.example.usergrpc.mq.QueueConsumer;
import org.example.usergrpc.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class UserGRpcApplication {

    @Autowired
    private QueueConsumer queueConsumer;  // 비-static으로 두고 Spring이 의존성 주입

    public static void main(String[] args) throws IOException, InterruptedException {
        // Spring Boot 애플리케이션 실행
        ApplicationContext context = SpringApplication.run(UserGRpcApplication.class, args);

        // Spring에서 생성된 UserGRpcApplication 객체를 가져와서 runGrpcServer 호출
        UserGRpcApplication app = context.getBean(UserGRpcApplication.class);
        app.runGrpcServer();
    }

    // 비-static 메서드로 gRPC 서버 실행
    public void runGrpcServer() throws IOException, InterruptedException {
        int port = 50052;

        // gRPC 서버 인스턴스 생성
        Server server = ServerBuilder.forPort(port)
                .addService(new UserServiceImpl())
                .build();

        System.out.println("====================================================");
        System.out.println("사용자 grpc 가동 " + ":" + port + "...");
        System.out.println("====================================================");

        // 서버 시작
        server.start();

        // MQ에서 데이터 처리할 Consumer Thread 시작
        Thread queueThread = new Thread(queueConsumer);
        queueThread.start();

        // 서버가 종료되지 않도록 대기
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("gRPC서버 종료");
            server.shutdown();
        }));

        // 서버 종료 대기
        server.awaitTermination();
    }
}
