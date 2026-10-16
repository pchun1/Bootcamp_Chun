package org.example;

import java.util.Scanner;

public class Calculator2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the deposit amount: ");
        double principal = scanner.nextDouble();

        System.out.print("Enter the annual interest rate: ");
        double annualInterestRate = scanner.nextDouble();

        System.out.print("Enter the number of years: ");
        int years = scanner.nextInt();

        double dailyRate = annualInterestRate / 100 / 365;
        int totalDays = years * 365;

        double powerResult = Math.pow(1 + dailyRate, totalDays);
        // (1 + dailyRate) ^ totalDays

        double futureValue = principal * powerResult;
        //FV = P * powerResult

        double totalInterest = futureValue - principal;

        System.out.printf("Future Value: $%.2f%n", futureValue);
        System.out.printf("Total Interest Earned: $%.2f%n", totalInterest);

        scanner.close();
    }
}
