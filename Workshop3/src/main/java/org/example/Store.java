package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Store {

    // Store all products from CSV
    private ArrayList<Product> products = new ArrayList<>();
    // Store products added to the shopping cart
    private ArrayList<Product> cart = new ArrayList<>();

    public Store() {
        loadProducts();
    }

    // Load products from CSV
    public void loadProducts() {

        // Open CSV using BufferedReader and FileReader
        try
                (BufferedReader reader = new BufferedReader(
                new FileReader("src/main/resources/products.csv"))) {

            // Skip the header row
            reader.readLine();

            String line;

            // Read each line until the end of the file
            while ((line = reader.readLine()) != null) {

                // Separate product information by pipes
                String[] data = line.split("\\|");

                String sku = data[0];
                String productName = data[1];
                double price = Double.parseDouble(data[2]);
                String department = data[3];


                Product product = new Product(
                        sku, productName, price, department
                );

                // Add product to inventory
                products.add(product);
            }

            // Display number of products loaded
            System.out.println("Loaded " + products.size() + " products.");

        } catch (IOException e) {

            // Handle errors when reading the file
            System.out.println("Error reading product file: " + e.getMessage());
        }
    }

    // Display all products in the store
    public void displayProducts() {

        System.out.println("\n===== STORE PRODUCTS =====");

        // Loop through all products
        for (Product product : products) {

            System.out.printf(
                    "%s | %-28s | $%.2f | %s%n",
                    product.getSku(),
                    product.getProductName(),
                    product.getPrice(),
                    product.getDepartment()
            );
        }
    }

    // Search products by name
    public void searchByName(String searchName) {

        boolean found = false;

        // Loop through all products
        for (Product product : products) {

            // Check if the product name contains the search
            if (product.getProductName().toLowerCase()
                    .contains(searchName.toLowerCase())) {

                System.out.printf(
                        "%s | %-28s | $%.2f | %s%n",
                        product.getSku(),
                        product.getProductName(),
                        product.getPrice(),
                        product.getDepartment()
                );

                found = true;
            }
        }

        // Display message if no products match
        if (!found) {
            System.out.println("No matching products found.");
        }
    }

    // Search products by price range
    public void searchByPrice(double minPrice, double maxPrice) {

        boolean found = false;

        // Check if the price range is valid
        if (minPrice > maxPrice) {
            System.out.println("Minimum price cannot exceed maximum price.");
            return;
        }

        // Loop through all products
        for (Product product : products) {

            // Check if the product price is within the range
            if (product.getPrice() >= minPrice &&
                    product.getPrice() <= maxPrice) {

                System.out.printf(
                        "%s | %-28s | $%.2f | %s%n",
                        product.getSku(),
                        product.getProductName(),
                        product.getPrice(),
                        product.getDepartment()
                );

                found = true;
            }
        }

        // Display message if no products match
        if (!found) {
            System.out.println("No products found within this price range.");
        }
    }
    // Search products by department
    public void searchByDepartment(String searchDepartment) {

        boolean found = false;

        // Display department search results
        System.out.println("\n===== " + searchDepartment.toUpperCase() + " =====");

        // Loop through all products
        for (Product product : products) {

            // Check if the department matches
            if (product.getDepartment().equalsIgnoreCase(searchDepartment)) {

                System.out.printf(
                        "%s | %-28s | $%.2f | %s%n",
                        product.getSku(),
                        product.getProductName(),
                        product.getPrice(),
                        product.getDepartment()
                );

                found = true;
            }
        }

        // Display message if no products match
        if (!found) {
            System.out.println("No products found in this department.");
        }
    }
    // Add a product to the shopping cart
    public void addToCart(String sku) {

        // Loop through all available products
        for (Product product : products) {

            // Check if the SKU matches
            if (product.getSku().equalsIgnoreCase(sku)) {

                // Add the matching product to the cart
                cart.add(product);

                System.out.println(product.getProductName()
                        + " added to cart!");

                return;
            }
        }

        // Display message if product was not found
        System.out.println("Product not found.");
    }
    // Display shopping cart and calculate total
    public void displayCart() {

        // Check if the cart is empty
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        double total = 0;

        // Loop through all products in the cart
        for (Product product : cart) {

            System.out.printf(
                    "%s | %-28s | $%.2f%n",
                    product.getSku(),
                    product.getProductName(),
                    product.getPrice()
            );

            // Add product price to total
            total += product.getPrice();
        }

        // Display total sales amount
        System.out.printf("\nCart Total: $%.2f%n", total);
    }
    // Remove a product from the shopping cart
    public void removeFromCart(String sku) {

        // Loop through products in the cart
        for (Product product : cart) {

            // Check if the SKU matches
            if (product.getSku().equalsIgnoreCase(sku)) {

                // Remove the selected product
                cart.remove(product);

                System.out.println(product.getProductName()
                        + " removed from cart!");

                return;
            }
        }

        // Display message if product is not in cart
        System.out.println("Product not found in cart.");
    }
}