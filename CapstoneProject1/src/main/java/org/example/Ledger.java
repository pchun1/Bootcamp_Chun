package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Ledger {

    private ArrayList<Transaction> transactions;

    // Set up the ledger with saved transactions
    public Ledger(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    // Add a transaction to the ledger
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    // Display all transactions, newest first
    public void displayAll() {

        ArrayList<Transaction> sortedTransactions = new ArrayList<>(transactions);

        // Sort by date and time
        // Reversed because comparator sorts from oldest to newest
        Collections.sort(sortedTransactions,
                Comparator.comparing(Transaction::getDate)
                        .thenComparing(Transaction::getTime)
                        .reversed());

        displayTableHeader();
        for (Transaction transaction : sortedTransactions) {
            displayTransaction(transaction);
        }
    }

    // Display deposits or payments
    public void displayByType(boolean deposits) {

        ArrayList<Transaction> sortedTransactions = new ArrayList<>(transactions);

        Collections.sort(sortedTransactions,
                Comparator.comparing(Transaction::getDate)
                        .thenComparing(Transaction::getTime)
                        .reversed());

        displayTableHeader();
        for (Transaction transaction : sortedTransactions) {

            if (deposits && transaction.getAmount() > 0) {
                displayTransaction(transaction);
            } else if (!deposits && transaction.getAmount() < 0) {
                displayTransaction(transaction);
            }
        }
    }

    // Display a single transaction
    public void displayTransaction(Transaction transaction) {

        double amount = transaction.getAmount();


        String color = amount >= 0 ? Main.GREEN : Main.RED;

        System.out.printf(
                "  %-12s %-10s %-25.25s %-20.20s %s$%10.2f%s%n",
                transaction.getDate(),
                transaction.getTime(),
                transaction.getDescription(),
                transaction.getVendor(),
                color,
                amount,
                Main.RESET
        );
    }

    public void displayTableHeader() {

        System.out.printf(
                Main.MUTED + "  %-12s %-10s %-25s %-20s %11s%n" + Main.RESET,
                "DATE", "TIME", "DESCRIPTION", "VENDOR", "AMOUNT"
        );

        System.out.println(
                Main.MUTED + "  " + "-".repeat(84) + Main.RESET
        );
    }
    // Get all transactions for reports
    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }
}
