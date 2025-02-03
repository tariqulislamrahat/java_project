package fashionhub;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class Track extends JFrame {
    private JPanel mainPanel;

    public Track() {
        setTitle("Track Order");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initializeUI();
        loadOrderStatus();
    }

    private void initializeUI() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(30, 30, 30));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Track Your Orders");
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50)));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(new Color(30, 30, 30));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadOrderStatus() {
        Map<Integer, String> orderNames = new HashMap<>();
        try (BufferedReader ordersReader = new BufferedReader(new FileReader("data/orders.txt"))) {
            String line;
            int orderIndex = 0;
            while ((line = ordersReader.readLine()) != null) {
                if (line.startsWith("Product:")) {
                    StringBuilder products = new StringBuilder();
                    String[] productDetails = line.substring("Product:".length()).trim().split("-");
                    products.append(productDetails[0].trim());

                    while ((line = ordersReader.readLine()) != null && !line.startsWith("Price:")) {
                        if (line.startsWith("Product:")) {
                            productDetails = line.substring("Product:".length()).trim().split("-");
                            products.append(", ").append(productDetails[0].trim());
                        }
                    }

                    orderNames.put(orderIndex++, products.toString());
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading order details. Please try again later.", "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error reading orders file: " + e.getMessage());
            return;
        }

        Map<Integer, String> orderStatuses = new HashMap<>();
        try (BufferedReader trackReader = new BufferedReader(new FileReader("data/track.txt"))) {
            String line;
            while ((line = trackReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    int index = Integer.parseInt(parts[0].trim());
                    String status = parts[1].trim();
                    orderStatuses.put(index, status);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading order statuses. Please try again later.", "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error reading track file: " + e.getMessage());
            return;
        }

        for (int i = 0; i < orderNames.size(); i++) {
            String productName = orderNames.get(i);
            String status = orderStatuses.getOrDefault(i, "Status not available");

            JPanel orderPanel = new JPanel();
            orderPanel.setLayout(new BoxLayout(orderPanel, BoxLayout.Y_AXIS));
            orderPanel.setBackground(new Color(40, 40, 40));
            orderPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
            orderPanel.setMaximumSize(new Dimension(500, 80));
            orderPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            namePanel.setBackground(new Color(40, 40, 40));
            JLabel orderLabel = new JLabel(productName);
            orderLabel.setFont(new Font("Verdana", Font.BOLD, 14));
            orderLabel.setForeground(Color.WHITE);
            namePanel.add(orderLabel);

            JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            statusPanel.setBackground(new Color(40, 40, 40));
            JLabel statusLabel = new JLabel(status);
            statusLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
            statusLabel.setForeground(new Color(220, 53, 69));
            statusPanel.add(statusLabel);

            orderPanel.add(namePanel);
            orderPanel.add(statusPanel);

            mainPanel.add(orderPanel);
            mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Track().setVisible(true));
    }
}
