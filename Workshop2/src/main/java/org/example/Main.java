
package org.example;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // You will only need ONE scanner
        Scanner scanner = new Scanner(System.in);

        // Create an array containing 20 Book objects
        Book[] books = new Book[20];

        books[0] = new Book(1, "9780743273565", "The Great Gatsby");
        books[1] = new Book(2, "9780061120084", "To Kill a Mockingbird");
        books[2] = new Book(3, "9780451524935", "1984");
        books[3] = new Book(4, "9780547928227", "The Hobbit");
        books[4] = new Book(5, "9780316769488", "The Catcher in the Rye");
        books[5] = new Book(6, "9780141439518", "Pride and Prejudice");
        books[6] = new Book(7, "9780140449136", "The Odyssey");
        books[7] = new Book(8, "9780553380163", "A Brief History of Time");
        books[8] = new Book(9, "9780439023481", "The Hunger Games");
        books[9] = new Book(10, "9780439139601", "Harry Potter and the Goblet of Fire");
        books[10] = new Book(11, "9780307474278", "The Da Vinci Code");
        books[11] = new Book(12, "9780062316097", "Sapiens");
        books[12] = new Book(13, "9780140283334", "Lord of the Flies");
        books[13] = new Book(14, "9780747532699", "Harry Potter and the Philosopher's Stone");
        books[14] = new Book(15, "9780060850524", "Brave New World");
        books[15] = new Book(16, "9780140449266", "The Count of Monte Cristo");
        books[16] = new Book(17, "9781451673319", "Fahrenheit 451");
        books[17] = new Book(18, "9780307387899", "The Road");
        books[18] = new Book(19, "9780553213119", "Frankenstein");
        books[19] = new Book(20, "9780142437230", "Don Quixote");

        // Keep running until the user chooses to exit
        while (true) {

            // Main menu
            System.out.println("\n=== LIBRARY HOME SCREEN ===");
            System.out.println("1. Show Available Books");
            System.out.println("2. Show Checked Out Books");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            // Read the user's menu choice
            int userInput;

            try {
                userInput = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            // Switch between the three menu options
            switch (userInput) {

                // shows available books
                case 1:

                    System.out.println("\n=== AVAILABLE BOOKS ===");

                    // Loop through the Book array
                    for (int i = 0; i < books.length; i++) {

                        // Only show books that are not checked out
                        if (!books[i].isCheckedOut()) {

                            System.out.println(
                                    "ID: " + books[i].getId()
                                            + " | ISBN: " + books[i].getIsbn()
                                            + " | Title: " + books[i].getTitle()
                            );
                        }
                    }

                    // prompts user for what book to checkout
                    System.out.print("\nEnter Book ID to check out (X to go back): ");
                    String checkoutChoice = scanner.nextLine();

                    // input X to return to main menu
                    if (checkoutChoice.equalsIgnoreCase("X")) {
                        break;
                    }

                    try {

                        // parsing string to integer
                        int bookId = Integer.parseInt(checkoutChoice);

                        boolean bookFound = false;

                        // searches array for matching book
                        for (int i = 0; i < books.length; i++) {

                            if (books[i].getId() == bookId) {

                                bookFound = true;

                                // makes sure the book is available
                                if (!books[i].isCheckedOut()) {

                                    System.out.print("Enter your name: ");
                                    String name = scanner.nextLine();

                                    if (name.trim().isEmpty()) {
                                        System.out.println("Name cannot be empty.");
                                    } else {

                                        // calls the book class in checkOut method
                                        books[i].checkOut(name);

                                        System.out.println(
                                                "You checked out: " + books[i].getTitle()
                                        );
                                    }

                                } else {
                                    System.out.println("This book is already checked out.");
                                }


                                break;
                            }
                        }

                        if (!bookFound) {
                            System.out.println("Book ID not found.");
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Invalid Book ID.");
                    }

                    break;

                // Shows checked out books
                case 2:

                    System.out.println("\n=== CHECKED OUT BOOKS ===");

                    boolean anyCheckedOut = false;

                    // Loop through the Book array
                    for (int i = 0; i < books.length; i++) {

                        // Only show books that are checked out
                        if (books[i].isCheckedOut()) {

                            anyCheckedOut = true;

                            System.out.println(
                                    "ID: " + books[i].getId()
                                            + " | ISBN: " + books[i].getIsbn()
                                            + " | Title: " + books[i].getTitle()
                                            + " | Checked Out To: " + books[i].getCheckedOutTo()
                            );
                        }
                    }

                    if (!anyCheckedOut) {
                        System.out.println("No books are currently checked out.");
                    }

                    // Checked out books submenu
                    System.out.println("\n1. Check In a Book");
                    System.out.println("2. Back to Home Screen");
                    System.out.print("Enter your choice: ");

                    int checkedOutChoice;

                    try {
                        checkedOutChoice = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid number.");
                        break;
                    }

                    // Switch for the checked out books menu
                    switch (checkedOutChoice) {

                        // checks in a book
                        case 1:

                            System.out.print("Enter Book ID to check in (X to go back): ");
                            String checkinChoice = scanner.nextLine();

                            // Return to main menu
                            if (checkinChoice.equalsIgnoreCase("X")) {
                                break;
                            }

                            try {

                                int bookId = Integer.parseInt(checkinChoice);
                                boolean bookFound = false;

                                // Search for the matching book
                                for (int i = 0; i < books.length; i++) {

                                    if (books[i].getId() == bookId) {

                                        bookFound = true;

                                        // Make sure the book is checked out
                                        if (books[i].isCheckedOut()) {

                                            // Calls the book in checkIn
                                            books[i].checkIn();

                                            System.out.println(
                                                    "You checked in: " + books[i].getTitle()
                                            );

                                        } else {
                                            System.out.println("This book is not checked out.");
                                        }

                                        // Stop searching
                                        break;
                                    }
                                }

                                if (!bookFound) {
                                    System.out.println("Book ID not found.");
                                }

                            } catch (NumberFormatException e) {
                                System.out.println("Invalid Book ID.");
                            }

                            break;

                        // back to homescreen
                        case 2:
                            break;

                        // NA choice in submenu
                        default:
                            System.out.println("Invalid choice.");
                            break;
                    }

                    break;

                // Exit
                case 3:

                    System.out.println("Thank you for using the library!");

                    // Close scanner and end the main method
                    scanner.close();
                    return;

                // NA Choice
                default:

                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
}
