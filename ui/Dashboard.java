package ui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import model.*;
import catalog.*;
import ingestion.*;
import data.*;
import optimizer.*;
import io.*;

import java.awt.*;
import java.util.*;

public class Dashboard extends JFrame {

    FoodCatalog catalog = new FoodCatalog();
    DataStore store = new DataStore();
    OrderQueue queue = new OrderQueue();
    GreedyOptimizer optimizer = new GreedyOptimizer();

    
    HashMap<Integer, String> orderStatus = new HashMap<>();

    JPanel contentPanel = new JPanel(new BorderLayout());
    boolean loaded = false;

    public Dashboard() {

        setTitle("AlgoVision 2.0 - Smart Food System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("AlgoVision Dashboard", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        add(title, BorderLayout.NORTH);

        JPanel left = new JPanel(new GridLayout(6, 1, 10, 10));
        left.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        left.setBackground(new Color(30, 30, 30));

        JButton viewMenu = createButton("View Menu");
        JButton placeOrder = createButton("Place Order");
        JButton search = createButton("Search");
        JButton viewOrders = createButton("Orders");
        JButton process = createButton("Process");
        JButton analytics = createButton("Analytics");

        JButton[] buttons = {
                viewMenu, placeOrder, search,
                viewOrders, process, analytics
        };

        for (JButton b : buttons) left.add(b);
        add(left, BorderLayout.WEST);

        contentPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(contentPanel, BorderLayout.CENTER);

        if (!loaded) {
            FileManager.loadFood(catalog);
            loaded = true;
        }

        showWelcome();

        viewMenu.addActionListener(e -> showMenuWithFilters());
        placeOrder.addActionListener(e -> placeOrder());
        viewOrders.addActionListener(e -> showOrdersUI());
        analytics.addActionListener(e -> showAnalyticsUI());

        process.addActionListener(e -> {
            Order o = queue.processOrder();

            if (o == null) {
                JOptionPane.showMessageDialog(this, "No orders to process");
            } else {
                orderStatus.put(o.hashCode(), "Processed");
                showOrdersUI();
            }
        });

        search.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Enter food name:");
            if (input != null && !input.trim().isEmpty())
                showSearchResults(input);
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(50, 50, 50));
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        return btn;
    }

    private void showWelcome() {
        JLabel label = new JLabel(
                "<html><center><h1>Welcome</h1><p>Smart Food Ordering System</p></center></html>",
                JLabel.CENTER
        );
        contentPanel.removeAll();
        contentPanel.add(label);
        refresh();
    }

    private JPanel createFoodCard(FoodItem item) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10,10,10,10)
        ));

        JLabel id = new JLabel("ID: " + item.id);
        JLabel name = new JLabel(item.name);
        JLabel category = new JLabel("Category: " + item.category);
        JLabel price = new JLabel("Price: " + item.price);

        name.setFont(new Font("Segoe UI", Font.BOLD, 16));
        id.setForeground(Color.GRAY);

        card.add(id);
        card.add(name);
        card.add(Box.createVerticalStrut(5));
        card.add(category);
        card.add(price);

        return card;
    }

    private JPanel createOrderCard(Order order) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10,10,10,10)
        ));

        String status = orderStatus.getOrDefault(order.hashCode(), "Pending");

        JLabel id = new JLabel("Order ID: " + order.hashCode());
        JLabel user = new JLabel("User: " + order.user.name);
        JLabel item = new JLabel("Item: " + order.item.name);
        JLabel price = new JLabel("Price: " + order.item.price);
        JLabel statusLabel = new JLabel("Status: " + status);

        if (status.equals("Processed"))
            statusLabel.setForeground(new Color(0,128,0));
        else
            statusLabel.setForeground(new Color(200,120,0));

        item.setFont(new Font("Segoe UI", Font.BOLD, 15));
        id.setForeground(Color.GRAY);

        card.add(id);
        card.add(Box.createVerticalStrut(5));
        card.add(user);
        card.add(item);
        card.add(price);
        card.add(statusLabel);

        return card;
    }

    private JScrollPane createSmoothScroll(Component comp) {
        JScrollPane scroll = new JScrollPane(comp);
        scroll.getVerticalScrollBar().setUnitIncrement(25);
        scroll.getVerticalScrollBar().setBlockIncrement(60);

        scroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(120, 120, 120);
            }
        });

        return scroll;
    }

    private void showFoodCards(FoodItem[] items) {

        JPanel grid = new JPanel(new GridLayout(0, 3, 20, 20));
        grid.setBackground(new Color(245,245,245));

        for (FoodItem item : items)
            if (item != null)
                grid.add(createFoodCard(item));

        contentPanel.removeAll();
        contentPanel.add(createSmoothScroll(grid));
        refresh();
    }

    private JPanel createFilterPanel() {

        JPanel top = new JPanel();

        JButton all = new JButton("All");
        JButton veg = new JButton("Veg");
        JButton nonVeg = new JButton("NonVeg");
        JButton drinks = new JButton("Drinks");
        JButton cheap = new JButton("Cheapest");

        top.add(all);
        top.add(veg);
        top.add(nonVeg);
        top.add(drinks);
        top.add(cheap);

        all.addActionListener(e -> showMenuWithFilters());
        veg.addActionListener(e -> filter("Veg"));
        nonVeg.addActionListener(e -> filter("NonVeg"));
        drinks.addActionListener(e -> filter("Drinks"));
        cheap.addActionListener(e -> showCheapest());

        return top;
    }

    private void showMenuWithFilters() {

        JPanel top = createFilterPanel();
        JPanel grid = new JPanel(new GridLayout(0, 3, 20, 20));

        for (int i = 0; i < catalog.count; i++)
            grid.add(createFoodCard(catalog.items[i]));

        contentPanel.removeAll();
        contentPanel.add(top, BorderLayout.NORTH);
        contentPanel.add(createSmoothScroll(grid), BorderLayout.CENTER);

        refresh();
    }

    private void filter(String category) {

        JPanel top = createFilterPanel();
        JPanel grid = new JPanel(new GridLayout(0, 3, 20, 20));

        for (int i = 0; i < catalog.count; i++) {
            if (catalog.items[i].category.equalsIgnoreCase(category)) {
                grid.add(createFoodCard(catalog.items[i]));
            }
        }

        contentPanel.removeAll();
        contentPanel.add(top, BorderLayout.NORTH);
        contentPanel.add(createSmoothScroll(grid), BorderLayout.CENTER);

        refresh();
    }

    private void showCheapest() {

        JPanel top = createFilterPanel();
        JPanel grid = new JPanel(new GridLayout(0, 3, 20, 20));

        FoodItem f = optimizer.getCheapest(catalog.items, catalog.count);
        if (f != null)
            grid.add(createFoodCard(f));

        contentPanel.removeAll();
        contentPanel.add(top, BorderLayout.NORTH);
        contentPanel.add(createSmoothScroll(grid), BorderLayout.CENTER);

        refresh();
    }

    private void showOrdersUI() {

        if (store.orders.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No orders yet");
            return;
        }

        JPanel grid = new JPanel(new GridLayout(0, 3, 20, 20));
        grid.setBackground(new Color(245,245,245));

        for (Order o : store.orders.values())
            grid.add(createOrderCard(o));

        contentPanel.removeAll();
        contentPanel.add(createSmoothScroll(grid));
        refresh();
    }

    private void showAnalyticsUI() {

        if (store.orders.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No data available");
            return;
        }

        HashMap<String, Integer> freq = new HashMap<>();

        for (Order o : store.orders.values()) {
            String name = o.item.name;
            freq.put(name, freq.getOrDefault(name, 0) + 1);
        }

        java.util.List<Map.Entry<String, Integer>> list = new ArrayList<>(freq.entrySet());
        list.sort((a, b) -> b.getValue() - a.getValue());

        JPanel grid = new JPanel(new GridLayout(0, 2, 20, 20));
        grid.setBackground(new Color(245,245,245));

        grid.add(createStatCard("Total Orders", String.valueOf(store.orders.size())));
        grid.add(createStatCard("Unique Items", String.valueOf(freq.size())));

        int limit = Math.min(3, list.size());
        for (int i = 0; i < limit; i++) {
            Map.Entry<String, Integer> e = list.get(i);
            grid.add(createStatCard("Top Item", e.getKey() + " (" + e.getValue() + ")"));
        }

        contentPanel.removeAll();
        contentPanel.add(grid);
        refresh();
    }

    private JPanel createStatCard(String title, String value) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(20,20,20,20)
        ));

        JLabel t = new JLabel(title);
        JLabel v = new JLabel(value);

        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        v.setFont(new Font("Segoe UI", Font.BOLD, 18));

        card.add(t);
        card.add(Box.createVerticalStrut(10));
        card.add(v);

        return card;
    }

    private void placeOrder() {
        try {
            String input = JOptionPane.showInputDialog("Enter Food ID:");
            if (input == null) return;

            int id = Integer.parseInt(input);

            if (id <= 0 || id > catalog.count) {
                JOptionPane.showMessageDialog(this, "Invalid Food ID");
                return;
            }

            String name = JOptionPane.showInputDialog("Enter Your Name:");
            if (name == null || name.isEmpty()) return;

            User user = new User(store.users.size() + 1, name);
            store.addUser(user);

            FoodItem item = catalog.items[id - 1];
            Order order = new Order(store.orders.size() + 1, item, user);

            queue.addOrder(order);
            store.addOrder(order);

            orderStatus.put(order.hashCode(), "Pending");

            FileManager.saveOrders(store.orders.values());

            showOrdersUI();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid Input");
        }
    }

    private void showSearchResults(String input) {

        java.util.List<FoodItem> results = new ArrayList<>();

        for (int i = 0; i < catalog.count; i++)
            if (catalog.items[i].name.toLowerCase().contains(input.toLowerCase()))
                results.add(catalog.items[i]);

        if (results.isEmpty())
            JOptionPane.showMessageDialog(this, "No items found");
        else
            showFoodCards(results.toArray(new FoodItem[0]));
    }

    private void refresh() {
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}