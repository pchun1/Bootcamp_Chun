
package org.example;

public class Book {

    //put in your properties, constructor, getters/setters

    // Properties
    private int id;
    private String isbn;
    private String title;
    private boolean isCheckedOut;
    private String checkedOutTo;

    // Default Constructor
    public Book() {
        this.isCheckedOut = false;
        this.checkedOutTo = "";
    }

    // Constructor with Parameters
    public Book(int id, String isbn, String title) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.isCheckedOut = false;
        this.checkedOutTo = "";
    }

    // Getters and Setters
    public int getId() {
        return id;
    }


    public String getIsbn() {
        return isbn;
    }


    public String getTitle() {
        return title;
    }


    public boolean isCheckedOut() {
        return isCheckedOut;
    }


    public String getCheckedOutTo() {
        return checkedOutTo;
    }


    //these are the custom methods you'll need to check in and
    //check out a book.

    public void checkOut(String name) {
        //switch the boolean to true or false
        this.isCheckedOut = true;

        //need to update checkedOutTo to the name
        this.checkedOutTo = name;
    }

    public void checkIn() {
        // Mark the book as available
        this.isCheckedOut = false;

        // Remove the previous borrower's name
        this.checkedOutTo = "";
    }
}
