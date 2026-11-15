package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;

public class DealershipFileManager {

    // Load dealership information and inventory from CSV
    public Dealership getDealership() {

        Dealership dealership = null;

        try (BufferedReader reader = new BufferedReader(
                new FileReader("src/main/resources/inventory.csv"))) {

            // Read dealership information from first line
            String line = reader.readLine();

            if (line != null) {

                String[] dealershipData = line.split("\\|");

                String name = dealershipData[0];
                String address = dealershipData[1];
                String phone = dealershipData[2];

                // Create dealership object
                dealership = new Dealership(name, address, phone);

                // Read vehicle information from remaining lines
                while ((line = reader.readLine()) != null) {

                    String[] vehicleData = line.split("\\|");

                    int vin = Integer.parseInt(vehicleData[0]);
                    int year = Integer.parseInt(vehicleData[1]);
                    String make = vehicleData[2];
                    String model = vehicleData[3];
                    String vehicleType = vehicleData[4];
                    String color = vehicleData[5];
                    int odometer = Integer.parseInt(vehicleData[6]);
                    BigDecimal price = new BigDecimal(vehicleData[7]);

                    // Create vehicle object
                    Vehicle vehicle = new Vehicle(vin, year, make, model,
                            vehicleType, color, odometer, price);

                    // Add vehicle to dealership inventory
                    dealership.addVehicle(vehicle);
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading dealership file: " + e.getMessage());
        }

        return dealership;
    }

    // Save dealership information and inventory to CSV
    public void saveDealership(Dealership dealership) {

    }
}