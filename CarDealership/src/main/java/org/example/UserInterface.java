package org.example;

import java.util.List;
import java.util.Scanner;

public class UserInterface {

    // Dealership information
    private Dealership dealership;

    // Constructor
    public UserInterface() {

    }

    // Load dealership from CSV
    private void init() {

        DealershipFileManager fileManager = new DealershipFileManager();
        this.dealership = fileManager.getDealership();
    }

    // Display main menu
    public void display() {

        init();

        Scanner scanner = new Scanner(System.in);
        int choice = 0;

        while (choice != 99) {

            System.out.println("\n===== AUDI MOTORHAUS =====");
            System.out.println("1 - Find vehicles within a price range");
            System.out.println("2 - Find vehicles by make / model");
            System.out.println("3 - Find vehicles by year range");
            System.out.println("4 - Find vehicles by color");
            System.out.println("5 - Find vehicles by mileage range");
            System.out.println("6 - Find vehicles by type");
            System.out.println("7 - List all vehicles");
            System.out.println("8 - Add a vehicle");
            System.out.println("9 - Remove a vehicle");
            System.out.println("99 - Quit");
            System.out.print("Enter your choice: ");

            // Read user input
            try {
                choice = Integer.parseInt(scanner.nextLine());

                // Process menu selection
                switch (choice) {

                    case 1:
                        processGetByPriceRequest();
                        break;

                    case 2:
                        processGetByMakeModelRequest();
                        break;

                    case 3:
                        processGetByYearRequest();
                        break;

                    case 4:
                        processGetByColorRequest();
                        break;

                    case 5:
                        processGetByMileageRequest();
                        break;

                    case 6:
                        processGetByVehicleTypeRequest();
                        break;

                    case 7:
                        processGetAllVehiclesRequest();
                        break;

                    case 8:
                        processAddVehicleRequest();
                        break;

                    case 9:
                        processRemoveVehicleRequest();
                        break;

                    case 99:
                        System.out.println("Exiting dealership application...");
                        break;

                    default:
                        System.out.println("Invalid selection. Please try again.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    // Display list of vehicles
    private void displayVehicles(List<Vehicle> vehicles) {

        for (Vehicle vehicle : vehicles) {

            System.out.println(
                    vehicle.getVin() + " | " +
                            vehicle.getYear() + " | " +
                            vehicle.getMake() + " | " +
                            vehicle.getModel() + " | " +
                            vehicle.getVehicleType() + " | " +
                            vehicle.getColor() + " | " +
                            vehicle.getOdometer() + " | $" +
                            vehicle.getPrice()
            );
        }
    }

    // List all dealership vehicles
    private void processGetAllVehiclesRequest() {

        List<Vehicle> vehicles = dealership.getAllVehicles();
        displayVehicles(vehicles);
    }

    // Search vehicles by price
    private void processGetByPriceRequest() {

    }

    // Search vehicles by make and model
    private void processGetByMakeModelRequest() {

    }

    // Search vehicles by year
    private void processGetByYearRequest() {

    }

    // Search vehicles by color
    private void processGetByColorRequest() {

    }

    // Search vehicles by mileage
    private void processGetByMileageRequest() {

    }

    // Search vehicles by type
    private void processGetByVehicleTypeRequest() {

    }

    // Add vehicle
    private void processAddVehicleRequest() {

    }

    // Remove vehicle
    private void processRemoveVehicleRequest() {

    }
}
