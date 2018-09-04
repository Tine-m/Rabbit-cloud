package rabbit.rabbitdemo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class RabbitReceiver {

    private final static String QUEUE_NAME = "demo";

    public static void main(String[] args) throws Exception {
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
        
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(QUEUE_NAME, true, consumer);

        //start polling messages
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println(" [x] Received '" + message + "'");
        }
    }
}
