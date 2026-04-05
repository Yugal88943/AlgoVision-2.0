package io;

import java.io.*;
import model.*;
import catalog.*;

public class FileManager {

    public static void loadFood(FoodCatalog catalog) {
        try (BufferedReader br = new BufferedReader(new FileReader("food.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                int id = Integer.parseInt(data[0]);
                String name = data[1];
                int price = Integer.parseInt(data[2]);
                String category = data[3]; 

                catalog.addItem(new FoodItem(id, name, price, category));
            }

        } catch (Exception e) {
            System.out.println("File load error");
        }
    }

    public static void saveOrders(java.util.Collection<Order> orders) {
        try (PrintWriter pw = new PrintWriter("orders.txt")) {
            for (Order o : orders) {
                pw.println(o.toString());
            }
        } catch (Exception e) {
            System.out.println("Save error");
        }
    }
}