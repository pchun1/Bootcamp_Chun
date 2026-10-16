package org.example;

import java.util.Scanner;

public class Calculator3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the monthly payout amount: ");
        double monthlyPayout = scanner.nextDouble();

        System.out.print("Enter the annual interest rate: ");
        double annualInterestRate = scanner.nextDouble();

        System.out.print("Enter the number of years to pay out: ");
        int years = scanner.nextInt();

        double monthlyInterestRate = annualInterestRate / 12 / 100;
        int totalPayments = years * 12;

        double powerResult = Math.pow(1 + monthlyInterestRate, -totalPayments);
        //(1 + r)^(-n) ALSO (1 + monthlyInterestRate) ^ (-totalPayments)

        double presentValue = monthlyPayout * ((1 - powerResult) / monthlyInterestRate);
        //monthlyPayout × ((1 - powerResult) / r)

        System.out.printf("Present Value: $%.2f%n", presentValue);

        scanner.close();
    }
}
