package org.example.usergrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.usergrpc.mq.QueueConsumer;

public class UserClient {
    public static void main(String[] args) {

        System.out.println("grpc 클라이언트 메인");
        String serverAddress = "172.168.10.71";
        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, 50052)
                .usePlaintext()
                .build();

        QueueConsumer queueConsumer = new QueueConsumer();
        Thread consumerThread = new Thread(queueConsumer);
        consumerThread.start();



        channel.shutdownNow();
    }
}
