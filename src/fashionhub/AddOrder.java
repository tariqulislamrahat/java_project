package fashionhub;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

public class AddOrder {
    private JFrame frame;
    private JTextField productNameField, priceField;
    private JButton addOrderButton, cancelButton;
    private Seller seller;
    public AddOrder(Seller seller) {
        this.seller = seller;
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Add Prodcut");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);
        frame.getContentPane().setBackground(new Color(30, 30, 30));

        JLabel titleLabel = new JLabel("Add New Product", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(50, 30, 500, 50);
        frame.add(titleLabel);

        JLabel productNameLabel = new JLabel("Product Name:");
        productNameLabel.setFont(new Font("Verdana", Font.PLAIN, 16));
        productNameLabel.setForeground(Color.WHITE);
        productNameLabel.setBounds(50, 100, 150, 30);
        frame.add(productNameLabel);

        productNameField = new JTextField();
        productNameField.setBounds(220, 100, 300, 30);
        productNameField.setFont(new Font("Verdana", Font.PLAIN, 14));
        frame.add(productNameField);

        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setFont(new Font("Verdana", Font.PLAIN, 16));
        priceLabel.setForeground(Color.WHITE);
        priceLabel.setBounds(50, 160, 150, 30);
        frame.add(priceLabel);

        priceField = new JTextField();
        priceField.setBounds(220, 160, 300, 30);
        priceField.setFont(new Font("Verdana", Font.PLAIN, 14));
        frame.add(priceField);

        addOrderButton = new JButton("Add Product");
        addOrderButton.setBounds(150, 250, 150, 40);
        addOrderButton.setFont(new Font("Verdana", Font.BOLD, 14));
        addOrderButton.setBackground(new Color(0, 123, 255));
        addOrderButton.setForeground(Color.WHITE);
        addOrderButton.setFocusPainted(false);
        addOrderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });
        frame.add(addOrderButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(320, 250, 120, 40);
        cancelButton.setFont(new Font("Verdana", Font.BOLD, 14));
        cancelButton.setBackground(new Color(220, 53, 69));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        frame.add(cancelButton);

        frame.setVisible(true);
    }

    private void addProduct() {
        String productName = productNameField.getText().trim();
        String price = priceField.getText().trim();

        if (productName.isEmpty() || price.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            
            double priceValue = Double.parseDouble(price);
            if (priceValue <= 0) {
                JOptionPane.showMessageDialog(frame, "Price must be greater than 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

           
            saveProductToFile(productName, price);
            JOptionPane.showMessageDialog(frame, "Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            
            seller.updateProductCards();
            frame.dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid price. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveProductToFile(String productName, String price) {
        try (FileWriter fw = new FileWriter("data/products.txt", true)) {
            fw.write(productName + "," + price + "\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error saving product: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}