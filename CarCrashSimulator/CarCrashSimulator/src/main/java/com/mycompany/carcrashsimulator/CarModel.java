package com.mycompany.carcrashsimulator;

public class CarModel {
    private String name;
    private double mass;
    private double safetyFactor;
    private double hardness;

    // Constructor để khởi tạo các giá trị
    public CarModel(String name, double mass, double safetyFactor, double hardness) {
        this.name = name;
        this.mass = mass;
        this.safetyFactor = safetyFactor;
        this.hardness = hardness;
    }

    // Các phương thức getter để lấy thông tin của CarModel
    public String getName() {
        return name;
    }

    public double getMass() {
        return mass;
    }

    public double getSafetyFactor() {
        return safetyFactor;
    }

    public double getHardness() {
        return hardness;
    }
}
