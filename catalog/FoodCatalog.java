package catalog;

import model.FoodItem;

public class FoodCatalog {
    public FoodItem[] items = new FoodItem[50];
    public int count = 0;

    public void addItem(FoodItem item) {
        items[count++] = item;
    }

    public String display() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(items[i].id + ". " + items[i]).append("\n");
        }
        return sb.toString();
    }
}