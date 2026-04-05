package search;

import model.FoodItem;

public class BST {
    private BSTNode root;

    public void insert(FoodItem item) {
        root = insertRec(root, item);
    }

    private BSTNode insertRec(BSTNode root, FoodItem item) {
        if (root == null) return new BSTNode(item);

        if (item.price < root.data.price)
            root.left = insertRec(root.left, item);
        else
            root.right = insertRec(root.right, item);

        return root;
    }

    public FoodItem search(int price) {
        return searchRec(root, price);
    }

    private FoodItem searchRec(BSTNode root, int price) {
        if (root == null) return null;
        if (root.data.price == price) return root.data;

        if (price < root.data.price)
            return searchRec(root.left, price);
        else
            return searchRec(root.right, price);
    }
}