package ingestion;

import java.util.LinkedList;
import model.Order;

public class OrderQueue {
    private LinkedList<Order> queue = new LinkedList<>();

    public void addOrder(Order o) {
        queue.add(o);
    }

    public Order processOrder() {
        if (!queue.isEmpty()) return queue.removeFirst();
        return null;
    }

    public int size() {
        return queue.size();
    }
}