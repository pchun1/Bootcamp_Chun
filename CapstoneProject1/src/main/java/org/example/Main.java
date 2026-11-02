
package org.example;

import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Load saved transactions
        TransactionFileManager fileManager = new TransactionFileManager();
        ArrayList<Transaction> transactions = fileManager.loadTransactions();
        Ledger ledger = new Ledger(transactions);

        boolean running = true;

        // Keep the application running
        while (running) {

            System.out.println("\n====================================");
            System.out.println("           STATIONLEDGER");
            System.out.println("    Business Financial Management");
            System.out.println("====================================");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {

                case "D":
                    addTransaction(scanner, fileManager, ledger, true);
                    break;

                case "P":
                    addTransaction(scanner, fileManager, ledger, false);
                    break;

                case "L":
                    showLedger(scanner, ledger);
                    break;

                case "X":
                    running = false;
                    System.out.println("Thank you for using StationLedger!");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    // Add a deposit or payment
    public static void addTransaction(Scanner scanner,
                                      TransactionFileManager fileManager,
                                      Ledger ledger,
                                      boolean deposit) {

        System.out.println(deposit ? "\n--- ADD DEPOSIT ---" : "\n--- MAKE PAYMENT ---");

        System.out.print("Description: ");
        String description = scanner.nextLine();

        System.out.print("Vendor: ");
        String vendor = scanner.nextLine();

        double amount;

        // Check that the amount is a valid number
        try {
            System.out.print("Amount: ");
            String amountInput = scanner.nextLine();

            // Remove the dollar sign if entered
            amountInput = amountInput.replace("$", "").trim();

            amount = Double.parseDouble(amountInput);

            if (amount <= 0) {
                System.out.println("Amount must be greater than zero.");
                return;
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a number.");
            return;
        }

        // Record the current date and time
        String date = LocalDate.now().toString();
        String time = LocalTime.now().withNano(0).toString();

        if (!deposit) {
            amount = -amount;
        }

        Transaction transaction = new Transaction(
                date, time, description, vendor, amount
        );


        // Save the transaction before adding it to the ledger
        if (fileManager.saveTransaction(transaction)) {

            ledger.addTransaction(transaction);
            System.out.println("Transaction recorded successfully!");

        } else {
            System.out.println("Transaction could not be saved.");
        }

    }

    // Display the ledger menu
    public static void showLedger(Scanner scanner, Ledger ledger) {

        boolean viewingLedger = true;

        while (viewingLedger) {

            System.out.println("\n========== LEDGER ==========");
            System.out.println("A) All Transactions");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {

                case "A":
                    System.out.println("\n--- ALL TRANSACTIONS ---");
                    ledger.displayAll();
                    break;

                case "D":
                    System.out.println("\n--- DEPOSITS ---");
                    ledger.displayByType(true);
                    break;

                case "P":
                    System.out.println("\n--- PAYMENTS ---");
                    ledger.displayByType(false);
                    break;

                case "R":
                    System.out.println("Reports Not Available");
                    break;

                case "H":
                    viewingLedger = false;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
