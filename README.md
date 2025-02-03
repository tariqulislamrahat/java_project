# FashionHub - Clothing E-Commerce Management System

## Overview
**FashionHub** is a comprehensive clothing e-commerce platform designed for managing buyers, sellers, orders, payments, and user accounts. Built using Java, it incorporates core Object-Oriented Programming (OOP) principles, including encapsulation, inheritance, and abstraction. The system allows users to interact with a modern graphical user interface (GUI) built using Java Swing components.

The system allows:
- Buyers to browse and purchase clothing items.
- Sellers to manage their products and orders.
- Admin to oversee user accounts, orders, and manage the overall system.

## Features
### 1. **User Authentication**
   - **Login**: Buyers and Sellers can log in with their credentials.
   - **Sign Up**: New users can create an account.
   - **Change Password**: Buyers, Sellers, and Admins can change their passwords.

### 2. **Buyer Features**
   - **Browse Products**: View available clothing items listed by different sellers.
   - **Add to Cart**: Add items to the shopping cart.
   - **Checkout**: Complete the purchase by providing shipping and payment information.

### 3. **Seller Features**
   - **Manage Products**: Sellers can add, update, or remove clothing items from their store.
   - **View Orders**: Sellers can see the orders placed for their products and manage them.
   - **Track Orders**: Track the shipping and delivery status of orders.

### 4. **Admin Features**
   - **Manage Accounts**: Admin can manage buyer and seller accounts (approve, delete, etc.).
   - **View All Orders**: Admin has a complete view of all placed orders across all sellers.
   - **Account Deletion**: Admin can delete accounts if necessary.

### 5. **Splash Screen**
   - A custom splash screen is shown upon startup to provide a smooth user experience during the application load.

## Technologies Used
- **Java**: Core language for building the application.
- **Swing**: For building the graphical user interface.
- **File I/O**: For reading and writing data to files (e.g., user credentials, product details).
- **Event-Driven Programming**: All user interactions are handled through event listeners (button clicks, etc.).

## Project Structure
The project is divided into multiple classes based on the roles and functionalities of the system. Below is a brief overview of the important classes in the project:

- **Start.java**: The entry point of the application, launching the splash screen and then the login page.
- **Login.java**: The login screen where users can enter their credentials.
- **Buyer.java**: Class responsible for managing buyer-specific functionalities.
- **Seller.java**: Class responsible for managing seller-specific functionalities.
- **Admin.java**: Class responsible for managing admin-specific functionalities.
- **Cart.java**: Class representing the shopping cart for buyers.
- **Payment.java**: Class handling payment-related operations.
- **Order.java**: Class to manage the orders placed by buyers.

## How to Run the Project

### Prerequisites
- **Java JDK 8 or above** must be installed on your machine.
- Set up **IDE** (such as IntelliJ IDEA, Eclipse, or NetBeans) to work with Java projects.

### Steps to Run the Project
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/tariqulislamrahat/fashionhub.git
