package fashionhub;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class ManageSellerAccounts {

    private static final String PENDING_SELLERS_FILE = "data/pending_sellers.txt";
    private static final String APPROVED_SELLERS_FILE = "data/seller_data.txt";
    private static String[] pendingSellers = new String[10];

    public static void main(String[] args) {
        loadPendingSellers();
        SwingUtilities.invokeLater(ManageSellerAccounts::showManageSellerPage);
    }

    private static void showManageSellerPage() {
        JFrame frame = new JFrame("FashionHub - Manage Seller Accounts");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.getContentPane().setBackground(new Color(30, 30, 30));

        JLabel titleLabel = new JLabel("Manage Seller Accounts", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(50, 20, 500, 50);
        frame.add(titleLabel);

        JPanel sellerPanel = new JPanel();
        sellerPanel.setLayout(new BoxLayout(sellerPanel, BoxLayout.Y_AXIS));
        sellerPanel.setBackground(new Color(30, 30, 30));

        JScrollPane scrollPane = new JScrollPane(sellerPanel);
        scrollPane.setBounds(50, 100, 500, 300);
        frame.add(scrollPane);

        for (int i = 0; i < pendingSellers.length; i++) {
            if (pendingSellers[i] != null) {
                JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
                row.setBackground(new Color(40, 40, 40));
                row.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                JLabel nameLabel = new JLabel(pendingSellers[i]);
                nameLabel.setFont(new Font("Verdana", Font.PLAIN, 16));
                nameLabel.setForeground(Color.WHITE);

                JButton approveButton = new JButton("Approve");
                approveButton.setFont(new Font("Verdana", Font.PLAIN, 12));
                approveButton.setBackground(new Color(0, 200, 0));
                approveButton.setForeground(Color.WHITE);

                int index = i;
                approveButton.addActionListener(e -> {
                    approveSeller(index);
                    sellerPanel.remove(row);
                    sellerPanel.revalidate();
                    sellerPanel.repaint();
                });

                JButton rejectButton = new JButton("Reject");
                rejectButton.setFont(new Font("Verdana", Font.PLAIN, 12));
                rejectButton.setBackground(new Color(200, 0, 0));
                rejectButton.setForeground(Color.WHITE);
                rejectButton.addActionListener(e -> {
                    rejectSeller(index);
                    sellerPanel.remove(row);
                    sellerPanel.revalidate();
                    sellerPanel.repaint();
                });

                row.add(nameLabel);
                row.add(approveButton);
                row.add(rejectButton);
                sellerPanel.add(row);
            }
        }

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Verdana", Font.BOLD, 14));
        backButton.setBackground(new Color(50, 50, 50));
        backButton.setForeground(Color.WHITE);
        backButton.setBounds(50, 420, 100, 30);
        backButton.addActionListener(e -> {
            frame.dispose();
            Admin.main(null);
        });
        frame.add(backButton);

        frame.setVisible(true);
    }

    private static void loadPendingSellers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(PENDING_SELLERS_FILE))) {
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null && i < pendingSellers.length) {
                pendingSellers[i++] = line.trim();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading pending sellers file: " + e.getMessage());
        }
    }

    private static void savePendingSellers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PENDING_SELLERS_FILE))) {
            for (String seller : pendingSellers) {
                if (seller != null) {
                    writer.write(seller);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving pending sellers: " + e.getMessage());
        }
    }

    private static void approveSeller(int index) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(APPROVED_SELLERS_FILE, true))) {
            if (pendingSellers[index] != null) {
                writer.write(pendingSellers[index]);
                writer.newLine();
                pendingSellers[index] = null;
                savePendingSellers();
                JOptionPane.showMessageDialog(null, "Seller approved!");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error approving seller: " + e.getMessage());
        }
    }

    private static void rejectSeller(int index) {
        if (pendingSellers[index] != null) {
            pendingSellers[index] = null;
            savePendingSellers();
            JOptionPane.showMessageDialog(null, "Seller rejected!");
        }
    }
}
