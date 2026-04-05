package model;

public class FoodItem {
    public int id;
    public String name;
    public int price;
    public String category; 

    public FoodItem(int id, String name, int price, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String toString() {
        return name + " (" + category + ") - ₹" + price;
    }
}