package org.example.back.mq;
import jakarta.jms.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.concurrent.CountDownLatch;

@Getter
@Setter
public class QueueConsumer implements Runnable, ExceptionListener {
    private String category;
    private String result;
    private final CountDownLatch latch;

    public QueueConsumer(CountDownLatch latch) {
        this.latch = latch;
    }



    @Override
    public void run() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        try {
            Connection connection = connectionFactory.createConnection();
            connection.start();
            connection.setExceptionListener(this);

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("gRpcToRest");
            MessageConsumer consumer = session.createConsumer(destination);

            Message message = consumer.receive();  // 메시지 수신
            System.out.println(((TextMessage)message).getText());

            if (message instanceof TextMessage) {
                this.result = ((TextMessage) message).getText();  // 메시지 내용 저장
            }

            // 메시지를 받았으므로 latch 카운트를 감소시킴
            latch.countDown();

            session.close();
            connection.close();

        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onException(JMSException e) {
        System.out.println("JMS Exception occurred. Shutting down client.");
    }

}
