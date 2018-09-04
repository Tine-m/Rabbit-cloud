package rabbit.rabbitdemo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class RabbitSender {

    private final static String QUEUE_NAME = "demo";

    public static void main(String[] args) throws IOException, URISyntaxException, NoSuchAlgorithmException, KeyManagementException {

        //setting the connection to the RabbitMQ server
        ConnectionFactory factory = new ConnectionFactory();
        String uri = System.getenv("CLOUDAMQP_URL");
        if (uri == null) {
            uri = "amqp://guest:guest@localhost";
        }

        factory.setUri(uri);
        Connection connection = factory.newConnection();

        //make the channel for messaging
        Channel channel = connection.createChannel();
        
         //declare a queue
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        
        //start sending messages
        String message = "Hello World!!!";
        //for (int i = 0; i < Integer.MAX_VALUE; i++) {
        for (int i = 0; i < 3; i++) {
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        }
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }
}

