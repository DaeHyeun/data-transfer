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

    private QueueProducer queueProducer = new QueueProducer();
    private QueueConsumer queueConsumer = new QueueConsumer(new CountDownLatch(1));

    //아이디 중복 검사
    @PostMapping("/idchk")
    public String idchk(@RequestBody User user) {
        // QueueProducer 실행
        queueProducer.setCategory("idchk");
        queueProducer.setUsername(user.getUsername());
        queueProducer.setPassword("null");
        Thread producerThread = new Thread(queueProducer);
        producerThread.start();

        // CountDownLatch 사용하여 메시지 수신 대기
        CountDownLatch latch = new CountDownLatch(1);
        queueConsumer = new QueueConsumer(latch);
        Thread consumerThread = new Thread(queueConsumer);
        consumerThread.start();

        try {
            // 메시지를 받을 때까지 대기
            latch.await();
            // 메시지를 받은 후, isDuplicate 값을 반환
            String isDuplicate = queueConsumer.getResult();
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
            queueConsumer.setResult(null);
            producerThread.interrupt();
            consumerThread.interrupt();
        }
    }

    //회원가입
    @PostMapping("/join")
    public String join(@RequestBody User user) {
        // QueueProducer 실행
        queueProducer.setCategory("join");
        queueProducer.setUsername(user.getUsername());
        queueProducer.setPassword(user.getPassword());
        Thread producerThread = new Thread(queueProducer);
        producerThread.start();

        // CountDownLatch 사용하여 메시지 수신 대기
        CountDownLatch latch1 = new CountDownLatch(1);
        QueueConsumer consumer = new QueueConsumer(latch1);
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();

        try {
            // 메시지를 받을 때까지 대기
            latch1.await();

            // 메시지를 받은 후, isDuplicate 값을 반환
            String joinId = consumer.getResult();
            if (joinId != null && !joinId.isEmpty()) {
                return joinId;
            } else {
                return "에러";
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "에러";
        } finally {
            // 메모리 해제
            consumer.setResult(null);
            producerThread.interrupt();
            consumerThread.interrupt();
        }

    }

    //로그인
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        // QueueProducer 실행
        queueProducer.setCategory("login");
        queueProducer.setUsername(user.getUsername());
        queueProducer.setPassword(user.getPassword());
        Thread producerThread = new Thread(queueProducer);
        producerThread.start();

        // CountDownLatch 사용하여 메시지 수신 대기
        CountDownLatch latch = new CountDownLatch(1);
        queueConsumer = new QueueConsumer(latch);
        Thread consumerThread = new Thread(queueConsumer);
        consumerThread.start();

        try {
            // 메시지를 받을 때까지 대기
            latch.await();
            // 메시지를 받은 후, isDuplicate 값을 반환
            String isDuplicate = queueConsumer.getResult();
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
            queueConsumer.setResult(null);
            producerThread.interrupt();
            consumerThread.interrupt();
        }
    }


}
