package Server;

import java.sql.*;

public class Airport{
    private Connection connection;
    private Statement statement;
    public Airport() throws Exception{
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/airport", "root", "rudenko1234567");
        statement = connection.createStatement();
    }
    public boolean add_new_airline(int id, String name){
        try{
            String sql_command = "INSERT airlines VALUE(" + id + "," + " \" " + name  + "\"" + ");";
            statement.executeUpdate(sql_command);
            return true;
        }catch(SQLException e){
            return false;
        }
    }
    public boolean add_trip(int id, String city_to, String city_from, double price, int id_airline){
        try{
            String sql_command = "INSERT INTO trips VALUE (" + id +","+
                    "\"" + city_to + "\"" +","+
                    "\"" + city_from + "\"" + "," + price + "," + id_airline +");";
            statement.executeUpdate(sql_command);
            return true;
        }catch(SQLException e){
            return false;
        }
    }
    public boolean delete_trip_by_id(int id){
        try{
            String sql_command = "DELETE FROM trips WHERE id_trip =" + id + ";";
            int count = statement.executeUpdate(sql_command);
            return count > 0;
        }catch(SQLException e){
            return false;
        }
    }
    public ResultSet get_all(){
        try{
            String sql_command = "SELECT * FROM airlines LEFT JOIN trips\n" +
                    "ON trips.id_airl = airlines.id_airline;";
            ResultSet res = statement.executeQuery(sql_command);
            return res;
        }catch(SQLException e){
            throw new RuntimeException();
        }
    }
    public void stop(){
        try{
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

