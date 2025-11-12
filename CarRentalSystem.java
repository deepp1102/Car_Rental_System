package main;

import db.DBConnection;
import java.sql.*;
import java.util.*;

public class CarRentalSystem {
    static Scanner sc = new Scanner(System.in);
    static Connection conn = DBConnection.getConnection();

    public static void main(String[] args) {
        System.out.println("====== Car Rental System ======");
        while (true) {
            System.out.println("\n1. Admin Login");
            System.out.println("2. Customer Login");
            System.out.println("3. Customer Registration");
            System.out.println("4. Exit");
            System.out.print("Choose option: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> adminMenu();
                case 2 -> customerLogin();
                case 3 -> registerCustomer();
                case 4 -> {
                    System.out.println("Thank you! Exiting...");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    // ================== ADMIN FUNCTIONS ==================
    static void adminMenu() {
        System.out.print("Enter admin password: ");
        String pass = sc.next();
        if (!pass.equals("admin123")) {
            System.out.println("Wrong password!");
            return;
        }

        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Add Car");
            System.out.println("2. View Cars");
            System.out.println("3. Update Car Availability");
            System.out.println("4. Delete Car");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter choice: ");
            int ch = sc.nextInt();

            switch (ch) {
                case 1 -> addCar();
                case 2 -> viewCars();
                case 3 -> updateCarAvailability();
                case 4 -> deleteCar();
                case 5 -> { return; }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    static void addCar() {
        try {
            System.out.print("Enter Car Name: ");
            sc.nextLine();
            String name = sc.nextLine();
            System.out.print("Enter Model: ");
            String model = sc.next();
            System.out.print("Enter Price per Day: ");
            double price = sc.nextDouble();

            String query = "INSERT INTO cars (car_name, model, price_per_day, available) VALUES (?, ?, ?, TRUE)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, model);
            ps.setDouble(3, price);
            ps.executeUpdate();
            System.out.println("✅ Car added successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void viewCars() {
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM cars");
            System.out.println("\n--- Available Cars ---");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("car_id") +
                        ", Name: " + rs.getString("car_name") +
                        ", Model: " + rs.getString("model") +
                        ", Price: " + rs.getDouble("price_per_day") +
                        ", Available: " + rs.getBoolean("available"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void updateCarAvailability() {
        try {
            System.out.print("Enter Car ID: ");
            int id = sc.nextInt();
            System.out.print("Enter Availability (true/false): ");
            boolean status = sc.nextBoolean();

            String query = "UPDATE cars SET available=? WHERE car_id=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setBoolean(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
            System.out.println("✅ Car availability updated!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void deleteCar() {
        try {
            System.out.print("Enter Car ID to delete: ");
            int id = sc.nextInt();
            String query = "DELETE FROM cars WHERE car_id=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("✅ Car deleted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================== CUSTOMER FUNCTIONS ==================
    static void registerCustomer() {
        try {
            System.out.print("Enter Name: ");
            sc.nextLine();
            String name = sc.nextLine();
            System.out.print("Enter Email: ");
            String email = sc.next();
            System.out.print("Enter Password: ");
            String pass = sc.next();

            String query = "INSERT INTO customers (name, email, password) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, pass);
            ps.executeUpdate();
            System.out.println("✅ Registration successful!");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("⚠️ Email already exists!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void customerLogin() {
        try {
            System.out.print("Enter Email: ");
            String email = sc.next();
            System.out.print("Enter Password: ");
            String pass = sc.next();

            String query = "SELECT * FROM customers WHERE email=? AND password=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int custId = rs.getInt("customer_id");
                System.out.println("✅ Login successful! Welcome " + rs.getString("name"));
                customerMenu(custId);
            } else {
                System.out.println("❌ Invalid credentials!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void customerMenu(int custId) {
        while (true) {
            System.out.println("\n--- Customer Menu ---");
            System.out.println("1. View Available Cars");
            System.out.println("2. Rent a Car");
            System.out.println("3. Return a Car");
            System.out.println("4. View Rental History");
            System.out.println("5. Logout");
            System.out.print("Enter choice: ");
            int ch = sc.nextInt();

            switch (ch) {
                case 1 -> viewAvailableCars();
                case 2 -> rentCar(custId);
                case 3 -> returnCar(custId);
                case 4 -> viewRentalHistory(custId);
                case 5 -> { return; }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    static void viewAvailableCars() {
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM cars WHERE available=TRUE");
            System.out.println("\n--- Available Cars ---");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("car_id") +
                        ", Name: " + rs.getString("car_name") +
                        ", Model: " + rs.getString("model") +
                        ", Price: " + rs.getDouble("price_per_day"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void rentCar(int custId) {
        try {
            viewAvailableCars();
            System.out.print("Enter Car ID to rent: ");
            int carId = sc.nextInt();
            System.out.print("Enter start date (YYYY-MM-DD): ");
            String start = sc.next();
            System.out.print("Enter end date (YYYY-MM-DD): ");
            String end = sc.next();

            // Get price
            String getPrice = "SELECT price_per_day FROM cars WHERE car_id=?";
            PreparedStatement ps1 = conn.prepareStatement(getPrice);
            ps1.setInt(1, carId);
            ResultSet rs = ps1.executeQuery();
            if (!rs.next()) {
                System.out.println("Invalid Car ID!");
                return;
            }

            double price = rs.getDouble("price_per_day");

            long diff = (java.sql.Date.valueOf(end).getTime() - java.sql.Date.valueOf(start).getTime()) / (1000 * 60 * 60 * 24);
            if (diff <= 0) diff = 1;
            double total = diff * price;

            String rentQuery = "INSERT INTO rentals (customer_id, car_id, start_date, end_date, total_amount) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps2 = conn.prepareStatement(rentQuery);
            ps2.setInt(1, custId);
            ps2.setInt(2, carId);
            ps2.setString(3, start);
            ps2.setString(4, end);
            ps2.setDouble(5, total);
            ps2.executeUpdate();

            // Update availability
            PreparedStatement ps3 = conn.prepareStatement("UPDATE cars SET available=FALSE WHERE car_id=?");
            ps3.setInt(1, carId);
            ps3.executeUpdate();

            System.out.println("✅ Car rented successfully! Total: ₹" + total);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void returnCar(int custId) {
        try {
            System.out.print("Enter Car ID to return: ");
            int carId = sc.nextInt();

            String query = "UPDATE cars SET available=TRUE WHERE car_id=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, carId);
            ps.executeUpdate();

            System.out.println("✅ Car returned successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void viewRentalHistory(int custId) {
        try {
            String query = "SELECT * FROM rentals WHERE customer_id=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, custId);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n--- Rental History ---");
            while (rs.next()) {
                System.out.println("Rental ID: " + rs.getInt("rental_id") +
                        ", Car ID: " + rs.getInt("car_id") +
                        ", Start: " + rs.getDate("start_date") +
                        ", End: " + rs.getDate("end_date") +
                        ", Total: ₹" + rs.getDouble("total_amount"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}