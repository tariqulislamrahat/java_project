package fashionhub;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class DeleteAccounts {

    private static final String APPROVED_SELLERS_FILE = "data/seller_data.txt";
    private static final String BUYER_DATA_FILE = "data/buyer_data.txt";
    private static String[] approvedSellers = new String[10];
    private static String[] buyers = new String[10];

    public static void main(String[] args) {
        loadAccounts(APPROVED_SELLERS_FILE, approvedSellers);
        loadAccounts(BUYER_DATA_FILE, buyers);
        SwingUtilities.invokeLater(DeleteAccounts::showDeleteAccountsPage);
    }

    private static void showDeleteAccountsPage() {
        JFrame frame = new JFrame("FashionHub - Delete Accounts");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.getContentPane().setBackground(new Color(30, 30, 30));

        JLabel titleLabel = new JLabel("Delete Accounts", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(50, 20, 800, 50);
        frame.add(titleLabel);

        JTextField searchField = new JTextField();
        searchField.setFont(new Font("Verdana", Font.PLAIN, 14));
        searchField.setBounds(50, 80, 500, 30);
        frame.add(searchField);

        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("Verdana", Font.BOLD, 14));
        searchButton.setBackground(new Color(0, 123, 255));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBounds(570, 80, 100, 30);
        frame.add(searchButton);

        JButton cancelSearchButton = new JButton("X");
        cancelSearchButton.setFont(new Font("Verdana", Font.BOLD, 14));
        cancelSearchButton.setBackground(new Color(255, 0, 0));
        cancelSearchButton.setForeground(Color.WHITE);
        cancelSearchButton.setBounds(680, 80, 50, 30);
        frame.add(cancelSearchButton);

        JPanel sellersPanel = new JPanel();
        sellersPanel.setLayout(new BoxLayout(sellersPanel, BoxLayout.Y_AXIS));
        sellersPanel.setBackground(new Color(30, 30, 30));

        JScrollPane sellersScrollPane = new JScrollPane(sellersPanel);
        sellersScrollPane.setBounds(50, 130, 400, 380);
        sellersScrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE, 1),
            "Sellers",
            0,
            0,
            new Font("Verdana", Font.BOLD, 16),
            Color.BLACK
        ));
        frame.add(sellersScrollPane);

        JPanel buyersPanel = new JPanel();
        buyersPanel.setLayout(new BoxLayout(buyersPanel, BoxLayout.Y_AXIS));
        buyersPanel.setBackground(new Color(30, 30, 30));

        JScrollPane buyersScrollPane = new JScrollPane(buyersPanel);
        buyersScrollPane.setBounds(470, 130, 400, 380);
        buyersScrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE, 1),
            "Buyers",
            0,
            0,
            new Font("Verdana", Font.BOLD, 16),
            Color.BLACK
        ));
        frame.add(buyersScrollPane);

        updateAccountPanel(sellersPanel, approvedSellers, "Seller", true);
        updateAccountPanel(buyersPanel, buyers, "Buyer", false);

        searchButton.addActionListener(e -> {
            String query = searchField.getText().trim().toLowerCase();
            updateAccountPanel(sellersPanel, approvedSellers, "Seller", true, query);
            updateAccountPanel(buyersPanel, buyers, "Buyer", false, query);
        });

        cancelSearchButton.addActionListener(e -> {
            searchField.setText("");
            updateAccountPanel(sellersPanel, approvedSellers, "Seller", true);
            updateAccountPanel(buyersPanel, buyers, "Buyer", false);
        });

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Verdana", Font.BOLD, 14));
        backButton.setBackground(new Color(50, 50, 50));
        backButton.setForeground(Color.WHITE);
        backButton.setBounds(50, 530, 100, 30);
        backButton.addActionListener(e -> {
            frame.dispose();
            Admin.main(null);
        });
        frame.add(backButton);

        frame.setVisible(true);
    }

    private static void loadAccounts(String fileName, String[] accounts) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null && i < accounts.length) {
                accounts[i++] = line.trim();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading accounts: " + e.getMessage());
        }
    }

    private static void updateAccountPanel(JPanel panel, String[] accounts, String accountType, boolean isSeller) {
        updateAccountPanel(panel, accounts, accountType, isSeller, null);
    }

    private static void updateAccountPanel(JPanel panel, String[] accounts, String accountType, boolean isSeller, String filter) {
        panel.removeAll();

        for (int i = 0; i < accounts.length; i++) {
            if (accounts[i] != null && (filter == null || accounts[i].toLowerCase().contains(filter))) {
                String[] parts = accounts[i].split(" ");
                String displayInfo = isSeller
                    ? String.format("Username: %s, Email: %s", parts[0], parts[2])
                    : String.format("Username: %s, Email: %s", parts[0], parts[1]);
                JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
                row.setBackground(new Color(40, 40, 40));
                row.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                JLabel nameLabel = new JLabel(displayInfo);
                nameLabel.setFont(new Font("Verdana", Font.PLAIN, 16));
                nameLabel.setForeground(Color.WHITE);

                JButton deleteButton = new JButton("Delete");
                deleteButton.setFont(new Font("Verdana", Font.PLAIN, 12));
                deleteButton.setBackground(new Color(255, 0, 0));
                deleteButton.setForeground(Color.WHITE);

                int index = i;
                String fileName = accountType.equals("Seller") ? APPROVED_SELLERS_FILE : BUYER_DATA_FILE;
                deleteButton.addActionListener(e -> {
                    deleteAccount(fileName, accounts, index);
                    panel.remove(row);
                    panel.revalidate();
                    panel.repaint();
                });

                row.add(nameLabel);
                row.add(deleteButton);
                panel.add(row);
            }
        }

        panel.revalidate();
        panel.repaint();
    }

    private static void deleteAccount(String fileName, String[] accounts, int index) {
        accounts[index] = null;
        saveAccountsToFile(fileName, accounts);
        JOptionPane.showMessageDialog(null, "Account deleted successfully!");
    }

    private static void saveAccountsToFile(String fileName, String[] accounts) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String account : accounts) {
                if (account != null) {
                    writer.write(account);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving accounts: " + e.getMessage());
        }
    }
}
