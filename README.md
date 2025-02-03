![FashionHub Cover](cover.png)


# Clothing E-Commerce Management System

## Overview
**FashionHub** is a comprehensive clothing e-commerce platform built using Java. It allows buyers to browse and purchase products, sellers to manage their inventories, and admins to oversee the system. The platform follows Object-Oriented Programming (OOP) principles such as **encapsulation**, **inheritance**, and **abstraction** for scalability and maintainability. The graphical user interface (GUI) is built using **Java Swing**, ensuring a responsive and user-friendly experience.

## Features

### 1. **User Authentication**
   - **Login**: Buyers, Sellers, and Admins can log in with their credentials.
   - **Sign Up**: New users (Buyers, Sellers,Admins) can create an account.
   - **Change Password**: Users (Buyers, Sellers, Admins) can change their passwords.

### 2. **Buyer Features**
   - **Browse Products**: Buyers can view available products listed by different sellers.
   - **Add to Cart**: Buyers can add items to the shopping cart.
   - **Checkout**: Buyers can complete their purchase with shipping and payment details.
   - **Order History**: Buyers can view their past orders.
   - **Track Orders**: Buyers can track the status of their orders.

### 3. **Seller Features**
   - **Product Management**: Sellers can add, update, or remove their products from the platform.
   - **Order Management**: Sellers can manage and track the orders placed by buyers.
   - **Sales Overview**: Sellers can view statistics such as total sales, total orders, and average order value.

### 4. **Admin Features**
   - **Manage Accounts**: Admin can approve, reject, or delete buyer and seller accounts.
   - **Order Management**: Admin can view and manage all orders across the platform.
   - **Seller Management**: Admin can approve or reject seller applications.
   - **Account Deletion**: Admin can delete accounts when necessary.

## Technologies Used
- **Java**: Core programming language.
- **Swing**: Java's GUI toolkit used to build the interface.
- **File I/O**: Reading and writing to files (for data persistence).
- **Event-Driven Programming**: Used for user interaction (button clicks, text input, etc.).

## Project Structure
The project is divided into several classes, each focusing on a specific part of the system:

### Key Classes:
- **Start.java**: The entry point of the application, launching the splash screen and the login page.
- **Login.java**: Handles the login and Registration page for Buyers, Sellers, and Admins.
- **Buyer.java**: Manages the buyer dashboard where users can browse products, view the cart, track orders, and change their passwords.
- **Seller.java**: Manages the seller dashboard, allowing sellers to add/remove products,update tracking status,view sales data, and change their passwords.
- **Admin.java**: Manages the admin dashboard, allowing admins to manage accounts, products, and orders.
- **AddOrder.java**: The seller's interface for adding new products to their store.
- **Cart.java**: Manages the shopping cart for the buyer.
- **ChangePasswordAdmin.java**: Interface for the admin to change their password.
- **ChangePasswordSeller.java**: Interface for sellers to change their password.
- **ChangePasswordBuyer.java**: Interface for buyers to change their password.
- **Checkout.java**: Handles the checkout process where buyers input their shipping and payment details.
- **DeleteAccounts.java**: Admin interface for deleting user accounts.
- **ManageSellerAccounts.java**: Admin interface for managing seller accounts.
- **Orders.java**: Displays order history for buyers.
- **Payment.java**: The payment interface for completing the transaction.
- **SellerOverview.java**: Provides a sales overview for sellers (total sales, total orders, etc.).
- **Track.java**: Allows users to track the status of their orders.
- **SellerOrderReceived.java**: Interface for sellers to manage received orders.

## How to Run the Project

### Prerequisites
- **Java JDK 8 or above** must be installed on your machine.
- **IDE**: You can use any Java IDE such as IntelliJ IDEA, Eclipse, or NetBeans.

### Steps to Run the Project
1. **Clone the Repository**:
   ```bash
   [git clone https://github.com/tariqulislamrahat/fashionhub.git](https://github.com/tariqulislamrahat/java_project.git]
   ```

2. **Compile the Code**:
   If using an IDE, open the project in your chosen IDE. If working from the command line, navigate to the project folder and run:
   ```bash
   javac *.java
   ```

3. **Run the Application**:
   To run the project, execute the `Start.java` file:
   ```bash
   java main.Start
   ```

### GUI Overview
- **Login Page**: The first screen where users log in with their credentials.
- **Buyer Dashboard**: After login, buyers can browse products, manage their cart, view order history, and track their orders.
- **Seller Dashboard**: After login, sellers can manage products, view sales data, and process orders.
- **Admin Dashboard**: Admins can manage accounts, view products, and control seller management.

## How to Contribute
1. **Fork the Repository**.
2. **Clone Your Fork** to your local machine.
3. Make the necessary changes or improvements.
4. **Submit a Pull Request**.

Please ensure that your code follows Java conventions and is well-documented.

## Credits
- **MD. Iftakher Hossain** [@IftakherEmon](https://github.com/IftakherEmon)
- **MD. Sakibul Islam Siam**
- **Tariqul Islam** [@tariqulislamrahat](https://github.com/tariqulislamrahat)

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact
For any inquiries or questions, please contact:
- Email: tariqulislamrahat@yahoo.com

---

### **Note**: 
Ensure you have the **Login.java** and other core classes within the `fashionhub` package in the project structure.
