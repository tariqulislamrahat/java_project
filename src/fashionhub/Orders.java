package fashionhub;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Orders extends JFrame {
    private JPanel mainPanel;
    private JScrollPane scrollPane;

    public Orders() {
        setTitle("Order History");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(30, 30, 30));

        JLabel titleLabel = new JLabel("Order History", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(50, 30, 700, 50);
        add(titleLabel);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(30, 30, 30));

        scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBounds(50, 100, 700, 450);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(new Color(30, 30, 30));
        add(scrollPane);

        loadOrders();
        setVisible(true);
    }

    private void loadOrders() {
        try (BufferedReader ordersReader = new BufferedReader(new FileReader("data/orders.txt"))) {
            StringBuilder orderBuilder = new StringBuilder();
            String line;

            while ((line = ordersReader.readLine()) != null) {
                if (line.startsWith("--------------------------------------")) {
                    if (orderBuilder.length() > 0) {
                        String orderDetails = orderBuilder.toString();
                        JPanel orderPanel = createOrderPanel(orderDetails);
                        mainPanel.add(orderPanel);
                        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                        orderBuilder.setLength(0);
                    }
                } else {
                    orderBuilder.append(line).append("\n");
                }
            }

            if (orderBuilder.length() > 0) {
                String orderDetails = orderBuilder.toString();
                JPanel orderPanel = createOrderPanel(orderDetails);
                mainPanel.add(orderPanel);
            }
        } catch (IOException e) {
            System.err.println("Error reading orders file: " + e.getMessage());
        }
    }

    private JPanel createOrderPanel(String orderDetails) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 5, 5));
        panel.setBackground(new Color(60, 60, 60));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] details = orderDetails.split("\n");
        for (String detail : details) {
            if (!detail.trim().isEmpty()) {
                JLabel label = new JLabel(detail);
                label.setFont(new Font("Verdana", Font.PLAIN, 14));
                label.setForeground(Color.WHITE);
                panel.add(label);
            }
        }

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Orders());
    }
}