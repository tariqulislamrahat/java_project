package fashionhub;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Vector;
import java.util.stream.Collectors;

public class Admin {
    private static Vector<String[]> products = new Vector<>();
    private static JPanel mainContent;
    private static final Color DARK_BG = new Color(30, 30, 30);
    private static final Color CARD_BG = new Color(45, 45, 45);
    private static final Color ACCENT_BLUE = new Color(0, 123, 255);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Admin::showAdminPage);
    }

    private static void showAdminPage() {
        JFrame frame = new JFrame("FashionHub - Admin Dashboard");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.getContentPane().setBackground(DARK_BG);
        frame.setLocationRelativeTo(null);

        addSidebar(frame);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBounds(220, 20, 560, 40);
        searchPanel.setBackground(DARK_BG);

        JTextField searchField = new JTextField(30);
        searchField.setPreferredSize(new Dimension(350, 30));
        searchField.setBackground(CARD_BG);
        searchField.setForeground(Color.WHITE);
        searchField.setCaretColor(Color.WHITE);
        searchField.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));

        JButton searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(80, 30));
        searchButton.setBackground(ACCENT_BLUE);
        searchButton.setForeground(Color.WHITE);
        searchButton.setBorderPainted(false);
        searchButton.setFocusPainted(false);
        searchButton.addActionListener(e -> searchProducts(searchField));

        JButton clearButton = new JButton("Clear");
        clearButton.setPreferredSize(new Dimension(80, 30));
        clearButton.setBackground(new Color(220, 53, 69));
        clearButton.setForeground(Color.WHITE);
        clearButton.setBorderPainted(false);
        clearButton.setFocusPainted(false);
        clearButton.addActionListener(e -> clearSearch(searchField));

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);
        frame.add(searchPanel);

        mainContent = new JPanel();
        mainContent.setBounds(220, 80, 560, 470);
        mainContent.setLayout(new GridLayout(0, 2, 15, 15));
        mainContent.setBackground(DARK_BG);

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBounds(220, 80, 560, 470);
        scrollPane.setBorder(null);
        scrollPane.setBackground(DARK_BG);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        frame.add(scrollPane);

        loadProducts();
        displayProducts();

        frame.setVisible(true);
    }

    private static void displayProducts() {
        mainContent.removeAll();
        for (String[] product : products) {
            mainContent.add(createProductCard(product[0], product[1]));
        }
        mainContent.revalidate();
        mainContent.repaint();
    }

    private static JPanel createProductCard(String name, String price) {
        JPanel card = new JPanel();
        card.setLayout(new GridBagLayout());
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);

        JLabel nameLabel = new JLabel(name, SwingConstants.CENTER);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel priceLabel = new JLabel(price + " Tk", SwingConstants.CENTER);
        priceLabel.setForeground(new Color(40, 167, 69));
        priceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton editButton = new JButton("Edit");
        editButton.setBackground(ACCENT_BLUE);
        editButton.setForeground(Color.WHITE);
        editButton.setBorderPainted(false);
        editButton.setFocusPainted(false);
        editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editButton.setPreferredSize(new Dimension(80, 30));
        editButton.addActionListener(e -> openEditProductDialog(name, price));

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setBorderPainted(false);
        deleteButton.setFocusPainted(false);
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.setPreferredSize(new Dimension(80, 30));
        deleteButton.addActionListener(e -> confirmDeleteProduct(name));

        card.add(nameLabel, gbc);
        card.add(priceLabel, gbc);
        card.add(editButton, gbc);
        card.add(deleteButton, gbc);

        return card;
    }

    private static void confirmDeleteProduct(String name) {
        int option = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to delete this product?", "Confirm Deletion",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (option == JOptionPane.YES_OPTION) {
            deleteProduct(name);
        }
    }

    private static void openEditProductDialog(String name, String price) {
        ProductManagement productManagement = new ProductManagement(name, price);
    }

    private static void deleteProduct(String name) {
        products.removeIf(product -> product[0].equals(name));
        saveProductsToFile();
        displayProducts();
    }

    private static void clearSearch(JTextField searchField) {
        searchField.setText("");
        displayProducts();
    }

    private static void searchProducts(JTextField searchField) {
        String searchText = searchField.getText().toLowerCase();
        if (searchText.isEmpty()) {
            displayProducts();
        } else {
            Vector<String[]> filteredProducts = products.stream()
                    .filter(product -> product[0].toLowerCase().contains(searchText))
                    .collect(Collectors.toCollection(Vector::new));
            displayFilteredProducts(filteredProducts);
        }
    }

    private static void displayFilteredProducts(Vector<String[]> filteredProducts) {
        mainContent.removeAll();
        for (String[] product : filteredProducts) {
            mainContent.add(createProductCard(product[0], product[1]));
        }
        mainContent.revalidate();
        mainContent.repaint();
    }

    private static void addSidebar(JFrame frame) {
        JPanel sidebar = new JPanel();
        sidebar.setBounds(0, 0, 200, 600);
        sidebar.setBackground(new Color(50, 50, 50));
        sidebar.setLayout(null);

        JLabel sidebarTitle = new JLabel("ADMIN", SwingConstants.CENTER);
        sidebarTitle.setBounds(10, 20, 180, 40);
        sidebarTitle.setFont(new Font("Verdana", Font.BOLD, 20));
        sidebarTitle.setForeground(Color.WHITE);
        sidebar.add(sidebarTitle);

        JButton manageSellersButton = createMenuButton("Manage Sellers", 20, 140);
        manageSellersButton.addActionListener(e -> {
            frame.dispose();
            ManageSellerAccounts.main(null);
        });

        JButton deleteLogsButton = createMenuButton("Delete Logs", 20, 190);
        deleteLogsButton.addActionListener(e -> {
            frame.dispose();
            DeleteAccounts.main(null);
        });

        JButton changePasswordButton = createMenuButton("Change Pass", 20, 240);
        changePasswordButton.addActionListener(e -> {
            frame.dispose();
            ChangePasswordAdmin.main(new String[]{});
        });

        JButton logoutButton = createMenuButton("Logout", 20, 290);
        logoutButton.setBackground(new Color(255, 77, 77));
        logoutButton.setPreferredSize(new Dimension(160, 40));
        logoutButton.addActionListener(e -> {
            frame.dispose();
            Login.main(null);
        });

        sidebar.add(manageSellersButton);
        sidebar.add(deleteLogsButton);
        sidebar.add(changePasswordButton);
        sidebar.add(logoutButton);
        frame.add(sidebar);
    }

    private static JButton createMenuButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 160, 40);
        button.setFont(new Font("Verdana", Font.BOLD, 14));
        button.setBackground(new Color(60, 60, 60));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(ACCENT_BLUE);
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(60, 60, 60));
            }
        });

        return button;
    }

    private static void loadProducts() {
        try (BufferedReader br = new BufferedReader(new FileReader("data/products.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    products.add(parts);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading products file: " + e.getMessage());
        }
    }

    private static void saveProductsToFile() {
        try (FileWriter fw = new FileWriter("data/products.txt")) {
            for (String[] product : products) {
                fw.write(String.join(",", product) + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving products: " + e.getMessage());
        }
    }

    public static class ProductManagement {
        private JFrame productFrame;
        private JTextField nameField, priceField;
        private JButton addUpdateButton;
        private String originalName;

        public ProductManagement(String name, String price) {
            this.originalName = name;
            initializeProductUI(name, price);
        }

        private void initializeProductUI(String name, String price) {
            productFrame = new JFrame("Edit Product");
            productFrame.setSize(500, 400);
            productFrame.setLayout(null);
            productFrame.getContentPane().setBackground(DARK_BG);
            productFrame.setLocationRelativeTo(null);

            JLabel titleLabel = new JLabel("Edit Product", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Verdana", Font.BOLD, 24));
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setBounds(50, 20, 400, 30);
            productFrame.add(titleLabel);

            String[] labels = {"Product Name:", "Price:"};
            JTextField[] fields = {
                nameField = new JTextField(name),
                priceField = new JTextField(price)
            };

            int yPos = 60;
            for (int i = 0; i < labels.length; i++) {
                JLabel label = new JLabel(labels[i]);
                label.setFont(new Font("Verdana", Font.PLAIN, 14));
                label.setForeground(Color.WHITE);
                label.setBounds(50, yPos, 100, 25);
                productFrame.add(label);

                fields[i].setBounds(160, yPos, 250, 25);
                fields[i].setFont(new Font("Verdana", Font.PLAIN, 14));
                fields[i].setEditable(true);
                fields[i].setBackground(new Color(40, 40, 40));
                fields[i].setForeground(Color.WHITE);
                fields[i].setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
                productFrame.add(fields[i]);

                yPos += 35;
            }

            addUpdateButton = new JButton("Save");
            addUpdateButton.setBounds(100, 280, 100, 30);
            addUpdateButton.setFont(new Font("Verdana", Font.BOLD, 14));
            addUpdateButton.setBackground(new Color(0, 123, 255));
            addUpdateButton.setForeground(Color.WHITE);
            addUpdateButton.setFocusPainted(false);
            addUpdateButton.addActionListener(e -> handleProductAction(nameField, priceField));
            productFrame.add(addUpdateButton);

            productFrame.setVisible(true);
        }

        private void handleProductAction(JTextField nameField, JTextField priceField) {
            String originalName = this.originalName;
            String newPrice = priceField.getText();
    
            boolean found = false;
            for (int i = 0; i < products.size(); i++) {
                String[] product = products.get(i);
                if (product[0].equals(originalName)) {
                    products.set(i, new String[]{originalName, newPrice});
                    found = true;
                    break;
                }
            }
            
            if (found) {
           
                saveProductsToFile();
           
                SwingUtilities.invokeLater(() -> {
                    displayProducts();
                    JOptionPane.showMessageDialog(productFrame,
                        "Product updated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                });
            } else {
                JOptionPane.showMessageDialog(productFrame,
                    "Error: Product not found!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
          
            productFrame.dispose();
        }
    }
}
