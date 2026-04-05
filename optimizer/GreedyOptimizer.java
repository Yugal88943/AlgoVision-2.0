package optimizer;

import model.FoodItem;

public class GreedyOptimizer {

    public FoodItem getCheapest(FoodItem[] items, int count) {
        if (count == 0) return null;

        FoodItem min = items[0];
        for (int i = 1; i < count; i++) {
            if (items[i].price < min.price)
                min = items[i];
        }
        return min;
    }
}