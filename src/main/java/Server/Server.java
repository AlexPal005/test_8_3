package Server;

import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Server {
    private String command;
    private final Channel channel1;
    private final String QUEUE_GET;
    private Airport airport;
    public Server(String command, Channel channel1, String QUEUE_GET ){
        this.command = command;
        this.channel1 = channel1;
        this.QUEUE_GET = QUEUE_GET;
        try {
            airport = new Airport();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        String[] fields = command.split("#");

        if (command == null || fields.length == 0) {
            return_error();
        } else {
            int number_menu = Integer.parseInt(fields[0]);
            switch (number_menu) {
                case (1):
                    first(fields);
                    break;
                case (2):
                    second(fields);
                    break;
                case (3):
                    third(fields);
                    break;
                case (4):
                    fourth();
                    break;
                default:
                    return_error();
                    break;
            }

        }
    }
     private void return_error(){
         try {
             channel1.basicPublish("", QUEUE_GET, null, "0".getBytes());
         } catch (IOException ex) {
             throw new RuntimeException(ex);
         }
     }
     private void return_success(){
         try {
             channel1.basicPublish("", QUEUE_GET, null, "1".getBytes());
         } catch (IOException ex) {
             throw new RuntimeException(ex);
         }
     }
    private void first(String[] fields){
        if(airport.add_new_airline(Integer.parseInt(fields[1]),fields[2])){
           return_success();
        }
        else{
            return_error();
        }
    }
    private void second(String[] fields){
        if(airport.add_trip(Integer.parseInt(fields[1]), fields[2],fields[3],
                Double.parseDouble(fields[4]),Integer.parseInt(fields[5]))){
            return_success();
        }
        else{
            return_error();
        }
    }
    private void third(String[] fields){
        if(airport.delete_trip_by_id(Integer.parseInt(fields[1]))){
            return_success();
        }
        else{
            return_error();
        }
    }
    private void fourth(){
        ResultSet result_set = airport.get_all();
        try{
            String result = "";
            while(result_set.next()){
                int id_airline = result_set.getInt("id_airline");
                String name = result_set.getString("name");

                int id_res = result_set.getInt("id_trip");
                String city_to =  result_set.getString("city_to");
                String city_from =  result_set.getString("city_from");
                double price = result_set.getDouble("price");
                int id_trip = result_set.getInt("id_airl");

                result +=
                        "Id: " + "#" + id_airline + "#" +
                                "Name: " + "#" + name + "#" +
                                "Id trip:"  + "#" + id_res + "#" +
                                "Місто прибуття: " + "#" +  city_to + "#" +
                                "Місто відправлення: "  + "#" + city_from + "#" +
                                "Ціна квитка: " + "#"  + price + "#" +
                                "Id авіакомпанії : " + "#"  + id_trip + "#";
            }
            channel1.basicPublish("", QUEUE_GET, null, result.getBytes());
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
