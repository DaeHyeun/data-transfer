//package org.example.back.config;
//
//
//import org.springframework.beans.factory.annotation.Value;
//
//@Configuration
//public class GrpcConfig {
//
//    @Value("${grpc.server.host}")
//    private String grpcServerHost;
//
//    @Value("${grpc.server.port}")
//    private int grpcServerPort;
//
//    @Bean
//    public ManagedChannel managedChannel() {
//        return ManagedChannelBuilder.forAddress(grpcServerHost, grpcServerPort)
//                .maxInboundMessageSize(10 * 1024 * 1024) // 10MB로 설정
//                .usePlaintext()
//                .build();
//    }
//
//    @Bean
//    public UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub(ManagedChannel channel) {
//        return UserServiceGrpc.newBlockingStub(channel);
//    }
//
//}