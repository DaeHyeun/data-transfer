package org.example.back.mq;


import jakarta.jms.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class QueueProducer implements Runnable{
    private String category;
    private String username;
    private String password;

    public QueueProducer(String category, String username) {
        this.category = category;
        this.username = username;
    }


    @Override
    public void run() {
        try {
            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

            // Create a Connection
            Connection connection = connectionFactory.createConnection();
            connection.start();

            // Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination
            Destination destination = session.createQueue("userGrpc");

            // Create a MessageProducer from the Session to the Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);


                // If a message is provided, send a MapMessage
                MapMessage mapMessage = session.createMapMessage();
                 // Add fields as key-value pairs to the MapMessage
                mapMessage.setString("category", this.category);
                mapMessage.setString("username", this.username);
                mapMessage.setString("password", this.password);
                // Send the MapMessage
                producer.send(mapMessage); // Send the MapMessage

            // Clean up
            //session.close();
            //connection.close();
        } catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }
}
