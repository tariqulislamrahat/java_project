package fashionhub;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Payment extends JFrame {
    private JLabel paymentMethodLabel, phoneLabel, codeLabel, pinLabel, titleLabel, totalCostLabel;
    private JTextField phoneField, codeField;
    private JPasswordField pinField;
    private JButton payButton, sendCodeButton;

    public Payment(String productList, String productPrice, String userName, String address, String email, String phone, double totalCost) {
        setTitle("Payment");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(30, 30, 30));
        setLocationRelativeTo(null);

        titleLabel = new JLabel("Payment Details", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(50, 30, 500, 50);
        add(titleLabel);

        totalCostLabel = new JLabel("Total Cost: " + totalCost + " BDT", SwingConstants.CENTER);
        totalCostLabel.setFont(new Font("Verdana", Font.PLAIN, 16));
        totalCostLabel.setForeground(Color.WHITE);
        totalCostLabel.setBounds(120, 90, 360, 30);
        add(totalCostLabel);

        paymentMethodLabel = new JLabel("Select Payment Method:");
        paymentMethodLabel.setFont(new Font("Verdana", Font.PLAIN, 16));
        paymentMethodLabel.setForeground(Color.WHITE);
        paymentMethodLabel.setBounds(120, 140, 200, 30);
        add(paymentMethodLabel);

        String[] methods = {"Bkash", "Rocket", "Nagad"};
        JComboBox<String> methodDropdown = new JComboBox<>(methods);
        methodDropdown.setBounds(320, 140, 170, 30);
        methodDropdown.setFont(new Font("Verdana", Font.PLAIN, 14));
        add(methodDropdown);

        phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setFont(new Font("Verdana", Font.PLAIN, 16));
        phoneLabel.setForeground(Color.WHITE);
        phoneLabel.setBounds(120, 190, 200, 30);
        add(phoneLabel);

        phoneField = new JTextField();
        phoneField.setBounds(320, 190, 170, 30);
        phoneField.setFont(new Font("Verdana", Font.PLAIN, 14));
        add(phoneField);

        sendCodeButton = new JButton("Send Code");
        sendCodeButton.setBounds(320, 230, 170, 30);
        sendCodeButton.setFont(new Font("Verdana", Font.BOLD, 12));
        sendCodeButton.setBackground(new Color(40, 167, 69));
        sendCodeButton.setForeground(Color.WHITE);
        sendCodeButton.addActionListener(e -> {
            String phoneNum = phoneField.getText();
            if (validatePhoneNumber(phoneNum)) {
                JOptionPane.showMessageDialog(Payment.this, "Code sent to " + phoneNum);
            } else {
                JOptionPane.showMessageDialog(Payment.this, "Invalid phone number. Please enter a valid phone number.");
            }
        });
        add(sendCodeButton);

        codeLabel = new JLabel("Transaction Code:");
        codeLabel.setFont(new Font("Verdana", Font.PLAIN, 16));
        codeLabel.setForeground(Color.WHITE);
        codeLabel.setBounds(120, 280, 200, 30);
        add(codeLabel);

        codeField = new JTextField();
        codeField.setBounds(320, 280, 170, 30);
        codeField.setFont(new Font("Verdana", Font.PLAIN, 14));
        add(codeField);

        pinLabel = new JLabel("PIN:");
        pinLabel.setFont(new Font("Verdana", Font.PLAIN, 16));
        pinLabel.setForeground(Color.WHITE);
        pinLabel.setBounds(120, 330, 200, 30);
        add(pinLabel);

        pinField = new JPasswordField();
        pinField.setBounds(320, 330, 170, 30);
        pinField.setFont(new Font("Verdana", Font.PLAIN, 14));
        add(pinField);

        payButton = new JButton("Pay Now");
        payButton.setBounds(250, 400, 120, 40);
        payButton.setFont(new Font("Verdana", Font.BOLD, 14));
        payButton.setBackground(new Color(0, 123, 255));
        payButton.setForeground(Color.WHITE);
        payButton.addActionListener(e -> {
            String paymentMethod = (String) methodDropdown.getSelectedItem();
            String phoneNum = phoneField.getText();
            String code = codeField.getText();
            String pin = new String(pinField.getPassword());

            if (phoneNum.matches("\\d+") || code.matches("\\d+") || pin.matches("\\d+")) {
                JOptionPane.showMessageDialog(Payment.this, "Payment Successful via " + paymentMethod + "!");
                saveOrderToFile(productList, productPrice, userName, address, email, phone, totalCost, paymentMethod);
                clearFields();
                dispose();
                new Buyer().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(Payment.this, "Please fill in all payment details correctly.");
            }
        });
        add(payButton);

        setVisible(true);
    }

    private boolean validatePhoneNumber(String phone) {
    
        return phone != null && phone.matches("01\\d{9}");
    }

    private void clearFields() {
        phoneField.setText("");
        codeField.setText("");
        pinField.setText("");
    }

    private void saveOrderToFile(String productList, String productPrice, String userName, String address, String email, String phone, double totalCost, String paymentMethod) {
        String orderDetails = "Product: " + productList + "\n" +
                            "Price: " + productPrice + " BDT\n" +
                            "Name: " + userName + "\n" +
                            "Address: " + address + "\n" +
                            "Email: " + email + "\n" +
                            "Phone: " + phone + "\n" +
                            "Total Price: " + totalCost + " BDT\n" +
                            "Payment Method: " + paymentMethod + "\n" +
                            "--------------------------------------\n";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/orders.txt", true))) {
            writer.write(orderDetails);
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving order to file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Payment("Sample Product", "1000", "John Doe", "123 Street", "john.doe@example.com", "+880123456789", 1050);
    }
}