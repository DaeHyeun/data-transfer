package org.example.back.config;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.back.user.UserServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfig {

    @Bean
    public ManagedChannel managedChannel() {
        return ManagedChannelBuilder.forAddress("localhost", 6565)
                .usePlaintext()  // TLS 비활성화 (개발 환경에서)
                .build();
    }

    // 비동기 UserServiceStub을 생성
    @Bean
    public UserServiceGrpc.UserServiceStub userServiceStub(ManagedChannel managedChannel) {
        return UserServiceGrpc.newStub(managedChannel);
    }

    // 동기 UserServiceStub을 생성
    @Bean
    public UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub(ManagedChannel managedChannel) {
        return UserServiceGrpc.newBlockingStub(managedChannel);
    }

    @Bean
    public UserServiceGrpc.UserServiceFutureStub userServiceFutureStub(ManagedChannel managedChannel) {
        return UserServiceGrpc.newFutureStub(managedChannel);
    }





}