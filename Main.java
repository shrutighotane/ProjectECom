

import com.dao.*;
import com.model.Product;
import com.model.User;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static UserD userDAO = new UserD();
    private static ProductD productDAO = new ProductD();
    private static CartD cartDAO = new CartD();
    private static OrderD orderDAO = new OrderD();
    private static User currentUser = null;

    public static void main(String[] args) {
        System.out.println("Welcome");

        while (true) {
            if (currentUser == null) {
                System.out.println("\n1. Register\n2. Login\n3. Exit");
                System.out.print("Choose: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        register();
                        break;
                    case 2:
                        login();
                        break;
                    case 3:
                        System.out.println("Thank you ");
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            } else {
                if ("admin".equals(currentUser.getRole())) {
                    adminMenu();
                } else {
                    UserMenu();
                }
            }
        }
    }

    private static void register() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter role (admin/customer): ");
        String role = scanner.nextLine();

        User user = new User(name, email, password, role);
        if (userDAO.register(user)) {
            System.out.println("Registration successful!");
        } else {
            System.out.println("Registration failed.");
        }
    }

    private static void login() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = userDAO.login(email, password);
        if (user != null) {
            currentUser = user;
            System.out.println("Login successful " + user.getName());
        } else {
            System.out.println("Invalid email or password.");
        }
    }

    private static void adminMenu() {
        while (true) {
            System.out.println("\n Admin Menu ");
            System.out.println("1. Add Product");
            System.out.println("2. View All Products");
            System.out.println("3. Delete Product");
            System.out.println("4. Logout");
            System.out.print("Choose: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addProduct();
                    break;
                case 2:
                    viewProducts();
                    break;
                case 3:
                    deleteProduct();
                    break;
                case 4:
                    currentUser = null;
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void UserMenu() {
        while (true) {
            System.out.println("\n User Menu ");
            System.out.println("1. View Products");
            System.out.println("2. Add to Cart");
            System.out.println("3. View Cart");
            System.out.println("4. Place Order");
            System.out.println("5. View Order History");
            System.out.println("6. Logout");
            System.out.print("Choose: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewProducts();
                    break;
                case 2:
                    addToCart();
                    break;
                case 3:
                    viewCart();
                    break;
                case 4:
                    placeOrder();
                    break;
                case 5:
                    viewOrders();
                    break;
                case 6:
                    currentUser = null;
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void addProduct() {
        System.out.print("Product name: ");
        String name = scanner.nextLine();
        System.out.print("Price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Description: ");
        String desc = scanner.nextLine();
        System.out.print("Quantity: ");
        int qty = scanner.nextInt();
        scanner.nextLine();

        Product product = new Product(name, price, desc, qty);
        if (productDAO.addProduct(product)) {
            System.out.println("Product added successfully.");
        } else {
            System.out.println("Failed to add product.");
        }
    }

    private static void viewProducts() {
        List<Product> products = productDAO.getAllProducts();
        if (products.isEmpty()) {
            System.out.println("No products found.");
            return;
        }
        System.out.println("Product List");
        for (Product p : products) {
            System.out.println("ID: " + p.getId() +
                               " | Name: " + p.getName() +
                               " | Price: ₹" + p.getPrice() +
                               " | Qty: " + p.getQuantity() +
                               " | Description: " + p.getDescription());
        }
    }

    private static void deleteProduct() {
        System.out.print("Enter Product ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        if (productDAO.deleteProduct(id)) {
            System.out.println("Product deleted.");
        } else {
            System.out.println("Failed to delete product.");
        }
    }

    private static void addToCart() {
        System.out.print("Enter Product ID to add to cart: ");
        int productId = scanner.nextInt();
        scanner.nextLine();

        if (cartDAO.addToCart(currentUser.getId(), productId)) {
            System.out.println("Added to cart.");
        } else {
            System.out.println("Failed to add to cart.");
        }
    }

    private static void viewCart() {
        List<Product> cartItems = cartDAO.viewCart(currentUser.getId());
        if (cartItems.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }

        System.out.println("Your Cart");
        for (Product p : cartItems) {
            System.out.println("ID: " + p.getId() +
                               " | Name: " + p.getName() +
                               " | Price: ₹" + p.getPrice());
        }
    }

    private static void placeOrder() {
        List<Product> cartItems = cartDAO.viewCart(currentUser.getId());
        if (cartItems.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }

        for (Product p : cartItems) {
            orderDAO.placeOrder(currentUser.getId(), p.getId());
        }

        cartDAO.clearCart(currentUser.getId());
        System.out.println("Order placed successfully!");
    }

    private static void viewOrders() {
        List<Product> orders = orderDAO.viewOrderHistory(currentUser.getId());
        if (orders.isEmpty()) {
            System.out.println("No orders yet.");
            return;
        }

        System.out.println("Order History");
        for (Product p : orders) {
            System.out.println("Name: " + p.getName() +
                               " | Price: ₹" + p.getPrice());
        }
    }
}
