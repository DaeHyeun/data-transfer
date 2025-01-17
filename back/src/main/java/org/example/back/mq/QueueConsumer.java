package org.example.back.mq;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.concurrent.CountDownLatch;

public class QueueConsumer implements Runnable, ExceptionListener {
    private String isDuplicate;
    private final CountDownLatch latch;

    public QueueConsumer(CountDownLatch latch) {
        this.latch = latch;
    }

    public void setIsDuplicate(String isDuplicate) {
        this.isDuplicate = isDuplicate;
    }

    @Override
    public void run() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        try {
            Connection connection = connectionFactory.createConnection();
            connection.start();
            connection.setExceptionListener(this);

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("isDuplicate");
            MessageConsumer consumer = session.createConsumer(destination);

            Message message = consumer.receive();  // 메시지 수신
            System.out.println(((TextMessage)message).getText());

            if (message instanceof TextMessage) {
                this.isDuplicate = ((TextMessage) message).getText();  // 메시지 내용 저장
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

    public String getIsDuplicate() {
        return isDuplicate;
    }
}
