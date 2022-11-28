package Client;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public class Main_client {
    private final String QUEUE_NAME = "hello";
    public static void main(String[] argv) throws Exception {
        new Main_client().start();

    }
    private void start() throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            Client client = new Client(channel,QUEUE_NAME, connection);
            client.start();
        }
    }
}
