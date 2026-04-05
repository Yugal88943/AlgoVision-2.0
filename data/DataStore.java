package data;

import java.util.HashMap;
import model.*;

public class DataStore {
    public HashMap<Integer, User> users = new HashMap<>();
    public HashMap<Integer, Order> orders = new HashMap<>();

    public void addUser(User u) {
        users.put(u.id, u);
    }

    public void addOrder(Order o) {
        orders.put(o.orderId, o);
    }

    public String getAllOrders() {
        StringBuilder sb = new StringBuilder();
        for (Order o : orders.values()) {
            sb.append(o.toString()).append("\n");
        }
        return sb.toString();
    }
}