package fashionhub;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class SellerOverview {
    private JFrame frame;
    private JLabel totalOrdersLabel;
    private JLabel totalSalesLabel;
    private JLabel averageOrderLabel;

    public SellerOverview() {
        frame = new JFrame("Sales Overview");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(20, 20));
        frame.getContentPane().setBackground(new Color(240, 242, 245));
        frame.setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(255, 255, 255));
        topPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JButton backButton = createButton("← Back", new Color(220, 53, 69));
        backButton.setPreferredSize(new Dimension(100, 40));
        backButton.addActionListener(e -> {
            frame.dispose();
            Seller.main(null);
        });
        topPanel.add(backButton, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("Sales Overview", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(33, 37, 41));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(new Color(240, 242, 245));
        statsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        OrderStats orderStats = calculateOrderStats();
        double averageOrder = orderStats.totalOrders > 0 ? 
            orderStats.totalSales / orderStats.totalOrders : 0;

        statsPanel.add(createStatCard("Total Orders", String.valueOf(orderStats.totalOrders), new Color(13, 110, 253)));
        statsPanel.add(createStatCard("Total Sales", String.format("%.2f BDT", orderStats.totalSales), new Color(25, 135, 84)));
        statsPanel.add(createStatCard("Average Order", String.format("%.2f BDT", averageOrder), new Color(102, 16, 242)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(240, 242, 245));

        JButton refreshButton = createButton("Refresh Data", new Color(13, 110, 253));
        JButton exportButton = createButton("Export Report", new Color(25, 135, 84));

        refreshButton.addActionListener(e -> {
            frame.dispose();
            new SellerOverview();
        });

        buttonPanel.add(refreshButton);
        buttonPanel.add(exportButton);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(statsPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(color, 2, true),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(108, 117, 125));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(valueLabel);

        return card;
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(150, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }

    private OrderStats calculateOrderStats() {
        int totalOrders = 0;
        double totalSales = 0.0;

        try (BufferedReader reader = new BufferedReader(new FileReader("data/orders.txt"))) {
            String line;
            double totalPrice = 0.0;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                if (line.startsWith("Total Price: ")) {
                    totalPrice = Double.parseDouble(line.substring(13).replace(" BDT", "").trim());
                } else if (line.equals("--------------------------------------")) {
                    totalOrders++;
                    totalSales += totalPrice;
                    totalPrice = 0.0;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new OrderStats(totalOrders, totalSales);
    }

    private static class OrderStats {
        int totalOrders;
        double totalSales;

        OrderStats(int totalOrders, double totalSales) {
            this.totalOrders = totalOrders;
            this.totalSales = totalSales;
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(SellerOverview::new);
    }
}