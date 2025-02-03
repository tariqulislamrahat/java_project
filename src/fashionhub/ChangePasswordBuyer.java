package fashionhub;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class ChangePasswordBuyer extends JFrame {

    private static final Color DARK_BG = new Color(30, 30, 30);
    private static final Color CARD_BG = new Color(45, 45, 45);
    private static final Color ACCENT_BLUE = new Color(0, 123, 255);
    private static final String BUYER_DATA_FILE = "data/buyer_data.txt";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChangePasswordBuyer().showChangePasswordPage());
    }

    public void showChangePasswordPage() {
        setTitle("FashionHub - Change Password");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(DARK_BG);
        setLocationRelativeTo(null);

        JLabel headerLabel = new JLabel("Change Buyer Password");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBounds(270, 30, 300, 30);
        add(headerLabel);

        JLabel currentPasswordLabel = new JLabel("Current Password:");
        currentPasswordLabel.setForeground(Color.WHITE);
        currentPasswordLabel.setBounds(150, 100, 150, 30);
        add(currentPasswordLabel);

        JPasswordField currentPasswordField = new JPasswordField();
        currentPasswordField.setBackground(CARD_BG);
        currentPasswordField.setForeground(Color.WHITE);
        currentPasswordField.setCaretColor(Color.WHITE);
        currentPasswordField.setBounds(300, 100, 250, 30);
        add(currentPasswordField);

        JLabel newPasswordLabel = new JLabel("New Password:");
        newPasswordLabel.setForeground(Color.WHITE);
        newPasswordLabel.setBounds(150, 150, 150, 30);
        add(newPasswordLabel);

        JPasswordField newPasswordField = new JPasswordField();
        newPasswordField.setBackground(CARD_BG);
        newPasswordField.setForeground(Color.WHITE);
        newPasswordField.setCaretColor(Color.WHITE);
        newPasswordField.setBounds(300, 150, 250, 30);
        add(newPasswordField);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setForeground(Color.WHITE);
        confirmPasswordLabel.setBounds(150, 200, 150, 30);
        add(confirmPasswordLabel);

        JPasswordField confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBackground(CARD_BG);
        confirmPasswordField.setForeground(Color.WHITE);
        confirmPasswordField.setCaretColor(Color.WHITE);
        confirmPasswordField.setBounds(300, 200, 250, 30);
        add(confirmPasswordField);

        JButton submitButton = new JButton("Change Password");
        submitButton.setPreferredSize(new Dimension(180, 40));
        submitButton.setBackground(ACCENT_BLUE);
        submitButton.setForeground(Color.WHITE);
        submitButton.setBorderPainted(false);
        submitButton.setFocusPainted(false);
        submitButton.setBounds(300, 280, 200, 40);
        submitButton.addActionListener(e -> changePassword(currentPasswordField, newPasswordField, confirmPasswordField));
        add(submitButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(180, 40));
        cancelButton.setBackground(new Color(220, 53, 69));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBorderPainted(false);
        cancelButton.setFocusPainted(false);
        cancelButton.setBounds(520, 280, 200, 40);
        cancelButton.addActionListener(e -> {
            dispose();
            Buyer.main(null);
        });
        add(cancelButton);

        setVisible(true);
    }

    private void changePassword(JPasswordField currentPasswordField, JPasswordField newPasswordField, JPasswordField confirmPasswordField) {
        String currentPassword = new String(currentPasswordField.getPassword()).trim();
        String newPassword = new String(newPasswordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

        String storedPassword = readBuyerPassword();
        System.out.println("Stored Password: " + storedPassword);
        System.out.println("Entered Current Password: " + currentPassword);

        if (storedPassword != null && currentPassword.equals(storedPassword)) {
            System.out.println("Password Match: " + currentPassword.equals(storedPassword));

            if (newPassword.equals(confirmPassword)) {
                updatePassword(newPassword);
                JOptionPane.showMessageDialog(this, "Password changed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "New passwords do not match. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Current password is incorrect. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String readBuyerPassword() {
        try (Scanner scanner = new Scanner(new FileReader(BUYER_DATA_FILE))) {
            if (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(" ");
                if (parts.length > 1) {
                    return parts[1];
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error reading buyer data file: " + e.getMessage());
        }
        return null;
    }

    private void updatePassword(String newPassword) {
        try (BufferedReader br = new BufferedReader(new FileReader(BUYER_DATA_FILE))) {
            String line = br.readLine();
            if (line != null) {
                String[] parts = line.split(" ");
                if (parts.length > 1) {
                    parts[1] = newPassword;
                    String updatedLine = String.join(" ", parts);

                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(BUYER_DATA_FILE))) {
                        bw.write(updatedLine);
                    } catch (IOException e) {
                        System.err.println("Error writing to buyer data file: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading buyer data file: " + e.getMessage());
        }
    }
}