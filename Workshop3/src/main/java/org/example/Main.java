
package org.example;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Create store and scanner
        Store store = new Store();
        Scanner scanner = new Scanner(System.in);

        boolean running = true;

        // Keep the application running
        while (running) {

            // Display home menu
            // Display store welcome message
            System.out.println("\n================================");
            System.out.println("     WELCOME TO PAUL'S PANTRY");
            System.out.println("            어서 오세요!");
            System.out.println("================================");
            System.out.println("1. Display Products");
            System.out.println("2. Display Cart");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {

                case "1": {

                    // Keep the product menu running until the user goes back
                    boolean viewingProducts = true;

                    while (viewingProducts) {

                        // Display all available store products
                        store.displayProducts();

                        // Display product menu options
                        System.out.println("\n===== PAUL'S PANTRY | PRODUCTS =====");
                        System.out.println("1. Search by Product Name");
                        System.out.println("2. Search by Price Range");
                        System.out.println("3. Search by Department");
                        System.out.println("4. Add Product to Cart");
                        System.out.println("5. Back to Home");
                        System.out.print("Enter your choice: ");

                        String productChoice = scanner.nextLine();

                        switch (productChoice) {

                            case "1":
                                // Prompt user to search products by name
                                System.out.print("Enter product name: ");
                                String searchName = scanner.nextLine();

                                // Display matching products
                                store.searchByName(searchName);
                                break;

                            case "2":
                                // Handle invalid price inputs
                                try {

                                    // Prompt user for minimum price
                                    System.out.print("Enter minimum price: ");
                                    double minPrice = Double.parseDouble(scanner.nextLine());

                                    // Prompt user for maximum price
                                    System.out.print("Enter maximum price: ");
                                    double maxPrice = Double.parseDouble(scanner.nextLine());

                                    // Display products within selected price range
                                    store.searchByPrice(minPrice, maxPrice);

                                } catch (NumberFormatException e) {

                                    // Display error if user enters an invalid number
                                    System.out.println("Invalid input! Please enter a valid number.");

                                }
                                break;

                            case "3":
                                // Display available departments
                                System.out.println("\n===== PAUL'S PANTRY | DEPARTMENTS =====");
                                System.out.println("1. Food");
                                System.out.println("2. Beverages");
                                System.out.println("3. Snacks");
                                System.out.println("4. Frozen Foods");
                                System.out.print("Enter your choice: ");

                                String departmentChoice = scanner.nextLine();
                                String department = "";

                                // Select department based on user input
                                switch (departmentChoice) {

                                    case "1":
                                        department = "Food";
                                        break;

                                    case "2":
                                        department = "Beverages";
                                        break;

                                    case "3":
                                        department = "Snacks";
                                        break;

                                    case "4":
                                        department = "Frozen Foods";
                                        break;

                                    default:
                                        // Handle invalid department selections
                                        System.out.println("Invalid department selection.");
                                        break;
                                }

                                // Display products if a valid department was selected
                                if (!department.isEmpty()) {
                                    store.searchByDepartment(department);
                                }
                                break;

                            case "4":
                                // Prompt user for product SKU
                                System.out.print("Enter product SKU: ");
                                String sku = scanner.nextLine();

                                // Add selected product to cart
                                store.addToCart(sku);
                                break;

                            case "5":
                                // Return to the home screen
                                viewingProducts = false;
                                break;

                            default:
                                // Handle invalid selections
                                System.out.println("Invalid selection. Try again.");
                                break;
                        }
                    }
                    break;
                }

                case "2": {

                    // Keep the cart menu running until the user goes back
                    boolean viewingCart = true;

                    while (viewingCart) {

                        // Display cart menu
                        System.out.println("\n===== PAUL'S PANTRY | SHOPPING CART =====");

                        // Display products and total inside the menu
                        store.displayCart();

                        // Display available cart options
                        System.out.println("\n1. Check Out");
                        System.out.println("2. Remove Product");
                        System.out.println("3. Back to Home");
                        System.out.print("Enter your choice: ");

                        String cartChoice = scanner.nextLine();

                        switch (cartChoice) {

                            case "1":
                                // Checkout functionality will be added later
                                System.out.println("Checkout coming soon!");
                                break;

                            case "2":
                                // Display cart before removing a product
                                store.displayCart();

                                // Prompt user for product SKU
                                System.out.print("Enter product SKU to remove: ");
                                String removeSku = scanner.nextLine();

                                // Remove selected product from cart
                                store.removeFromCart(removeSku);
                                break;

                            case "3":
                                // Return to the home screen
                                viewingCart = false;
                                break;

                            default:
                                // Handle invalid selections
                                System.out.println("Invalid selection. Try again.");
                                break;
                        }
                    }
                    break;
                }

                case "3":
                    // Exit application
                    System.out.println("Thank you for shopping at Paul's Pantry!");
                    System.out.println("조심히 가세요!");
                    running = false;
                    break;

                default:
                    // Handle invalid selections
                    System.out.println("Invalid selection. Try again.");
                    break;
            }
        }

        // Close scanner
        scanner.close();
    }
}
