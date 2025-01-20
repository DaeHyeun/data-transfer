package org.example.usergrpc.mq;

import lombok.Getter;
import lombok.Setter;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;


@Getter
@Setter
public class Producer implements Runnable {

    private String result;

    public Producer(String result) {
        this.result = result;
    }

    @Override
    public void run() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        try {
            Connection connection = connectionFactory.createConnection();

            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination destination = session.createQueue("gRpcToRest");

            MessageProducer producer = session.createProducer(destination);

            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            Message message = session.createTextMessage(result);

            producer.send(message);

            session.close();

            connection.close();

        } catch (JMSException e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }
}
