/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.carcrashsimulator;
import com.formdev.flatlaf.FlatDarkLaf;
import weka.core.Instances;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    private JTextArea resultArea;
    private DecimalFormat df = new DecimalFormat("#.##");

    private InputPanel inputPanel;
    private ButtonPanel buttonPanel;

    // Các biến lưu thông số mô phỏng
    private double car1X, car1Y, car2X, car2Y;
    private double car1Speed, car2Speed, car1Mass, car2Mass;
    private int numPassengersCar1, numPassengersCar2;
    private double safetyFactorCar1, safetyFactorCar2;
    private double hardnessCar1, hardnessCar2, hardnessObject;
    private double coefficientOfRestitution;
    private String scenarioType = "Va chạm xe - xe";
    private String obstacleType = "Cây cối";

    private List<Spark> sparks = new ArrayList<>();
    private List<Debris> debrisList = new ArrayList<>();
    private Timer animationTimer;
    private boolean collisionOccurred = false;
    private boolean carsStopped = false;
    private int stopTimer = 0;
    private boolean simulationStarted = false;
    private Rectangle2D obstacleRect = new Rectangle2D.Double(800, 300, 50, 100);

    // ----- COLORS -----
    private Color backgroundColor = new Color(20, 20, 25);
    private Color panelBackgroundColor = new Color(35, 35, 40);
    private Color textColor = new Color(220, 220, 220);
    private Color inputFieldBackgroundColor = new Color(50, 50, 55);
    private Color buttonColor = new Color(70, 130, 180);
    private Color buttonHoverColor = new Color(90, 150, 200);
    private Color roadColor = new Color(70, 70, 70);
    private Color car1Color = new Color(0, 123, 255);
    private Color car2Color = new Color(255, 0, 0);
    private Color carWindowColor = new Color(180, 200, 220);
    // ----- END COLORS -----

    private boolean isDeformedCar1 = false;
    private boolean isDeformedCar2 = false;
    private int deformationTimer = 0;
    private int deformationDuration = 25;
    private int deformationIntensity = 20;

    private CarAnimationPanel animationPanel;

    public MainFrame() {
        setTitle("Mô phỏng Va chạm Xe - Tình huống đa dạng");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        animationPanel = new CarAnimationPanel(this);
        add(animationPanel, BorderLayout.CENTER);

        resultArea = new JTextArea(12, 50);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        resultArea.setBackground(backgroundColor);
        resultArea.setForeground(textColor);
        add(new JScrollPane(resultArea), BorderLayout.SOUTH);

        inputPanel = new InputPanel(this);
        JScrollPane inputScrollPane = new JScrollPane(inputPanel);
        inputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        buttonPanel = new ButtonPanel(this);
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.add(inputScrollPane, BorderLayout.CENTER);
        controlPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(controlPanel, BorderLayout.EAST);
    }

    public void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Getter để cho các thành phần khác (ví dụ ButtonPanel) truy cập InputPanel
    public InputPanel getInputPanel() {
        return inputPanel;
    }

    public void startSimulation() {
        try {
            simulationStarted = true;
            collisionOccurred = false;
            carsStopped = false;
            stopTimer = 0;
            deformationTimer = 0;
            sparks.clear();
            debrisList.clear();
            resultArea.setText("");

            car1Mass = inputPanel.getMassCar1();
            car1Speed = inputPanel.getSpeedCar1();
            car2Mass = inputPanel.getMassCar2();
            car2Speed = inputPanel.getSpeedCar2();
            numPassengersCar1 = inputPanel.getNumPassengersCar1();
            numPassengersCar2 = inputPanel.getNumPassengersCar2();
            safetyFactorCar1 = inputPanel.getSafetyFactorCar1();
            safetyFactorCar2 = inputPanel.getSafetyFactorCar2();
            hardnessCar1 = inputPanel.getHardnessCar1();
            hardnessCar2 = inputPanel.getHardnessCar2();
            hardnessObject = inputPanel.getHardnessObject();
            coefficientOfRestitution = inputPanel.getCoefficientOfRestitution();

            if (scenarioType.equals("Va chạm xe - chướng ngại vật")) {
                car1X = 50;
                car1Y = 350;
                if (obstacleType.equals("Cây cối")) {
                    obstacleRect = new Rectangle2D.Double(800, 280, 60, 120);
                } else {
                    obstacleRect = new Rectangle2D.Double(800, 300, 50, 100);
                }
            } else {
                car1X = 50;
                car1Y = 350;
                car2X = 800;
                car2Y = 350;
            }

            if (animationTimer != null && animationTimer.isRunning()) {
                animationTimer.stop();
            }
            startAnimationInner();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ!");
        }
    }

    public void resetSimulation() {
        if (animationTimer != null) {
            animationTimer.stop();
        }
        resultArea.setText("");
        sparks.clear();
        debrisList.clear();
        collisionOccurred = false;
        carsStopped = false;
        stopTimer = 0;
        deformationTimer = 0;
        simulationStarted = false;
        animationPanel.repaint();
    }

    private void startAnimationInner() {
        animationTimer = new Timer(20, e -> {
            if (!collisionOccurred) {
                if (scenarioType.equals("Va chạm xe - chướng ngại vật")) {
                    car1X += (car1Speed / 10);
                    Rectangle2D car1Rect = new Rectangle2D.Double(car1X, car1Y, 140, 60);
                    if (car1Rect.intersects(obstacleRect)) {
                        collisionOccurred = true;
                        isDeformedCar1 = true;
                        calculateCrashResults();
                        animationPanel.createSparks();
                        animationPanel.createDebris();
                        car1Speed = 0;
                    }
                } else {
                    car1X += (car1Speed / 10);
                    car2X -= (car2Speed / 10);
                    Rectangle2D car1Rect = new Rectangle2D.Double(car1X, car1Y, 140, 60);
                    Rectangle2D car2Rect = new Rectangle2D.Double(car2X, car2Y, 140, 60);
                    if (car1Rect.intersects(car2Rect)) {
                        collisionOccurred = true;
                        isDeformedCar1 = true;
                        isDeformedCar2 = true;
                        calculateCrashResults();
                        animationPanel.createSparks();
                        animationPanel.createDebris();
                        car1Speed = 0;
                        car2Speed = 0;
                    }
                }
            } else {
                if (!carsStopped) {
                    stopTimer++;
                    if (stopTimer >= 50) {
                        carsStopped = true;
                    }
                }
            }
            if (isDeformedCar1 || isDeformedCar2) {
                deformationTimer++;
                if (deformationTimer >= deformationDuration) {
                    deformationTimer = 0;
                    isDeformedCar1 = false;
                    isDeformedCar2 = false;
                }
            }
            updateSparks();
            updateDebris();
            animationPanel.repaint();
        });
        animationTimer.start();
    }

    private void updateSparks() {
        sparks.removeIf(spark -> spark.getAlpha() < 5 || spark.getSize() < 0.5);
        for (Spark spark : sparks) {
            spark.update();
        }
    }

    private void updateDebris() {
        debrisList.removeIf(debris -> debris.alpha < 5 || debris.size < 0.5);
        for (Debris d : debrisList) {
            d.update();
        }
    }

    private void calculateCrashResults() {
        double ke1Initial = 0.5 * car1Mass * Math.pow(car1Speed / 3.6, 2);
        double ke2Initial = 0.5 * car2Mass * Math.pow(car2Speed / 3.6, 2);

        double ke1Final, ke2Final;
        if (scenarioType.equals("Va chạm xe - xe")) {
            double totalMass = car1Mass + car2Mass;
            double v1Final = ((car1Mass - coefficientOfRestitution * car2Mass) / totalMass * (car1Speed / 3.6))
                    + ((1 + coefficientOfRestitution) * car2Mass / totalMass * (car2Speed / 3.6));
            double v2Final = ((1 + coefficientOfRestitution) * car1Mass / totalMass * (car1Speed / 3.6))
                    + ((car2Mass - coefficientOfRestitution * car1Mass) / totalMass * (car2Speed / 3.6));
            ke1Final = 0.5 * car1Mass * Math.pow(v1Final, 2);
            ke2Final = 0.5 * car2Mass * Math.pow(v2Final, 2);
        } else {
            ke1Final = 0.1 * ke1Initial;
            ke2Final = 0.0;
        }

        double safetyPercentageCar1 = (1 - ke1Final / ke1Initial) * 100;
        double safetyPercentageCar2 = (scenarioType.equals("Va chạm xe - xe")) ? (1 - ke2Final / ke2Initial) * 100 : 0;
        safetyPercentageCar1 = Math.max(0, Math.min(100, safetyPercentageCar1));
        safetyPercentageCar2 = Math.max(0, Math.min(100, safetyPercentageCar2));

        // Tích hợp ML: Tải cấu trúc dữ liệu từ file ARFF và sử dụng mô hình đã lưu để dự đoán lời khuyên.
        Instances structure = SafetyAdvisor.loadStructure("safety_structure.arff"); // file ARFF định nghĩa cấu trúc các thuộc tính
        SafetyAdvisor advisor = new SafetyAdvisor("safety_advisor.model", structure);
        String mlAdvice = advisor.getAdvice(car1Mass, car1Speed / 3.6, safetyFactorCar1, hardnessCar1,
                                              (scenarioType.equals("Va chạm xe - xe") ? car2Mass : 0),
                                              (scenarioType.equals("Va chạm xe - xe") ? car2Speed / 3.6 : 0),
                                              (scenarioType.equals("Va chạm xe - xe") ? safetyFactorCar2 : 0),
                                              (scenarioType.equals("Va chạm xe - xe") ? hardnessCar2 : 0));

        StringBuilder result = new StringBuilder();
        result.append("Kết quả mô phỏng:\n");
        result.append("Xe 1 - Động năng ban đầu: ").append(df.format(ke1Initial)).append(" J, sau va chạm: ")
                .append(df.format(ke1Final)).append(" J, % an toàn: ").append(df.format(safetyPercentageCar1)).append("%\n");
        if (scenarioType.equals("Va chạm xe - xe")) {
            result.append("Xe 2 - Động năng ban đầu: ").append(df.format(ke2Initial)).append(" J, sau va chạm: ")
                    .append(df.format(ke2Final)).append(" J, % an toàn: ").append(df.format(safetyPercentageCar2)).append("%\n");
        }
        result.append("Loại tai nạn: ").append(scenarioType).append("\n\n");
        result.append("Giải thích từ ML:\n").append(mlAdvice);
        resultArea.setText(result.toString());
    }

    public void chooseCarColor(int carNumber) {
        Color initialColor = (carNumber == 1) ? car1Color : car2Color;
        Color chosenColor = JColorChooser.showDialog(this, "Chọn màu xe " + carNumber, initialColor);
        if (chosenColor != null) {
            if (carNumber == 1) {
                car1Color = chosenColor;
            } else {
                car2Color = chosenColor;
            }
            animationPanel.repaint();
        }
    }

    // Getters cho các màu và thông số giao diện
    public Color getBackgroundColor() { return backgroundColor; }
    public Color getPanelBackgroundColor() { return panelBackgroundColor; }
    public Color getTextColor() { return textColor; }
    public Color getInputFieldBackgroundColor() { return inputFieldBackgroundColor; }
    public Color getButtonColor() { return buttonColor; }
    public Color getButtonHoverColor() { return buttonHoverColor; }
    public Color getRoadColor() { return roadColor; }
    public Color getCar1Color() { return car1Color; }
    public Color getCar2Color() { return car2Color; }
    public Color getCarWindowColor() { return carWindowColor; }
    public String getScenarioType() { return scenarioType; }
    public void setScenarioType(String scenarioType) { this.scenarioType = scenarioType; }
    public String getObstacleType() { return obstacleType; }
    public void setObstacleType(String obstacleType) { this.obstacleType = obstacleType; }
    public int getDeformationIntensity() { return deformationIntensity; }
    public double getCar1X() { return car1X; }
    public double getCar1Y() { return car1Y; }
    public double getCar2X() { return car2X; }
    public double getCar2Y() { return car2Y; }
    public List<Spark> getSparks() { return sparks; }
    public List<Debris> getDebrisList() { return debrisList; }
    public Rectangle2D getObstacleRect() { return obstacleRect; }
    public boolean isSimulationStarted() { return simulationStarted; }
    public boolean isCollisionOccurred() { return collisionOccurred; }
    public boolean isDeformedCar1() { return isDeformedCar1; }
    public boolean isDeformedCar2() { return isDeformedCar2; }
    public double getCar1Speed() { return car1Speed; }
    public double getCar2Speed() { return car2Speed; }
}

