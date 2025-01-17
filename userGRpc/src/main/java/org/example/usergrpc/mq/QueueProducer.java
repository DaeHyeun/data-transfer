package org.example.usergrpc.mq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;

import javax.jms.*;


@Getter
@Setter
public class QueueProducer implements Runnable {

    private String isDuplicate;

    public QueueProducer(String isDuplicate) {
        this.isDuplicate = isDuplicate;
    }

    @Override
    public void run() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        try {
            Connection connection = connectionFactory.createConnection();

            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination destination = session.createQueue("isDuplicate");

            MessageProducer producer = session.createProducer(destination);

            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            Message message = session.createTextMessage(isDuplicate);

            producer.send(message);

            session.close();

            connection.close();

        } catch (JMSException e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }
}
