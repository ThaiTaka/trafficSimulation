package DAO;

import Models.Vehicle;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {
    private static final String DB_URL = "jdbc:sqlite:vehicles.db";
    
    public static List<String> getAllVehicleModels() {
        List<String> models = new ArrayList<>();
        String query = "SELECT model FROM VehicleModels";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                models.add(rs.getString("model"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return models;
    }
    
    public static Vehicle getVehicleByModel(String model) {
        String query = "SELECT * FROM VehicleModels WHERE model = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, model);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Vehicle(
                    rs.getString("model"),
                    rs.getDouble("weight"),
                    rs.getDouble("speed"),
                    rs.getInt("passengers"),
                    rs.getDouble("safetyFactor"),
                    rs.getDouble("hardness")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}