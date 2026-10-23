package org.example;

import java.util.Scanner;

public class Calculator1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the principal amount: ");
        double principal = scanner.nextDouble();

        System.out.println("Enter the annual interest rate: ");
        double annualInterestRate = scanner.nextDouble();

        System.out.println("Enter the loan length in years: ");
        int loanLengthYears = scanner.nextInt();

        double monthlyInterestRate = annualInterestRate / 12 / 100;
        int monthlyPayments = loanLengthYears * 12;

        double powerResult = Math.pow(1 + monthlyInterestRate, monthlyPayments);
        // (1 + i) ^ n

        double monthlyPayment = (principal * monthlyInterestRate * powerResult) / (powerResult -1);
        //(P * r * powerResult) / (powerResult - 1)
        // ------------------
        //  (powerResult - 1)

        double totalPaid = monthlyPayment * monthlyPayments;
        double totalInterest = totalPaid - principal;
        // (M * n) - P

        System.out.printf("Monthly Payments: $%.2f%n", monthlyPayment);
        System.out.printf("Total Interest: $%.2f%n", totalInterest);

        scanner.close();
    }
}
