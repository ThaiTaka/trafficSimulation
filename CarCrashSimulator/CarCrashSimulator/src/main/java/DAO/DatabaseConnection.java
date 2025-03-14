package DAO;

import Models.Vehicle;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:database.db"; // Đường dẫn tới database

    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Vehicles")) {

            while (rs.next()) {
                String name = rs.getString("name");
                double mass = rs.getDouble("mass");
                double speed = rs.getDouble("speed");
                int passengers = rs.getInt("passengers");
                double safetyFactor = rs.getDouble("safetyFactor");
                double hardness = rs.getDouble("hardness");

                vehicles.add(new Vehicle(name, mass, speed, passengers, safetyFactor, hardness));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vehicles;
    }
}