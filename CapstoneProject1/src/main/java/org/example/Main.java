
package org.example;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

        // Loops application to keep running
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
        String time = LocalTime.now().format(
                DateTimeFormatter.ofPattern("HH:mm:ss")
        );

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
                    showReports(scanner, ledger);
                    break;

                case "H":
                    viewingLedger = false;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Display the reports menu
    public static void showReports(Scanner scanner, Ledger ledger) {

        boolean viewingReports = true;

        while (viewingReports) {

            System.out.println("\n========== REPORTS ==========");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("6) Custom Search");
            System.out.println("0) Back");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {

                case "1":
                    showMonthToDate(ledger);
                    break;

                case "2":
                    System.out.println("\n--- PREVIOUS MONTH ---");
                    break;

                case "3":
                    System.out.println("\n--- YEAR TO DATE ---");
                    break;

                case "4":
                    System.out.println("\n--- PREVIOUS YEAR ---");
                    break;

                case "5":
                    System.out.println("\n--- SEARCH BY VENDOR ---");
                    break;

                case "6":
                    System.out.println("\n--- CUSTOM SEARCH ---");
                    break;

                case "0":
                    viewingReports = false;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    // Display transactions from the current month
    public static void showMonthToDate(Ledger ledger) {

        System.out.println("\n--- MONTH TO DATE ---");

        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);

        ArrayList<Transaction> sortedTransactions =
                new ArrayList<>(ledger.getTransactions());

        // Sort transactions from newest to oldest
        Collections.sort(sortedTransactions,
                Comparator.comparing(Transaction::getDate)
                        .thenComparing(Transaction::getTime)
                        .reversed());

        for (Transaction transaction : sortedTransactions) {

            LocalDate transactionDate = LocalDate.parse(transaction.getDate());

            if (!transactionDate.isBefore(startOfMonth)
                    && !transactionDate.isAfter(today)) {

                ledger.displayTransaction(transaction);
            }
        }
    }
}
