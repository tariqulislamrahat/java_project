package main;

import javax.swing.*;
import fashionhub.Login;

public class Start {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
        
            showLoginPage();
        });
    }

    private static void showLoginPage() {
        SwingUtilities.invokeLater(() -> {
            Login.main(null);
        });
    }
}