
package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class Store {

    // Store all products from CSV
    private ArrayList<Product> products = new ArrayList<>();

    // Store products added to the shopping cart
    private ArrayList<CartItem> cart = new ArrayList<>();

    // Constructor
    public Store() {
        loadProducts();
    }

    // Load products from CSV
    public void loadProducts() {

        // Clear inventory before loading
        products.clear();

        // Open CSV using BufferedReader and FileReader
        try (BufferedReader reader = new BufferedReader(
                new FileReader("src/main/resources/products.csv"))) {

            // Skip the header row
            reader.readLine();

            String line;

            // Read each line until the end of the file
            while ((line = reader.readLine()) != null) {

                // Skip empty lines
                if (line.isBlank()) {
                    continue;
                }

                // Handle invalid product information
                try {

                    // Separate product information by pipes
                    String[] data = line.split("\\|", -1);

                    if (data.length != 4) {
                        System.out.println("Invalid product information: " + line);
                        continue;
                    }

                    String sku = data[0].trim();
                    String productName = data[1].trim();
                    double price = Double.parseDouble(data[2].trim());
                    String department = data[3].trim();

                    // Check for missing or invalid values
                    if (sku.isEmpty() || productName.isEmpty()
                            || department.isEmpty()
                            || !Double.isFinite(price) || price < 0) {

                        System.out.println("Invalid product information: " + line);
                        continue;
                    }

                    // Create a new Product object
                    Product product = new Product(
                            sku, productName, price, department
                    );

                    // Add product to inventory
                    products.add(product);

                } catch (NumberFormatException e) {

                    // Handle invalid product prices
                    System.out.println("Invalid product price: " + line);
                }
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
        if (!Double.isFinite(minPrice) || !Double.isFinite(maxPrice)
                || minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {

            System.out.println("Invalid price range.");
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
        System.out.println("\n===== "
                + searchDepartment.toUpperCase() + " =====");

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
            if (product.getSku().equalsIgnoreCase(sku.trim())) {

                // Check if product already exists in cart
                for (CartItem item : cart) {

                    if (item.getProduct().getSku()
                            .equalsIgnoreCase(sku.trim())) {

                        // Increase quantity instead of adding a duplicate
                        item.increaseQuantity();

                        System.out.println(product.getProductName()
                                + " added to cart!");

                        return;
                    }
                }

                // Add a new cart item
                cart.add(new CartItem(product));

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

        // Display cart column headings
        System.out.printf(
                "%-5s | %-28s | %3s | %9s%n",
                "SKU", "Product", "Qty", "Subtotal"
        );

        System.out.println("-------------------------------------------------------");

        // Loop through all products in the cart
        for (CartItem item : cart) {

            System.out.printf(
                    Locale.US,
                    "%-5s | %-28s | %3d | $%8.2f%n",
                    item.getProduct().getSku(),
                    item.getProduct().getProductName(),
                    item.getQuantity(),
                    item.getSubtotal()
            );
        }

        // Display total sales amount
        System.out.printf(Locale.US,
                "\nCart Total: $%.2f%n", getCartTotal());
    }

    // Calculate the shopping cart total
    public double getCartTotal() {

        double total = 0;

        // Add all cart item subtotals
        for (CartItem item : cart) {
            total += item.getSubtotal();
        }

        return Math.round(total * 100.0) / 100.0;
    }

    // Check if the shopping cart is empty
    public boolean isCartEmpty() {
        return cart.isEmpty();
    }

    // Remove a product from the shopping cart
    public void removeFromCart(String sku) {

        // Loop through products in the cart
        for (CartItem item : cart) {

            // Check if the SKU matches
            if (item.getProduct().getSku()
                    .equalsIgnoreCase(sku.trim())) {

                // Decrease quantity if more than one exists
                if (item.getQuantity() > 1) {

                    item.decreaseQuantity();

                    System.out.println("Removed one "
                            + item.getProduct().getProductName()
                            + " from cart!");

                } else {

                    // Remove product if quantity reaches zero
                    cart.remove(item);

                    System.out.println(item.getProduct().getProductName()
                            + " removed from cart!");
                }

                return;
            }
        }

        // Display message if product is not in cart
        System.out.println("Product not found in cart.");
    }

    // Complete checkout and save receipt
    public boolean checkout(double amountPaid) {

        // Check if the cart is empty
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return false;
        }

        double total = getCartTotal();

        // Validate payment amount
        if (!Double.isFinite(amountPaid) || amountPaid < 0) {
            System.out.println("Invalid payment amount.");
            return false;
        }

        // Round payment to two decimal places
        amountPaid = Math.round(amountPaid * 100.0) / 100.0;

        // Check if payment covers the total
        if (amountPaid < total) {
            System.out.printf(Locale.US,
                    "Insufficient payment. Amount due: $%.2f%n", total);
            return false;
        }

        // Calculate change
        double change = Math.round(
                (amountPaid - total) * 100.0) / 100.0;

        // Get current date and time
        LocalDateTime orderDate = LocalDateTime.now();

        DateTimeFormatter dateFormat =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        DateTimeFormatter fileFormat =
                DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

        // Build the sales receipt
        StringBuilder receipt = new StringBuilder();

        receipt.append("\n===== PAUL'S PANTRY | SALES RECEIPT =====\n");
        receipt.append("어서 오세요!\n\n");

        receipt.append("Order Date: ")
                .append(orderDate.format(dateFormat))
                .append("\n\n");

        receipt.append(String.format(
                "%-5s | %-28s | %3s | %9s%n",
                "SKU", "Product", "Qty", "Subtotal"
        ));

        receipt.append("-------------------------------------------------------\n");

        // Add all purchased products to receipt
        for (CartItem item : cart) {

            receipt.append(String.format(
                    Locale.US,
                    "%-5s | %-28s | %3d | $%8.2f%n",
                    item.getProduct().getSku(),
                    item.getProduct().getProductName(),
                    item.getQuantity(),
                    item.getSubtotal()
            ));
        }

        // Add payment information
        receipt.append("\n");

        receipt.append(String.format(
                Locale.US, "Sales Total:  $%.2f%n", total));

        receipt.append(String.format(
                Locale.US, "Amount Paid:  $%.2f%n", amountPaid));

        receipt.append(String.format(
                Locale.US, "Change Given: $%.2f%n", change));

        receipt.append("\nThank you for shopping at Paul's Pantry!\n");
        receipt.append("감사합니다!\n");

        // Create Receipts folder inside Workshop3 folder
        File receiptsFolder = new File("Receipts");

        if (!receiptsFolder.exists() && !receiptsFolder.mkdirs()) {
            System.out.println("Unable to create Receipts folder.");
            return false;
        }

        // Create receipt filename using date and time
        String fileName = orderDate.format(fileFormat) + ".txt";

        File receiptFile = new File(receiptsFolder, fileName);

        // Prevent overwriting an existing receipt
        if (receiptFile.exists()) {
            System.out.println("A receipt with this timestamp already exists.");
            System.out.println("Please try checkout again.");
            return false;
        }

        // Save the receipt using FileWriter and BufferedWriter
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(receiptFile))) {

            writer.write(receipt.toString());

        } catch (IOException e) {

            // Handle errors when saving the receipt
            System.out.println("Error saving receipt: " + e.getMessage());
            return false;
        }

        // Display the completed receipt
        System.out.print(receipt);

        System.out.println("\nReceipt saved to: "
                + receiptFile.getPath());

        // Clear the shopping cart after successful checkout
        cart.clear();

        return true;
    }
}
