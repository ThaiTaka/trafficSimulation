package Models;

public class Vehicle {
    private int id;
    private String name;
    private double weight;
    private double speed;
    private int passengers;
    private double safetyFactor;
    private double hardness;

    public Vehicle(String name, double weight, double speed, int id, double safetyFactor, double hardness) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.speed = speed;
        this.passengers = passengers;
        this.safetyFactor = safetyFactor;
        this.hardness = hardness;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    public double getSpeed() {
        return speed;
    }

    public int getPassengers() {
        return passengers;
    }

    public double getSafetyFactor() {
        return safetyFactor;
    }

    public double getHardness() {
        return hardness;
    }
}
