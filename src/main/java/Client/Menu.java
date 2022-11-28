package Client;
import java.util.Scanner;

public class Menu {
    private final Scanner in;
    public Menu(){
        in = new Scanner(System.in);
    }
    public void show_menu(){
        System.out.println("----------Меню----------");
        System.out.println("1. Додати нову авіакомпанію");
        System.out.println("2. Додати рейс");
        System.out.println("3. Видалити рейс");
        System.out.println("4. Отримати всі авіакомпанії з їх рейсами");
    }
    public String add_airline(){
        System.out.println("Уведіть id: ");
        int id = in.nextInt();
        in.nextLine();
        if(id <= 0 ){
            System.out.println("Помилка! id має бути більшим за 0!");
            return "";
        }
        else{
            System.out.println("Уведіть назву: ");
            String name = in.nextLine();
            return id + "#" + name;
        }

    }
    public String add_trip(){
        System.out.println("Уведіть id: ");
        int id = in.nextInt();
        in.nextLine();
        System.out.println("Уведіть місто відправлення: ");
        String city_from = in.nextLine();
        System.out.println("Уведіть місто прибуття: ");
        String city_to = in.nextLine();
        System.out.println("Уведіть ціну квитка: ");
        double price = in.nextDouble();
        in.nextLine();
        System.out.println("Уведіть id авіакомпанії:");
        int id_airline = in.nextInt();
        in.nextLine();

        return id + "#" + city_from + "#" + city_to + "#" + price + "#" + id_airline;

    }
    public String delete_trip(){
        System.out.println("Уведіть id: ");
        int id = in.nextInt();
        in.nextLine();
        return Integer.toString(id);
    }
}