package Client;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.Scanner;

public class Client {
    private final String QUEUE_NAME;
    private final Channel channel;
    private Connection connection;
    private final Channel channel1;
    private final Scanner in;
    public Client(Channel channel, String QUEUE_NAME, Connection connection) throws IOException {
        this.channel = channel;
        this.QUEUE_NAME = QUEUE_NAME;
        this.connection = connection;
        in = new Scanner(System.in);
        channel1 = connection.createChannel();
        channel1.queueDeclare("GET", false, false, false, null);
    }
    public void start(){
        Menu menu = new Menu();
        while(true) {
            menu.show_menu();

            System.out.println("Уведіть номер меню: ");
            int number_menu = in.nextInt();
            in.nextLine();

            switch (number_menu) {
                case (1):
                    send_message(number_menu + "#" + menu.add_airline());
                    is_correct();
                    break;
                case (2):
                    send_message(number_menu + "#" + menu.add_trip());
                    is_correct();
                    break;
                case (3):
                    send_message(number_menu + "#" + menu.delete_trip());
                    is_correct();
                    break;
                case (4):
                    send_message("4");
                    show_set_all();
                    break;
                default:
                    System.out.println("Уведіть номер меню!");
                    break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
    private void send_message(String message){
        try {
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void is_correct(){
        try{
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                if(Integer.parseInt(message) == 1){
                    System.out.println("Виконано!");
                }
                else{
                    System.out.println("Помилка!");
                }
            };
            channel1.basicConsume("GET", true, deliverCallback, consumerTag -> { });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void show_set_all(){
        try{
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String str = new String(delivery.getBody(), "UTF-8");

                if(str.equals("")){
                    System.out.println("Нічого не знайдено!");
                }
                else{
                    String[] res = str.split("#");
                    int prev_id_airline = 0;
                    int id = 1;
                    for (int i = 0; i < res.length; i++) {
                        if(res[i].equals("Id trip:")){
                            if(res[i+1].equals("0")){
                                i+=9;
                                continue;
                            }
                        }
                        if(res[i].equals("Id: ")){
                            id = Integer.parseInt(res[i+1]);
                            if(id != prev_id_airline){
                                System.out.println(res[i] + " " + res[i + 1] + " ");
                                System.out.println(res[i+2] + " " + res[i + 3] + " ");
                                i = i + 3;
                            }
                            else{
                                i = i+3;
                            }
                        }
                        else {
                            System.out.print("\t" + res[i] + " ");
                        }
                        prev_id_airline = id;
                        if(i % 2 != 0){
                            System.out.println();
                        }
                    }
                }
            };
            channel1.basicConsume("GET", true, deliverCallback, consumerTag -> { });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
