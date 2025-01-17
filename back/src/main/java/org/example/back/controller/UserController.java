package org.example.back.controller;

import org.example.back.model.User;
import org.example.back.mq.QueueConsumer;
import org.example.back.mq.QueueProducer;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CountDownLatch;

@RestController
@RequestMapping("/user")  // "/user" 경로로 들어오는 요청을 처리하는 컨트롤러
@CrossOrigin(origins = "http://localhost:3000")  // CORS 설정: 로컬 서버에서 오는 요청을 허용
public class UserController {

    @PostMapping("/idchk")
    public String idchk(@RequestBody User user) {
        System.out.println(user.getUsername());

        // QueueProducer 실행
        QueueProducer queueProcedure = new QueueProducer("idchk", user.getUsername(), "11");
        Thread producerThread = new Thread(queueProcedure);
        producerThread.start();

        // CountDownLatch 사용하여 메시지 수신 대기
        CountDownLatch latch = new CountDownLatch(1);
        QueueConsumer queueConsumer = new QueueConsumer(latch);
        Thread consumerThread = new Thread(queueConsumer);
        consumerThread.start();

        try {
            // 메시지를 받을 때까지 대기
            latch.await();

            // 메시지를 받은 후, isDuplicate 값을 반환
            String isDuplicate = queueConsumer.getIsDuplicate();
            if (isDuplicate != null && !isDuplicate.isEmpty()) {
                return isDuplicate;
            } else {
                return "에러";
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "에러";
        } finally {
            // 메모리 해제
            queueConsumer.setIsDuplicate(null);
            producerThread.interrupt();
            consumerThread.interrupt();
        }
    }



}
