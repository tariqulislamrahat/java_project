package fashionhub;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Cart extends JFrame {
    private static Cart cartInstance;
    private JButton checkoutButton, removeButton;
    private JList<String> cartList;
    private DefaultListModel<String> cartModel;
    private JLabel totalLabel;

    private Cart() {
        setTitle("Your Shopping Cart");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(30, 30, 30));

        JLabel titleLabel = new JLabel("Your Shopping Cart", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, BorderLayout.NORTH);

        cartModel = new DefaultListModel<>();
        cartList = new JList<>(cartModel);
        cartList.setFont(new Font("Arial", Font.PLAIN, 14));
        cartList.setBackground(new Color(40, 40, 40));
        cartList.setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(cartList);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(30, 30, 30));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        add(bottomPanel, BorderLayout.SOUTH);

        totalLabel = new JLabel("Total: BDT 0.00");
        totalLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        totalLabel.setForeground(Color.WHITE);
        bottomPanel.add(totalLabel);

        removeButton = new JButton("Remove Item");
        removeButton.setFont(new Font("Arial", Font.PLAIN, 14));
        removeButton.setBackground(new Color(220, 53, 69));
        removeButton.setForeground(Color.WHITE);
        removeButton.setFocusPainted(false);
        removeButton.addActionListener(e -> removeItemFromCart());
        bottomPanel.add(removeButton);

        checkoutButton = new JButton("Checkout");
        checkoutButton.setFont(new Font("Arial", Font.BOLD, 16));
        checkoutButton.setForeground(Color.WHITE);
        checkoutButton.setBackground(new Color(0, 128, 0));
        checkoutButton.setFocusPainted(false);
        checkoutButton.addActionListener(e -> proceedToCheckout());
        bottomPanel.add(checkoutButton);

        loadCartFromFile();
    }

    public static Cart getInstance() {
        if (cartInstance == null) {
            cartInstance = new Cart();
        }
        return cartInstance;
    }

    public void addItemToCart(String productName, String productPrice) {
        cartModel.addElement(productName + " - BDT " + productPrice);
        updateTotal();
        saveCartToFile();
    }

    private void updateTotal() {
        double total = 0.0;
        for (int i = 0; i < cartModel.size(); i++) {
            String item = cartModel.getElementAt(i);
            String[] parts = item.split(" - BDT ");
            if (parts.length >= 2) {
                try {
                    total += Double.parseDouble(parts[1].trim());
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing price: " + parts[1]);
                }
            }
        }
        totalLabel.setText(String.format("Total: BDT %.2f", total));
    }

    private void loadCartFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("data/cart.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                cartModel.addElement(line);
                System.out.println("Loaded item: " + line);
            }
        } catch (IOException e) {
            System.err.println("Error loading cart: " + e.getMessage());
        }
        updateTotal();
    }

    private void saveCartToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/cart.txt"))) {
            for (int i = 0; i < cartModel.size(); i++) {
                writer.write(cartModel.getElementAt(i) + "\n");
            }
            System.out.println("Cart saved to file.");
        } catch (IOException e) {
            System.err.println("Error saving cart: " + e.getMessage());
        }
    }

    private void removeItemFromCart() {
        int selectedIndex = cartList.getSelectedIndex();
        if (selectedIndex != -1) {
            cartModel.remove(selectedIndex);
            updateTotal();
            saveCartToFile();
        } else {
            JOptionPane.showMessageDialog(this, "Select an item to remove.");
        }
    }

    private void proceedToCheckout() {
        if (cartModel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty. Add items to proceed to checkout.");
            return;
        }

        double total = 0.0;
        for (int i = 0; i < cartModel.size(); i++) {
            String item = cartModel.getElementAt(i);
            String[] parts = item.split(" - BDT ");
            if (parts.length >= 2) {
                try {
                    total += Double.parseDouble(parts[1].trim());
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing price: " + parts[1]);
                }
            }
        }

        double deliveryCharge = 50.0;
        double totalCost = total + deliveryCharge;

        StringBuilder productList = new StringBuilder();
        for (int i = 0; i < cartModel.size(); i++) {
            productList.append(cartModel.getElementAt(i)).append("\n");
        }

        new Checkout(productList.toString(), String.format("%.2f", total), String.format("%.2f", deliveryCharge)).setVisible(true);
        cartModel.clear();
        updateTotal();
        saveCartToFile();
        dispose();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> Cart.getInstance().setVisible(true));
    }
}
