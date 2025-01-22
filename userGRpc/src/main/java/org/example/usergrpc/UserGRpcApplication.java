package org.example.usergrpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.example.usergrpc.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class UserGRpcApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserGRpcApplication.class, args);
    }
   /*
    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 50052; // 사용하고자 하는 포트

        // gRPC 서버 인스턴스 생성
        Server server = ServerBuilder.forPort(port) // 포트 50052에서 서버 실행
                .addService(new UserServiceImpl()) // ChatService 구현체 등록
                .build();

        System.out.println("====================================================");
        System.out.println("메인서버 가동 " +  ":" + port + "...");
        System.out.println("====================================================");


        // 서버 시작
        server.start();

        // 서버가 종료되지 않도록 대기
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("gRPC서버 종료");
            server.shutdown();
        }));

        // 서버 종료 대기
        server.awaitTermination();
    }
    */
}
