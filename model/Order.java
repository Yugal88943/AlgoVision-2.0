package model;

public class Order {
    public int orderId;
    public FoodItem item;
    public User user;

    public Order(int orderId, FoodItem item, User user) {
        this.orderId = orderId;
        this.item = item;
        this.user = user;
    }

    public String toString() {
        return "Order#" + orderId + " - " + item.name + " (" + user.name + ")";
    }
}