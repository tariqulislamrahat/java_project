package fashionhub;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Vector;

public class Seller {
    private JFrame frame;
    private JPanel sidebar;
    private JButton addOrderButton, orderReceivedButton, overviewButton, changePasswordButton, logoutButton;
    private JTextField searchField;
    private JPanel mainContent;
    private Vector<JPanel> productCards;
    private Vector<String[]> products;
    private int selectedProductIndex = -1;
    private JButton cancelSearchButton;

    private class ProductManagement {
        private JFrame productFrame;
        private JTextField nameField, priceField;
        private JButton addUpdateButton, deleteButton;
        private boolean isEditMode = false;

        public ProductManagement(int productIndex) {
            if (productIndex != -1) {
                selectedProductIndex = productIndex;
                isEditMode = true;
            }
            initializeProductUI();
        }

        private void initializeProductUI() {
            productFrame = new JFrame(isEditMode ? "Edit Product" : "Add Product");
            productFrame.setSize(500, 400);
            productFrame.setLayout(null);
            productFrame.getContentPane().setBackground(new Color(30, 30, 30));
            productFrame.setLocationRelativeTo(null);

            JLabel titleLabel = new JLabel(isEditMode ? "Edit Product" : "Add Product", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Verdana", Font.BOLD, 24));
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setBounds(50, 20, 400, 30);
            productFrame.add(titleLabel);

            String[] labels = {"Product Name:", "Price:"};
            JTextField[] fields = {
                nameField = new JTextField(isEditMode ? products.get(selectedProductIndex)[0] : ""),
                priceField = new JTextField(isEditMode ? products.get(selectedProductIndex)[1] : "")
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

            addUpdateButton = new JButton(isEditMode ? "Update" : "Add");
            addUpdateButton.setBounds(100, 280, 100, 30);
            addUpdateButton.setFont(new Font("Verdana", Font.BOLD, 14));
            addUpdateButton.setBackground(new Color(0, 123, 255));
            addUpdateButton.setForeground(Color.WHITE);
            addUpdateButton.setFocusPainted(false);
            addUpdateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            addUpdateButton.addActionListener(e -> handleProductAction());
            productFrame.add(addUpdateButton);

            deleteButton = new JButton("Delete");
            deleteButton.setBounds(250, 280, 100, 30);
            deleteButton.setFont(new Font("Verdana", Font.BOLD, 14));
            deleteButton.setBackground(new Color(220, 53, 69));
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setFocusPainted(false);
            deleteButton.setEnabled(isEditMode);
            deleteButton.addActionListener(e -> deleteProduct());
            productFrame.add(deleteButton);

            productFrame.setVisible(true);
        }

        private void handleProductAction() {
            String productName = nameField.getText().trim();
            String price = priceField.getText().trim();

            if (productName.isEmpty() || price.isEmpty()) {
                JOptionPane.showMessageDialog(productFrame, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double priceValue = Double.parseDouble(price);
                if (priceValue <= 0) {
                    JOptionPane.showMessageDialog(productFrame, "Price must be greater than 0.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (isEditMode) {
                    products.get(selectedProductIndex)[0] = productName;
                    products.get(selectedProductIndex)[1] = price;
                    JOptionPane.showMessageDialog(productFrame, "Product updated successfully!");
                } else {
                    String[] newProduct = {productName, price};
                    products.add(newProduct);
                    JOptionPane.showMessageDialog(productFrame, "Product added successfully!");
                }
                saveProductsToFile();
                updateProductCards();
                productFrame.dispose();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(productFrame, "Invalid price. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void deleteProduct() {
            int confirm = JOptionPane.showConfirmDialog(productFrame, "Are you sure you want to delete this product?", "Delete Product", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                products.remove(selectedProductIndex);
                JOptionPane.showMessageDialog(productFrame, "Product deleted successfully!");
                saveProductsToFile();
                updateProductCards();
                productFrame.dispose();
            }
        }
    }

    public Seller() {
        productCards = new Vector<>();
        products = new Vector<>();
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("FashionHub - Seller Dashboard");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.getContentPane().setBackground(new Color(30, 30, 30));
        frame.setLocationRelativeTo(null);

        setupSidebar();
        setupSearchBar();
        setupMainContent();

        frame.setVisible(true);
    }

    private void setupSidebar() {
        sidebar = new JPanel();
        sidebar.setBounds(0, 0, 200, 600);
        sidebar.setBackground(new Color(50, 50, 50));
        sidebar.setLayout(null);

        JLabel titleLabel = new JLabel("SELLER", SwingConstants.CENTER);
        titleLabel.setBounds(10, 20, 180, 40);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        sidebar.add(titleLabel);

        addOrderButton = createMenuButton("Add Product", 20, 140);
        orderReceivedButton = createMenuButton("Order Received", 20, 190);
        overviewButton = createMenuButton("Overview", 20, 240);
        changePasswordButton = createMenuButton("Change Pass", 20, 290);
        logoutButton = createMenuButton("Logout", 20, 340);

        logoutButton.addActionListener(e -> {
            frame.dispose();
            Login.main(null);
        });

        addOrderButton.addActionListener(e -> new AddOrder(Seller.this));
        orderReceivedButton.addActionListener(new ActionListener() {
           // ... existing code ...
           public void actionPerformed(ActionEvent e) {
            new SellerOrderReceived();
            frame.dispose();
        }
        });

        overviewButton.addActionListener(e -> {
            new SellerOverview();
            frame.dispose();
        });

        changePasswordButton.addActionListener(e -> {
            new ChangePasswordSeller().showChangePasswordPage();
        });

        frame.add(sidebar);
    }

    private JButton createMenuButton(String text, int x, int y) {
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
                button.setBackground(new Color(0, 123, 255));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(60, 60, 60));
            }
        });

        sidebar.add(button);
        return button;
    }

    private void setupSearchBar() {
        JPanel searchPanel = new JPanel();
        searchPanel.setBounds(220, 20, 560, 50);
        searchPanel.setBackground(new Color(30, 30, 30));
        searchPanel.setLayout(new BorderLayout(10, 0));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.setBackground(new Color(30, 30, 30));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        searchField = new JTextField();
        searchField.setFont(new Font("Verdana", Font.PLAIN, 16));
        searchField.setBackground(new Color(70, 70, 70));
        searchField.setForeground(Color.WHITE);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100), 2),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        searchField.setCaretColor(Color.WHITE);
        searchField.putClientProperty("JTextField.placeholderText", "Search products...");

        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("Verdana", Font.BOLD, 14));
        searchButton.setBackground(new Color(0, 123, 255));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        searchButton.addActionListener(e -> performSearch());

        cancelSearchButton = new JButton("Clear");
        cancelSearchButton.setFont(new Font("Verdana", Font.BOLD, 14));
        cancelSearchButton.setBackground(new Color(220, 53, 69));
        cancelSearchButton.setForeground(Color.WHITE);
        cancelSearchButton.setFocusPainted(false);
        cancelSearchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelSearchButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        cancelSearchButton.addActionListener(e -> cancelSearch());

        searchButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                searchButton.setBackground(new Color(0, 86, 179));
            }

            public void mouseExited(MouseEvent e) {
                searchButton.setBackground(new Color(0, 123, 255));
            }
        });

        cancelSearchButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                cancelSearchButton.setBackground(new Color(200, 35, 51));
            }

            public void mouseExited(MouseEvent e) {
                cancelSearchButton.setBackground(new Color(220, 53, 69));
            }
        });

        buttonPanel.add(searchButton);
        buttonPanel.add(cancelSearchButton);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(buttonPanel, BorderLayout.EAST);

        frame.add(searchPanel);
    }

    private void cancelSearch() {
        searchField.setText("");
        for (JPanel card : productCards) {
            if (card != null) {
                card.setVisible(true);
            }
        }
        mainContent.revalidate();
        mainContent.repaint();
    }

    private void performSearch() {
        String searchTerm = searchField.getText().toLowerCase().trim();
        for (JPanel card : productCards) {
            if (card != null) {
                boolean visible = true;
                if (!searchTerm.isEmpty()) {
                    Component[] components = card.getComponents();
                    visible = false;
                    for (Component component : components) {
                        if (component instanceof JLabel) {
                            String text = ((JLabel) component).getText().toLowerCase();
                            if (text.contains(searchTerm)) {
                                visible = true;
                                break;
                            }
                        }
                    }
                }
                card.setVisible(visible);
            }
        }
        mainContent.revalidate();
        mainContent.repaint();
    }

    private void setupMainContent() {
        mainContent = new JPanel();
        mainContent.setLayout(new GridLayout(0, 2, 15, 15));
        mainContent.setBackground(new Color(30, 30, 30));

        loadProducts();
        for (String[] product : products) {
            JPanel card = createProductCard(product[0], "Price: " + product[1] + " TK");
            productCards.add(card);
            mainContent.add(card);
        }

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBounds(220, 80, 540, 460);
        scrollPane.setBorder(null);
        scrollPane.setBackground(new Color(30, 30, 30));
        frame.add(scrollPane);
    }

    private JPanel createProductCard(String name, String price) {
        JPanel card = new JPanel();
        card.setLayout(new GridLayout(3, 1, 5, 5));
        card.setBackground(new Color(60, 60, 60));
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Verdana", Font.BOLD, 14));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel priceLabel = new JLabel(price);
        priceLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
        priceLabel.setForeground(new Color(40, 167, 69));
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton viewButton = new JButton("View");
        viewButton.setFont(new Font("Verdana", Font.PLAIN, 12));
        viewButton.setBackground(new Color(0, 123, 255));
        viewButton.setForeground(Color.WHITE);
        viewButton.setFocusPainted(false);
        viewButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewButton.addActionListener(e -> new ProductManagement(productCards.indexOf(card)));

        card.add(nameLabel);
        card.add(priceLabel);
        card.add(viewButton);

        return card;
    }

    private void loadProducts() {
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

    private void saveProductsToFile() {
        try (FileWriter fw = new FileWriter("data/products.txt")) {
            for (String[] product : products) {
                fw.write(String.join(",", product) + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving products: " + e.getMessage());
        }
    }

    public void updateProductCards() {
        mainContent.removeAll();
        productCards.clear();
        for (String[] product : products) {
            JPanel card = createProductCard(product[0], "Price: " + product[1] + " TK");
            productCards.add(card);
            mainContent.add(card);
        }
        mainContent.revalidate();
        mainContent.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Seller());
    }
}