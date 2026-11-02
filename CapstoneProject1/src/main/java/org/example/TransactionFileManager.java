
package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class TransactionFileManager {

    private final String fileName = "transactions.csv";

    // Load transactions from the file
    public ArrayList<Transaction> loadTransactions() {

        ArrayList<Transaction> transactions = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            //Skips header
            reader.readLine();

            String line;

            // Read each saved transaction
            while ((line = reader.readLine()) != null) {

                String[] parts = line.split("\\|");

                if (parts.length != 5) {
                    System.out.println("Skipping an invalid transaction.");
                    continue;
                }

                try {
                    String date = parts[0];
                    String time = parts[1];
                    String description = parts[2];
                    String vendor = parts[3];
                    double amount = Double.parseDouble(parts[4]);

                    // Create a transaction from the CSV data
                    Transaction transaction = new Transaction(
                            date, time, description, vendor, amount
                    );

                    transactions.add(transaction);

                } catch (NumberFormatException e) {
                    System.out.println("Skipping a transaction with an invalid amount.");
                }
            }

            reader.close();

        } catch (IOException e) {
            System.out.println("Error reading transactions: " + e.getMessage());
        }

        return transactions;
    }

    // Save a transaction to the file
    public boolean saveTransaction(Transaction transaction) {

        try {
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(fileName, true)
            );

            // Format the transaction for CSV
            String line = String.format(
                    Locale.US,
                    "%s|%s|%s|%s|%.2f",
                    transaction.getDate(),
                    transaction.getTime(),
                    transaction.getDescription(),
                    transaction.getVendor(),
                    transaction.getAmount()
            );

            writer.write(line);
            writer.newLine();
            writer.close();

            return true;

        } catch (IOException e) {
            System.out.println("Error saving transaction: " + e.getMessage());
            return false;
        }
    }

}
