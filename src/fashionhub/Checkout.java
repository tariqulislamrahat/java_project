package fashionhub;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Checkout extends JFrame {
    private JLabel productNameLabel, productPriceLabel, totalPriceLabel;
    private JTextField userNameField, addressField, emailField, phoneField;
    private JButton checkoutButton;

    public Checkout(String productName, String productPrice, String deliveryCharge) {
        setTitle("Checkout");
        setSize(800, 600);
        setLayout(null);
        getContentPane().setBackground(new Color(30, 30, 30));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Checkout", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(0, 10, 500, 30);
        add(titleLabel);

        productNameLabel = new JLabel("<html>Products:<br>" + productName.replace("\n", "<br>") + "</html>", SwingConstants.LEFT);
        productNameLabel.setForeground(Color.WHITE);
        productNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        productNameLabel.setBounds(50, 40, 400, 100);
        add(productNameLabel);

        productPriceLabel = new JLabel("Subtotal: " + productPrice + " BDT", SwingConstants.LEFT);
        productPriceLabel.setForeground(Color.WHITE);
        productPriceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        productPriceLabel.setBounds(50, 150, 400, 30);
        add(productPriceLabel);

        JLabel userNameLabel = new JLabel("Name:");
        userNameLabel.setForeground(Color.WHITE);
        userNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userNameLabel.setBounds(50, 190, 100, 30);
        add(userNameLabel);

        userNameField = new JTextField();
        userNameField.setBounds(150, 190, 300, 30);
        userNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        add(userNameField);

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setForeground(Color.WHITE);
        addressLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        addressLabel.setBounds(50, 230, 100, 30);
        add(addressLabel);

        addressField = new JTextField();
        addressField.setBounds(150, 230, 300, 30);
        addressField.setFont(new Font("Arial", Font.PLAIN, 14));
        add(addressField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emailLabel.setBounds(50, 270, 100, 30);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(150, 270, 300, 30);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        add(emailField);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setForeground(Color.WHITE);
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        phoneLabel.setBounds(50, 310, 100, 30);
        add(phoneLabel);

        phoneField = new JTextField();
        phoneField.setBounds(150, 310, 300, 30);
        phoneField.setFont(new Font("Arial", Font.PLAIN, 14));
        add(phoneField);

        double productCost = 0, deliveryCost = 0;
        try {
            productCost = Double.parseDouble(productPrice);
            deliveryCost = Double.parseDouble(deliveryCharge);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Invalid price format!");
        }

        double totalCost = productCost + deliveryCost;
        totalPriceLabel = new JLabel("Delivery: 50 BDT", SwingConstants.LEFT);
        totalPriceLabel.setForeground(Color.WHITE);
        totalPriceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        totalPriceLabel.setBounds(50, 350, 400, 30);
        add(totalPriceLabel);

        JLabel totalCostLabel = new JLabel("Total Price: " + totalCost + " BDT", SwingConstants.LEFT);
        totalCostLabel.setForeground(Color.WHITE);
        totalCostLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        totalCostLabel.setBounds(50, 390, 400, 30);
        add(totalCostLabel);

        checkoutButton = new JButton("Proceed to Payment");
        checkoutButton.setBounds(150, 430, 200, 40);
        checkoutButton.setBackground(new Color(0, 123, 255));
        checkoutButton.setForeground(Color.WHITE);
        checkoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        checkoutButton.setFocusPainted(false);
        checkoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String userName = userNameField.getText();
                String address = addressField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();

                if (userName.isEmpty() || address.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                    JOptionPane.showMessageDialog(Checkout.this, "Please fill in all fields to proceed.");
                } else {
                    new Payment(productName, productPrice, userName, address, email, phone, totalCost);
                    dispose();
                }
            }
        });
        add(checkoutButton);

        setVisible(true);
    }

    public static void main(String[] args) {
        new Checkout("Sample Product", "1000", "50");
    }
}
