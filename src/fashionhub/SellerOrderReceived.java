package fashionhub;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SellerOrderReceived extends JFrame {
    private JTextArea orderDetailsArea;
    private JComboBox<String> statusComboBox;
    private JButton updateButton;
    private JLabel currentOrderLabel;
    private int currentOrderIndex = 0;
    private String[] orders;

    public SellerOrderReceived() {
        setTitle("FashionHub - Seller Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(30, 30, 30));
        setLayout(null);
        setLocationRelativeTo(null);

        setupUI();
        loadOrders();
        setVisible(true);
    }

    private void setupUI() {

        JLabel titleLabel = new JLabel("Order Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(50, 20, 700, 50);
        add(titleLabel);

        JButton prevButton = createStyledButton("Previous");
        prevButton.setBounds(50, 90, 120, 40);
        prevButton.setBackground(new Color(0, 123, 255));
        add(prevButton);

        currentOrderLabel = new JLabel("Order 1", SwingConstants.CENTER);
        currentOrderLabel.setFont(new Font("Verdana", Font.BOLD, 16));
        currentOrderLabel.setForeground(Color.WHITE);
        currentOrderLabel.setBounds(200, 90, 400, 40);
        add(currentOrderLabel);

        JButton nextButton = createStyledButton("Next");
        nextButton.setBounds(630, 90, 120, 40);
        nextButton.setBackground(new Color(0, 123, 255));
        add(nextButton);

        orderDetailsArea = new JTextArea();
        orderDetailsArea.setEditable(false);
        orderDetailsArea.setFont(new Font("Verdana", Font.PLAIN, 14));
        orderDetailsArea.setBackground(new Color(40, 40, 40));
        orderDetailsArea.setForeground(Color.WHITE);
        orderDetailsArea.setLineWrap(true);
        orderDetailsArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(orderDetailsArea);
        scrollPane.setBounds(50, 150, 700, 300);
        add(scrollPane);

        JLabel statusLabel = new JLabel("Update Status:");
        statusLabel.setFont(new Font("Verdana", Font.PLAIN, 16));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setBounds(50, 470, 150, 30);
        add(statusLabel);

        String[] statuses = {"Pending", "Processing", "Shipped", "Delivered", "Cancelled"};
        statusComboBox = new JComboBox<>(statuses);
        statusComboBox.setBounds(200, 470, 250, 30);
        statusComboBox.setFont(new Font("Verdana", Font.PLAIN, 14));
        add(statusComboBox);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(460, 470, 300, 30);
        buttonPanel.setLayout(new GridLayout(1, 2, 10, 0));
        buttonPanel.setOpaque(false);

        updateButton = createStyledButton("Update Status");
        updateButton.setBackground(new Color(40, 167, 69));
        buttonPanel.add(updateButton);

        JButton backButton = createStyledButton("Back");
        backButton.setBackground(new Color(220, 53, 69));
        backButton.addActionListener(e -> {
            dispose();
            new Seller();
        });
        buttonPanel.add(backButton);

        add(buttonPanel);

        prevButton.addActionListener(e -> showPreviousOrder());
        nextButton.addActionListener(e -> showNextOrder());
        updateButton.addActionListener(e -> updateOrderStatus());
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Verdana", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void loadOrders() {
        try {
            File file = new File("data/orders.txt");
            if (file.exists()) {
                String content = new String(Files.readAllBytes(file.toPath()));
                orders = content.split("--------------------------------------");
                if (orders.length > 0) {
                    showOrder(0);
                } else {
                    orderDetailsArea.setText("No orders found.");
                }
            } else {
                orderDetailsArea.setText("Orders file not found.");
            }
        } catch (IOException e) {
            orderDetailsArea.setText("Error loading orders: " + e.getMessage());
        }
    }

    private void showOrder(int index) {
        if (orders != null && index >= 0 && index < orders.length) {
            currentOrderIndex = index;
            currentOrderLabel.setText("Order " + (index + 1) + " of " + orders.length);
            orderDetailsArea.setText(orders[index].trim());
            
            try {
                String currentStatus = getCurrentStatus(index);
                if (currentStatus != null) {
                    statusComboBox.setSelectedItem(currentStatus);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getCurrentStatus(int orderIndex) throws IOException {
        File file = new File("data/track.txt");
        if (!file.exists()) {
            return null;
        }
        
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        int currentLine = 0;
        String status = null;
        
        while ((line = reader.readLine()) != null) {
            if (currentLine == orderIndex) {
                String[] parts = line.split(",");
                if (parts.length > 1) {
                    status = parts[1];
                }
                break;
            }
            currentLine++;
        }
        reader.close();
        return status;
    }

    private void updateOrderStatus() {
        String newStatus = (String) statusComboBox.getSelectedItem();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = now.format(formatter);
        
        try {
            updateTrackFile(currentOrderIndex, newStatus, timestamp);
            showSuccessMessage("Order status updated! ^_^");
        } catch (IOException e) {
            showErrorMessage("Error updating status: " + e.getMessage());
        }
    }

    private void updateTrackFile(int orderIndex, String status, String timestamp) throws IOException {
        File file = new File("data/track.txt");
        String[] lines = new String[orders.length];
        
        if (file.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null && i < lines.length) {
                lines[i] = line;
                i++;
            }
            reader.close();
        }
        
        lines[orderIndex] = orderIndex + "," + status + "," + timestamp;
        
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for (String line : lines) {
            if (line != null) {
                writer.write(line);
                writer.newLine();
            }
        }
        writer.close();
    }

    private void showSuccessMessage(String message) {
        JFrame successFrame = new JFrame("Success");
        successFrame.setSize(300, 150);
        successFrame.setLayout(null);
        successFrame.getContentPane().setBackground(new Color(30, 30, 30));
        successFrame.setLocationRelativeTo(this);
        
        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setFont(new Font("Verdana", Font.BOLD, 14));
        messageLabel.setBounds(20, 20, 260, 40);
        
        JButton okButton = createStyledButton("OK");
        okButton.setBackground(new Color(40, 167, 69));
        okButton.setBounds(100, 70, 100, 30);
        okButton.addActionListener(e -> successFrame.dispose());
        
        successFrame.add(messageLabel);
        successFrame.add(okButton);
        successFrame.setVisible(true);
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showNextOrder() {
        if (currentOrderIndex < orders.length - 1) {
            showOrder(currentOrderIndex + 1);
        }
    }

    private void showPreviousOrder() {
        if (currentOrderIndex > 0) {
            showOrder(currentOrderIndex - 1);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SellerOrderReceived());
    }
}