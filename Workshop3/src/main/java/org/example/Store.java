package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Store {

    // Stores all products from our CSV
    private ArrayList<Product> products = new ArrayList<>();

    // Constructor
    public Store() {
        loadProducts();
    }

    // Loads products from CSV
    public void loadProducts() {

        try {
            Scanner fileScanner = new Scanner(
                    new File("src/main/resources/products.csv")
            );

            // Skip the header row
            fileScanner.nextLine();

            while (fileScanner.hasNextLine()) {

                String line = fileScanner.nextLine();

                // Separate product information by pipes
                String[] data = line.split("\\|");

                String sku = data[0];
                String productName = data[1];
                double price = Double.parseDouble(data[2]);
                String department = data[3];

                // Create a Product object
                Product product = new Product(
                        sku, productName, price, department
                );

                // Add product to inventory
                products.add(product);
            }

            fileScanner.close();

            System.out.println("Loaded " + products.size() + " products.");

        } catch (FileNotFoundException e) {
            System.out.println("Product file not found!");
        }
    }
    // Displays all products in the store
    public void displayProducts() {

        System.out.println("\n===== STORE PRODUCTS =====");

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
}