package fashionhub;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Login {
    private static final String PENDING_SELLERS_FILE = "data/pending_sellers.txt";
    private static final String APPROVED_SELLERS_FILE = "data/seller_data.txt";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            setupDataDirectories();
            showLoginPage();
        });
    }

    private static void setupDataDirectories() {
        createDataDirectory("data");
        createDataFile("data/buyer_data.txt");
        createDataFile(PENDING_SELLERS_FILE);
        createDataFile(APPROVED_SELLERS_FILE);
        createDataFile("data/admin_data.txt");
    }

    private static void createDataDirectory(String dirName) {
        File directory = new File(dirName);
        if (!directory.exists() && !directory.mkdir()) {
            System.out.println("Failed to create " + dirName + " directory.");
        }
    }

    private static void createDataFile(String fileName) {
        try {
            File file = new File(fileName);
            if (file.createNewFile()) {
                System.out.println(fileName + " created successfully.");
            } else {
                System.out.println(fileName + " already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating " + fileName);
            e.printStackTrace();
        }
    }

    private static void showLoginPage() {
        JFrame frame = new JFrame("FashionHub - Login");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        frame.getContentPane().setBackground(new Color(30, 30, 30));

        JLabel titleLabel = new JLabel("Welcome to FashionHub", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(50, 30, 500, 50);
        frame.add(titleLabel);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Verdana", Font.PLAIN, 16));
        userLabel.setForeground(Color.WHITE);
        userLabel.setBounds(120, 100, 100, 30);
        frame.add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(240, 100, 250, 30);
        userField.setFont(new Font("Verdana", Font.PLAIN, 14));
        frame.add(userField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Verdana", Font.PLAIN, 16));
        passLabel.setForeground(Color.WHITE);
        passLabel.setBounds(120, 160, 100, 30);
        frame.add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(240, 160, 250, 30);
        passField.setFont(new Font("Verdana", Font.PLAIN, 14));
        frame.add(passField);

        JLabel roleLabel = new JLabel("Login As:");
        roleLabel.setFont(new Font("Verdana", Font.PLAIN, 16));
        roleLabel.setForeground(Color.WHITE);
        roleLabel.setBounds(120, 220, 100, 30);
        frame.add(roleLabel);

        String[] roles = {"Buyer", "Seller", "Admin"};
        JComboBox<String> roleDropdown = new JComboBox<>(roles);
        roleDropdown.setBounds(240, 220, 250, 30);
        roleDropdown.setFont(new Font("Verdana", Font.PLAIN, 14));
        frame.add(roleDropdown);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(180, 320, 100, 40);
        loginButton.setFont(new Font("Verdana", Font.BOLD, 14));
        loginButton.setBackground(new Color(0, 123, 255));
        loginButton.setForeground(Color.WHITE);
        frame.add(loginButton);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(320, 320, 120, 40);
        registerButton.setFont(new Font("Verdana", Font.BOLD, 14));
        registerButton.setBackground(new Color(40, 167, 69));
        registerButton.setForeground(Color.WHITE);
        frame.add(registerButton);

        loginButton.addActionListener(e -> {
            String role = (String) roleDropdown.getSelectedItem();
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            if (role.equals("Buyer")) {
                if (isBuyerValid(username, password)) {
                    JOptionPane.showMessageDialog(frame, "Login Successful as Buyer! Welcome, " + username + "!");
                    frame.dispose();
                    Buyer.main(null);
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid username or password for Buyer", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else if (role.equals("Seller")) {
                if (isSellerValid(username, password)) {
                    JOptionPane.showMessageDialog(frame, "Login Successful as Seller! Welcome, " + username + "!");
                    frame.dispose();
                    Seller.main(null);
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid username or password for Seller", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else if (role.equals("Admin")) {
                if (isAdminValid(username, password)) {
                    JOptionPane.showMessageDialog(frame, "Login Successful as Admin! Welcome, " + username + "!");
                    frame.dispose();
                    Admin.main(null);
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid username or password for Admin", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        registerButton.addActionListener(e -> {
            frame.dispose();
            showRegistrationPage();
        });

        frame.setVisible(true);
    }

    private static boolean isBuyerValid(String username, String password) {
        return checkFileForUsername("data/buyer_data.txt", username, password);
    }

    private static boolean isSellerValid(String username, String password) {
        return checkFileForUsername("data/seller_data.txt", username, password);
    }

    private static boolean isAdminValid(String username, String password) {
        return checkFileForUsername("data/admin_data.txt", username, password);
    }

    private static boolean checkFileForUsername(String fileName, String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + fileName);
        }
        return false;
    }

    private static void showRegistrationPage() {
        JFrame frame = new JFrame("FashionHub - Registration");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        frame.getContentPane().setBackground(new Color(30, 30, 30));

        JLabel titleLabel = new JLabel("Register to FashionHub", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(50, 30, 500, 50);
        frame.add(titleLabel);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Verdana", Font.PLAIN, 16));
        userLabel.setForeground(Color.WHITE);
        userLabel.setBounds(120, 100, 100, 30);
        frame.add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(240, 100, 250, 30);
        userField.setFont(new Font("Verdana", Font.PLAIN, 14));
        frame.add(userField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Verdana", Font.PLAIN, 16));
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setBounds(120, 160, 100, 30);
        frame.add(emailLabel);

        JTextField emailField = new JTextField();
        emailField.setBounds(240, 160, 250, 30);
        emailField.setFont(new Font("Verdana", Font.PLAIN, 14));
        frame.add(emailField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Verdana", Font.PLAIN, 16));
        passLabel.setForeground(Color.WHITE);
        passLabel.setBounds(120, 220, 100, 30);
        frame.add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(240, 220, 250, 30);
        passField.setFont(new Font("Verdana", Font.PLAIN, 14));
        frame.add(passField);

        JLabel roleLabel = new JLabel("Register As:");
        roleLabel.setFont(new Font("Verdana", Font.PLAIN, 16));
        roleLabel.setForeground(Color.WHITE);
        roleLabel.setBounds(120, 280, 120, 30);
        frame.add(roleLabel);

        String[] roles = {"Buyer", "Seller", "Admin"};
        JComboBox<String> roleDropdown = new JComboBox<>(roles);
        roleDropdown.setBounds(240, 280, 250, 30);
        frame.add(roleDropdown);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(180, 400, 120, 40);
        registerButton.setFont(new Font("Verdana", Font.BOLD, 14));
        registerButton.setBackground(new Color(0, 123, 255));
        registerButton.setForeground(Color.WHITE);
        frame.add(registerButton);

        JButton backButton = new JButton("Back");
        backButton.setBounds(320, 400, 120, 40);
        backButton.setFont(new Font("Verdana", Font.BOLD, 14));
        backButton.setBackground(new Color(220, 53, 69));
        backButton.setForeground(Color.WHITE);
        frame.add(backButton);

        registerButton.addActionListener(e -> {
            String role = (String) roleDropdown.getSelectedItem();
            String username = userField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                if (role.equals("Seller")) {
                    saveToFile(PENDING_SELLERS_FILE, username + " " + password + " " + email);
                    JOptionPane.showMessageDialog(frame, "Registration successful! Your account is pending admin approval.");
                } else {
                    String fileName = "data/" + role.toLowerCase() + "_data.txt";
                    saveToFile(fileName, username + " " + password + " " + email);
                    JOptionPane.showMessageDialog(frame, "Registration successful! Welcome, " + username + "!");
                }
                frame.dispose();
                showLoginPage();
            } else {
                JOptionPane.showMessageDialog(frame, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> {
            frame.dispose();
            showLoginPage();
        });

        frame.setVisible(true);
    }

    private static void saveToFile(String fileName, String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(data);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + fileName);
        }
    }
}
