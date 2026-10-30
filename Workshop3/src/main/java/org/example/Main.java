package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Store store = new Store();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {

            System.out.println("\n===== STORE HOME =====");
            System.out.println("1. Display Products");
            System.out.println("2. Display Cart");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.println("Displaying Products...");
                    break;

                case "2":
                    System.out.println("Displaying Cart...");
                    break;

                case "3":
                    System.out.println("Thank you for shopping!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid selection. Try again.");
                    break;
            }
        }

        scanner.close();
    }
}