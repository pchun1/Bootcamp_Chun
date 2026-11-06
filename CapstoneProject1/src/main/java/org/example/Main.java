
package org.example;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class Main {

    // Console styling
    public static final String RESET = "\u001B[0m";
    public static final String ACCENT = "\u001B[36m";
    public static final String MUTED = "\u001B[90m";
    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Set up user accounts and login
        UserManager userManager = new UserManager();
        User currentUser = showLogin(scanner, userManager);

        if (currentUser == null) {
            System.out.println("Login cancelled. Goodbye!");
            scanner.close();
            return;
        }

        // Load saved transactions
        TransactionFileManager fileManager = new TransactionFileManager();
        ArrayList<Transaction> transactions = fileManager.loadTransactions();
        Ledger ledger = new Ledger(transactions);

        boolean running = true;

        // Loops application to keep running
        while (running) {

            // Display main menu
            System.out.println();
            System.out.println(ACCENT + centerText("STATIONLEDGER", 44) + RESET);
            System.out.println(MUTED + "  ----------------------------------------" + RESET);
            System.out.println(centerText("MAIN MENU", 44));
            System.out.println(MUTED + centerText("Financial Management", 44) + RESET);
            System.out.println();
            System.out.println("  " + ACCENT + "[D]" + RESET + "  Add Deposit");
            System.out.println("  " + ACCENT + "[P]" + RESET + "  Make Payment");
            System.out.println("  " + ACCENT + "[L]" + RESET + "  Financial Ledger");
            System.out.println();
            System.out.println("  " + MUTED + "[X]  Exit Application" + RESET);
            System.out.println(MUTED + "  ----------------------------------------" + RESET);
            System.out.print(ACCENT + "  > " + RESET);

            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {

                case "D":
                    addTransaction(scanner, fileManager, ledger, true, currentUser, userManager);
                    break;

                case "P":
                    addTransaction(scanner, fileManager, ledger, false, currentUser, userManager);
                    break;

                case "L":
                    showLedger(scanner, ledger, currentUser, userManager);
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
                                      boolean deposit,
                                      User currentUser,
                                      UserManager userManager) {

        // Display transaction menu
        System.out.println();
        System.out.println(MUTED + "  ----------------------------------------" + RESET);
        System.out.println(ACCENT + centerText("STATIONLEDGER", 44) + RESET);
        System.out.println(MUTED + "  ----------------------------------------" + RESET);
        System.out.println();

        String title = deposit ? "ADD DEPOSIT" : "MAKE PAYMENT";
        System.out.println(centerText(title, 44));
        System.out.println();

        System.out.println("  " + ACCENT + "[1]" + RESET + "  Current Transaction");
        System.out.println("  " + ACCENT + "[2]" + RESET + "  Past Transaction (Manager Authorization)");
        System.out.println();
        System.out.println("  " + MUTED + "[X]  Return to Home" + RESET);
        System.out.println(MUTED + "  ----------------------------------------" + RESET);
        System.out.print(ACCENT + "  > " + RESET);

        String choice = scanner.nextLine().trim();

        if (choice.equalsIgnoreCase("X")) {
            System.out.println("Transaction cancelled.");
            return;
        }

        if (!choice.equals("1") && !choice.equals("2")) {
            System.out.println("Invalid choice. Please try again.");
            return;
        }

        String date;
        String time;

        if (choice.equals("2")) {

            // Require manager authorization for past transactions
            if (!authorizeManager(currentUser, userManager)) {
                return;
            }

            System.out.println("\n--- PAST TRANSACTION ---");

            // Get the original transaction date
            String dateInput = getInput(scanner, "Date (YYYY-MM-DD): ");
            if (dateInput == null) {
                System.out.println("Transaction cancelled.");
                return;
            }

            // Validate the entered date
            LocalDate transactionDate;

            try {
                transactionDate = LocalDate.parse(dateInput);

                if (transactionDate.isAfter(LocalDate.now())) {
                    System.out.println("Transaction date cannot be in the future.");
                    return;
                }

            } catch (java.time.format.DateTimeParseException e) {
                System.out.println("Invalid date. Use YYYY-MM-DD.");
                return;
            }

            // Get the original transaction time
            String timeInput = getInput(scanner, "Time (HH:mm:ss): ");
            if (timeInput == null) {
                System.out.println("Transaction cancelled.");
                return;
            }

            LocalTime transactionTime;

            try {
                transactionTime = LocalTime.parse(
                        timeInput, DateTimeFormatter.ofPattern("HH:mm:ss")
                );

                if (transactionDate.isEqual(LocalDate.now())
                        && transactionTime.isAfter(LocalTime.now())) {
                    System.out.println("Transaction time cannot be in the future.");
                    return;
                }

            } catch (java.time.format.DateTimeParseException e) {
                System.out.println("Invalid time. Use HH:mm:ss.");
                return;
            }

            date = transactionDate.toString();
            time = transactionTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        } else {

            // Record the current date and time
            date = LocalDate.now().toString();
            time = LocalTime.now().format(
                    DateTimeFormatter.ofPattern("HH:mm:ss")
            );
        }

        // Get transaction details
        String description = getInput(scanner, "Description: ");
        if (description == null) {
            System.out.println("Transaction cancelled.");
            return;
        }

        String vendor = getInput(scanner, "Vendor: ");
        if (vendor == null) {
            System.out.println("Transaction cancelled.");
            return;
        }

        double amount;

        // Validate the transaction amount
        try {
            String amountInput = getInput(scanner, "Amount: ");

            if (amountInput == null) {
                System.out.println("Transaction cancelled.");
                return;
            }

            amountInput = amountInput.replace("$", "").trim();
            amount = Double.parseDouble(amountInput);

            if (!Double.isFinite(amount) || amount <= 0) {
                System.out.println("Amount must be a valid number greater than zero.");
                return;
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a number.");
            return;
        }

        if (!deposit) {
            amount = -amount;
        }

        Transaction transaction = new Transaction(
                date, time, description, vendor, amount
        );

        // Save the transaction before adding it to the ledger
        if (fileManager.saveTransaction(transaction)) {
            ledger.addTransaction(transaction);

            System.out.println();
            System.out.println(MUTED + "  ----------------------------------------" + RESET);
            System.out.println(GREEN + centerText("TRANSACTION RECORDED", 44) + RESET);
            System.out.println(MUTED + centerText("Transaction saved successfully", 44) + RESET);
            System.out.println(MUTED + "  ----------------------------------------" + RESET);

        } else {
            System.out.println(RED + "  [ERROR] Transaction could not be saved." + RESET);
        }
    }
    // Display the ledger menu
    public static void showLedger(Scanner scanner, Ledger ledger, User currentUser, UserManager userManager) {

        // Limit employee access to transaction history
        if (!currentUser.isManager()) {

            boolean viewingEmployeeLedger = true;

            while (viewingEmployeeLedger) {
                // Display employee ledger menu
                System.out.println();
                System.out.println(MUTED + "  ----------------------------------------" + RESET);
                System.out.println(ACCENT + centerText("STATIONLEDGER", 44) + RESET);
                System.out.println(MUTED + "  ----------------------------------------" + RESET);
                System.out.println();
                System.out.println(centerText("EMPLOYEE LEDGER", 44));
                System.out.println();
                System.out.println("  " + ACCENT + "[1]" + RESET + "  Today's Transactions");
                System.out.println("  " + ACCENT + "[2]" + RESET + "  Full Ledger (Manager Authorization)");
                System.out.println();
                System.out.println("  " + MUTED + "[X]  Return to Home" + RESET);
                System.out.println(MUTED + "  ----------------------------------------" + RESET);

                String choice = getInput(scanner, "  > ");

                if (choice == null) {
                    viewingEmployeeLedger = false;
                    continue;
                }

                switch (choice) {

                    case "1":
                        System.out.println("\n--- TODAY'S TRANSACTIONS ---");
                        showTodayTransactions(ledger);
                        break;

                    case "2":
                        // Require manager approval to view transaction history
                        if (authorizeManager(currentUser, userManager)) {
                            System.out.println("\n--- ALL TRANSACTIONS ---");
                            ledger.displayAll();
                        }
                        break;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }

            return;
        }

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
    // Display transactions recorded today
    public static void showTodayTransactions(Ledger ledger) {
        LocalDate today = LocalDate.now();

        ArrayList<Transaction> sortedTransactions =
                new ArrayList<>(ledger.getTransactions());

        // Sort newest first
        Collections.sort(sortedTransactions,
                Comparator.comparing(Transaction::getDate)
                        .thenComparing(Transaction::getTime)
                        .reversed());

        boolean found = false;

        for (Transaction transaction : sortedTransactions) {
            LocalDate transactionDate = LocalDate.parse(transaction.getDate());

            if (transactionDate.isEqual(today)) {
                ledger.displayTransaction(transaction);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No transactions recorded today.");
        }
    }
    // Display the reports menu
    public static void showReports(Scanner scanner, Ledger ledger) {

        boolean viewingReports = true;

        while (viewingReports) {

            // Display financial reports menu
            System.out.println();
            System.out.println(MUTED + "  ----------------------------------------" + RESET);
            System.out.println(ACCENT + centerText("STATIONLEDGER", 44) + RESET);
            System.out.println(MUTED + "  ----------------------------------------" + RESET);
            System.out.println();
            System.out.println(centerText("FINANCIAL REPORTS", 44));
            System.out.println();
            System.out.println("  " + ACCENT + "[1]" + RESET + "  Month to Date");
            System.out.println("  " + ACCENT + "[2]" + RESET + "  Previous Month");
            System.out.println("  " + ACCENT + "[3]" + RESET + "  Year to Date");
            System.out.println("  " + ACCENT + "[4]" + RESET + "  Previous Year");
            System.out.println("  " + ACCENT + "[5]" + RESET + "  Search by Vendor");
            System.out.println("  " + ACCENT + "[6]" + RESET + "  Custom Search");
            System.out.println();
            System.out.println("  " + MUTED + "[X]  Return to Ledger" + RESET);
            System.out.println(MUTED + "  ----------------------------------------" + RESET);

            String choice = getInput(scanner, "  > ");

            if (choice == null) {
                viewingReports = false;
                continue;
            }

            switch (choice) {

                case "1":
                    showMonthToDate(ledger);
                    break;

                case "2":
                    showPreviousMonth(ledger);
                    break;

                case "3":
                    showYearToDate(ledger);
                    break;

                case "4":
                    showPreviousYear(ledger);
                    break;

                case "5":
                    showSearchByVendor(scanner, ledger);
                    break;

                case "6":
                    showCustomSearch(scanner, ledger);
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

        showReportHeader("MONTH TO DATE");

        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);

        ArrayList<Transaction> sortedTransactions =
                new ArrayList<>(ledger.getTransactions());

        // Sort transactions from newest to oldest
        Collections.sort(sortedTransactions,
                Comparator.comparing(Transaction::getDate)
                        .thenComparing(Transaction::getTime)
                        .reversed());

        ledger.displayTableHeader();
        for (Transaction transaction : sortedTransactions) {

            LocalDate transactionDate = LocalDate.parse(transaction.getDate());

            if (!transactionDate.isBefore(startOfMonth)
                    && !transactionDate.isAfter(today)) {

                ledger.displayTransaction(transaction);
            }
        }
    }
    // Display transactions from the previous month
    public static void showPreviousMonth(Ledger ledger) {
        showReportHeader("PREVIOUS MONTH");

        LocalDate today = LocalDate.now();
        LocalDate startOfPreviousMonth = today.minusMonths(1).withDayOfMonth(1);
        LocalDate endOfPreviousMonth = today.withDayOfMonth(1).minusDays(1);

        ArrayList<Transaction> sortedTransactions =
                new ArrayList<>(ledger.getTransactions());

        // Sort transactions from newest to oldest
        Collections.sort(sortedTransactions,
                Comparator.comparing(Transaction::getDate)
                        .thenComparing(Transaction::getTime)
                        .reversed());

        ledger.displayTableHeader();
        for (Transaction transaction : sortedTransactions) {
            LocalDate transactionDate = LocalDate.parse(transaction.getDate());

            if (!transactionDate.isBefore(startOfPreviousMonth)
                    && !transactionDate.isAfter(endOfPreviousMonth)) {
                ledger.displayTransaction(transaction);
            }
        }
    }
    // Display transactions from the beginning of the current year
    public static void showYearToDate(Ledger ledger) {
        showReportHeader("YEAR TO DATE");

        LocalDate today = LocalDate.now();
        LocalDate startOfYear = today.withDayOfYear(1);

        ArrayList<Transaction> sortedTransactions =
                new ArrayList<>(ledger.getTransactions());

        // Sort transactions from newest to oldest
        Collections.sort(sortedTransactions,
                Comparator.comparing(Transaction::getDate)
                        .thenComparing(Transaction::getTime)
                        .reversed());

        ledger.displayTableHeader();
        for (Transaction transaction : sortedTransactions) {
            LocalDate transactionDate = LocalDate.parse(transaction.getDate());

            if (!transactionDate.isBefore(startOfYear)
                    && !transactionDate.isAfter(today)) {
                ledger.displayTransaction(transaction);
            }
        }
    }
    // Display transactions from the previous year
    public static void showPreviousYear(Ledger ledger) {
        showReportHeader("PREVIOUS YEAR");

        LocalDate today = LocalDate.now();
        LocalDate previousYear = today.minusYears(1);

        LocalDate startOfPreviousYear = previousYear.withDayOfYear(1);
        LocalDate endOfPreviousYear = previousYear.withMonth(12).withDayOfMonth(31);

        ArrayList<Transaction> sortedTransactions =
                new ArrayList<>(ledger.getTransactions());

        // Sort transactions from newest to oldest
        Collections.sort(sortedTransactions,
                Comparator.comparing(Transaction::getDate)
                        .thenComparing(Transaction::getTime)
                        .reversed());

        ledger.displayTableHeader();
        for (Transaction transaction : sortedTransactions) {
            LocalDate transactionDate = LocalDate.parse(transaction.getDate());

            if (!transactionDate.isBefore(startOfPreviousYear)
                    && !transactionDate.isAfter(endOfPreviousYear)) {
                ledger.displayTransaction(transaction);
            }
        }
    }
    // Search transactions by vendor name
    public static void showSearchByVendor(Scanner scanner, Ledger ledger) {
        showReportHeader("SEARCH BY VENDOR");

        String search = getInput(scanner, "Enter vendor name: ");

        if (search == null) {
            System.out.println("Search cancelled.");
            return;
        }

        if (search.isEmpty()) {
            System.out.println("Vendor name cannot be empty.");
            return;
        }

        ArrayList<Transaction> sortedTransactions =
                new ArrayList<>(ledger.getTransactions());

        // Sort transactions from newest to oldest
        Collections.sort(sortedTransactions,
                Comparator.comparing(Transaction::getDate)
                        .thenComparing(Transaction::getTime)
                        .reversed());

        ledger.displayTableHeader();
        boolean found = false;

        for (Transaction transaction : sortedTransactions) {
            if (transaction.getVendor().toLowerCase()
                    .contains(search.toLowerCase())) {

                ledger.displayTransaction(transaction);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No matching transactions found.");
        }
    }
    // Search transactions using multiple filters (custom search)
    public static void showCustomSearch(Scanner scanner, Ledger ledger) {
        showReportHeader("CUSTOM SEARCH");
        System.out.println("Leave a field blank to skip that filter.");

        try {
            String startInput = getInput(scanner, "Start Date (YYYY-MM-DD): ");
            if (startInput == null) {
                System.out.println("Search cancelled.");
                return;
            }

            String endInput = getInput(scanner, "End Date (YYYY-MM-DD): ");
            if (endInput == null) {
                System.out.println("Search cancelled.");
                return;
            }

            LocalDate startDate = startInput.isEmpty()
                    ? null : LocalDate.parse(startInput);

            LocalDate endDate = endInput.isEmpty()
                    ? null : LocalDate.parse(endInput);

            if (startDate != null && endDate != null
                    && startDate.isAfter(endDate)) {
                System.out.println("Start date cannot be after end date.");
                return;
            }

            // Get search filters
            String description = getInput(scanner, "Description: ");
            if (description == null) {
                System.out.println("Search cancelled.");
                return;
            }
            description = description.toLowerCase();

            String vendor = getInput(scanner, "Vendor: ");
            if (vendor == null) {
                System.out.println("Search cancelled.");
                return;
            }
            vendor = vendor.toLowerCase();

            // Get amount range
            String minInput = getInput(scanner, "Minimum Amount: ");
            if (minInput == null) {
                System.out.println("Search cancelled.");
                return;
            }
            minInput = minInput.replace("$", "");

            String maxInput = getInput(scanner, "Maximum Amount: ");
            if (maxInput == null) {
                System.out.println("Search cancelled.");
                return;
            }
            maxInput = maxInput.replace("$", "");

            Double minAmount = minInput.isEmpty()
                    ? null : Double.parseDouble(minInput);

            Double maxAmount = maxInput.isEmpty()
                    ? null : Double.parseDouble(maxInput);

            if ((minAmount != null && !Double.isFinite(minAmount))
                    || (maxAmount != null && !Double.isFinite(maxAmount))) {
                System.out.println("Amounts must be valid numbers.");
                return;
            }

            if (minAmount != null && maxAmount != null
                    && minAmount > maxAmount) {
                System.out.println("Minimum amount cannot exceed maximum amount.");
                return;
            }

            ArrayList<Transaction> sortedTransactions =
                    new ArrayList<>(ledger.getTransactions());

            Collections.sort(sortedTransactions,
                    Comparator.comparing(Transaction::getDate)
                            .thenComparing(Transaction::getTime)
                            .reversed());

            ledger.displayTableHeader();
            boolean found = false;

            for (Transaction transaction : sortedTransactions) {
                LocalDate transactionDate =
                        LocalDate.parse(transaction.getDate());

                if (startDate != null && transactionDate.isBefore(startDate)) {
                    continue;
                }

                if (endDate != null && transactionDate.isAfter(endDate)) {
                    continue;
                }

                if (!transaction.getDescription().toLowerCase()
                        .contains(description)) {
                    continue;
                }

                if (!transaction.getVendor().toLowerCase()
                        .contains(vendor)) {
                    continue;
                }

                if (minAmount != null && transaction.getAmount() < minAmount) {
                    continue;
                }

                if (maxAmount != null && transaction.getAmount() > maxAmount) {
                    continue;
                }

                ledger.displayTransaction(transaction);
                found = true;
            }

            if (!found) {
                System.out.println("No matching transactions found.");
            }

        } catch (java.time.format.DateTimeParseException e) {
            System.out.println("Invalid date. Use YYYY-MM-DD.");

        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a number.");
        }
    }
    // Authenticate user before accessing StationLedger
    public static User showLogin(Scanner scanner, UserManager userManager) {

        while (true) {
            // Display login screen
            System.out.println();
            System.out.println(MUTED + "  ----------------------------------------" + RESET);
            System.out.println(ACCENT + centerText("STATIONLEDGER", 44) + RESET);
            System.out.println(MUTED + centerText("Business Financial Management", 44) + RESET);
            System.out.println(MUTED + "  ----------------------------------------" + RESET);
            System.out.println();
            System.out.println(centerText("USER LOGIN", 44));
            System.out.println();
            System.out.println(MUTED + "  Enter X to exit StationLedger" + RESET);
            System.out.println();
            System.out.print(ACCENT + "  Username > " + RESET);
            String username = scanner.nextLine().trim();

            if (username.equalsIgnoreCase("X")) {
                return null;
            }

            String password = getPassword();

            if (password == null) {
                return null;
            }

            User user = userManager.login(username, password);

            if (user != null) {
                System.out.println("Login successful!");
                System.out.println("Welcome, " + user.getUsername() + "!");
                return user;
            }

            System.out.println("Invalid username or password. Please try again.");
        }
    }
    // Read user input and allow cancellation
    public static String getInput(Scanner scanner, String prompt) {
        System.out.println("(Enter X to cancel and return to the previous menu)");
        System.out.print(prompt);

        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("X")) {
            return null;
        }

        return input;
    }
    // Check manager authorization for restricted actions
    public static boolean authorizeManager(User currentUser, UserManager userManager) {

        if (currentUser.isManager()) {
            return true;
        }

        System.out.println("Manager authorization required.");

        String password = getPassword();

        if (password == null) {
            System.out.println("Authorization cancelled.");
            return false;
        }

        if (userManager.verifyManagerPassword(password)) {
            System.out.println("Manager authorization successful.");
            return true;
        }

        System.out.println("Incorrect manager password.");
        return false;
    }
    // Read a password without displaying the characters
    public static String getPassword() {
        JPasswordField passwordField = new JPasswordField();

        // Displays password popup
        int result = JOptionPane.showConfirmDialog(
                null,
                passwordField,
                "StationLedger - Enter Password",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
        // Checks whether the user cancelled upon closing or clicking cancel
        if (result != JOptionPane.OK_OPTION) {
            return null;
        }
        // Returns the entered password
        return new String(passwordField.getPassword());
    }
    // Center text within a fixed width
    public static String centerText(String text, int width) {
        int padding = Math.max(0, (width - text.length()) / 2);
        return " ".repeat(padding) + text;
    }
    // Display a formatted report heading
    public static void showReportHeader(String title) {
        System.out.println();
        System.out.println(MUTED + "  ----------------------------------------" + RESET);
        System.out.println(ACCENT + centerText("STATIONLEDGER", 44) + RESET);
        System.out.println(MUTED + "  ----------------------------------------" + RESET);
        System.out.println();
        System.out.println(centerText(title, 44));
        System.out.println(MUTED + "  ----------------------------------------" + RESET);
        System.out.println();
    }
}
