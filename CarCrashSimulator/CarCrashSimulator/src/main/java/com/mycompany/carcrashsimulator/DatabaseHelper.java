package com.mycompany.carcrashsimulator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:car_models.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static List<CarModel> getCarModels() {
        List<CarModel> models = new ArrayList<>();
        String sql = "SELECT name, mass, safety_factor, hardness FROM car_models";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String name = rs.getString("name");
                double mass = rs.getDouble("mass");
                double safetyFactor = rs.getDouble("safety_factor");
                double hardness = rs.getDouble("hardness");
                models.add(new CarModel(name, mass, safetyFactor, hardness));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return models;
    }
}
