package fashionhub;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Vector;

public class Buyer extends JFrame {
    private JPanel sidebar;
    private JButton cartButton, orderHistoryButton, trackOrderButton, searchButton, cancelSearchButton, logoutButton, changePasswordButton;
    private JTextField searchField;
    private int orderCount;
    private JPanel mainContent;
    private Vector<JPanel> productCards;
    private Vector<String[]> products;

    public Buyer() {
        setTitle("FashionHub - Buyer Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        orderCount = getOrderCountFromFile();
        productCards = new Vector<>();
        products = new Vector<>();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(null);
        getContentPane().setBackground(new Color(30, 30, 30));
        setLocationRelativeTo(null);

        setupSidebar();
        setupMainContent();
        setupSearchBar();
        addHoverEffects();
        setVisible(true);
    }

    private void setupSidebar() {
        sidebar = new JPanel();
        sidebar.setBounds(0, 0, 200, 600);
        sidebar.setBackground(new Color(50, 50, 50));
        sidebar.setLayout(null);

        JLabel titleLabel = new JLabel("BUYER", SwingConstants.CENTER);
        titleLabel.setBounds(10, 20, 180, 40);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        sidebar.add(titleLabel);

        cartButton = createMenuButton("Cart", 20, 140);
        orderHistoryButton = createMenuButton("My Orders", 20, 190);
        trackOrderButton = createMenuButton("Track", 20, 240);
        changePasswordButton = createMenuButton("Change Pass", 20, 290);
        logoutButton = createMenuButton("Logout", 20, 340);

        add(sidebar);

        logoutButton.addActionListener(e -> {
            dispose();
            Login.main(null);
        });
        cartButton.addActionListener(e -> openCart());
        orderHistoryButton.addActionListener(e -> openOrderHistory());
        trackOrderButton.addActionListener(e -> openTrackOrder());

        changePasswordButton.addActionListener(e -> {
            new ChangePasswordBuyer().showChangePasswordPage();
        });
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

    private void openCart() {
        Cart cart = Cart.getInstance();
        cart.setVisible(true);
    }

    private void openOrderHistory() {
        new Orders().setVisible(true);
    }

    private void openTrackOrder() {
        new Track().setVisible(true);
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

        searchButton = new JButton("Search");
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

        buttonPanel.add(searchButton);
        buttonPanel.add(cancelSearchButton);

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(buttonPanel, BorderLayout.EAST);

        add(searchPanel);
    }

    private void cancelSearch() {
        searchField.setText("");
        for (JPanel card : productCards) {
            card.setVisible(true);
        }
        mainContent.revalidate();
        mainContent.repaint();
    }

    private void performSearch() {
        String searchTerm = searchField.getText().toLowerCase().trim();
        
        for (JPanel card : productCards) {
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
        mainContent.revalidate();
        mainContent.repaint();
    }

    private void setupMainContent() {
        mainContent = new JPanel();
        mainContent.setLayout(new GridLayout(0, 2, 15, 15));
        mainContent.setBackground(new Color(30, 30, 30));

        loadProducts();
        for (String[] product : products) {
            JPanel card = createProductCard(product[0], product[1]);
            productCards.add(card);
            mainContent.add(card);
        }

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBounds(220, 80, 540, 460);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(new Color(30, 30, 30));
        add(scrollPane);
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

        JLabel priceLabel = new JLabel("Price: " + price + " TK");
        priceLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
        priceLabel.setForeground(new Color(40, 167, 69));
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(60, 60, 60));

        JButton buyButton = new JButton("Buy Now");
        buyButton.setFont(new Font("Verdana", Font.PLAIN, 12));
        buyButton.setBackground(new Color(0, 123, 255));
        buyButton.setForeground(Color.WHITE);
        buyButton.setFocusPainted(false);
        buyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton cartButton = new JButton("Add to Cart");
        cartButton.setFont(new Font("Verdana", Font.PLAIN, 12));
        cartButton.setBackground(new Color(220, 53, 69));
        cartButton.setForeground(Color.WHITE);
        cartButton.setFocusPainted(false);
        cartButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buyButton.addActionListener(e -> openCheckout(name, price));

        cartButton.addActionListener(e -> {
            Cart cart = Cart.getInstance();
            cart.addItemToCart(name, price);
            cart.setVisible(true);
        });

        buttonPanel.add(buyButton);
        buttonPanel.add(cartButton);

        card.add(nameLabel);
        card.add(priceLabel);
        card.add(buttonPanel);

        return card;
    }

    private void openCheckout(String productName, String productPrice) {
        String deliveryCharge = "50";
        new Checkout(productName, productPrice, deliveryCharge).setVisible(true);
    }

    private void addHoverEffects() {
        searchButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                searchButton.setBackground(new Color(33, 136, 56));
            }
            public void mouseExited(MouseEvent e) {
                searchButton.setBackground(new Color(40, 167, 69));
            }
        });

        cancelSearchButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                cancelSearchButton.setBackground(new Color(200, 0, 0));
            }
            public void mouseExited(MouseEvent e) {
                cancelSearchButton.setBackground(Color.RED);
            }
        });
    }

    private int getOrderCountFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("data/orders.txt"))) {
            String line = br.readLine();
            return line != null ? Integer.parseInt(line.trim()) : 0;
        } catch (IOException | NumberFormatException e) {
            return 0;
        }
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Buyer());
    }
}