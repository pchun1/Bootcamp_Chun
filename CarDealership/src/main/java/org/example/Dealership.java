package org.example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Dealership {

    // Dealership information
    private String name;
    private String address;
    private String phone;

    // Store all vehicles in inventory
    private ArrayList<Vehicle> inventory;

    // Constructor
    public Dealership(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;

        // Initialize inventory
        inventory = new ArrayList<>();
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Get all vehicles
    public List<Vehicle> getAllVehicles() {
        return inventory;
    }

    // Add a vehicle to inventory
    public void addVehicle(Vehicle vehicle) {
        inventory.add(vehicle);
    }

    // Remove a vehicle from inventory
    public void removeVehicle(Vehicle vehicle) {

    }

    // Search vehicles by price range
    public List<Vehicle> getVehiclesByPrice(BigDecimal min, BigDecimal max) {
        return null;
    }

    // Search vehicles by make and model
    public List<Vehicle> getVehiclesByMakeModel(String make, String model) {
        return null;
    }

    // Search vehicles by year range
    public List<Vehicle> getVehiclesByYear(int min, int max) {
        return null;
    }

    // Search vehicles by color
    public List<Vehicle> getVehiclesByColor(String color) {
        return null;
    }

    // Search vehicles by mileage range
    public List<Vehicle> getVehiclesByMileage(int min, int max) {
        return null;
    }

    // Search vehicles by type
    public List<Vehicle> getVehiclesByType(String vehicleType) {
        return null;
    }
}