/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.carcrashsimulator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;

public class InputPanel extends JPanel {

    private MainFrame mainFrame;
    private JTextField massCar1Field, speedCar1Field, massCar2Field, speedCar2Field,
            numPassengersCar1Field, numPassengersCar2Field,
            safetyFactorCar1Field, safetyFactorCar2Field,
            hardnessCar1Field, hardnessCar2Field, hardnessObjectField,
            coefficientOfRestitutionField;
    private JComboBox<String> scenarioComboBox;
    private JComboBox<String> obstacleTypeComboBox;

    public InputPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setBackground(mainFrame.getPanelBackgroundColor());
        setBorder(BorderFactory.createTitledBorder("Thông số mô phỏng"));
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        add(createInputField("Khối lượng xe 1 (kg):", massCar1Field = createStyledTextField("1500")), gbc);
        add(createInputField("Vận tốc xe 1 (km/h):", speedCar1Field = createStyledTextField("60")), gbc);
        add(createInputField("Số người xe 1:", numPassengersCar1Field = createStyledTextField("2")), gbc);
        add(createInputField("Hệ số an toàn xe 1:", safetyFactorCar1Field = createStyledTextField("0.8")), gbc);
        add(createInputField("Độ cứng xe 1:", hardnessCar1Field = createStyledTextField("1.2")), gbc);

        add(Box.createVerticalStrut(10), gbc);

        add(createInputField("Khối lượng xe 2 (kg):", massCar2Field = createStyledTextField("1200")), gbc);
        add(createInputField("Vận tốc xe 2 (km/h):", speedCar2Field = createStyledTextField("-40")), gbc);
        add(createInputField("Số người xe 2:", numPassengersCar2Field = createStyledTextField("1")), gbc);
        add(createInputField("Hệ số an toàn xe 2:", safetyFactorCar2Field = createStyledTextField("0.7")), gbc);
        add(createInputField("Độ cứng xe 2:", hardnessCar2Field = createStyledTextField("1.0")), gbc);

        add(Box.createVerticalStrut(10), gbc);

        add(createInputField("Độ cứng vật thể va chạm:", hardnessObjectField = createStyledTextField("1.5")), gbc);

        add(Box.createVerticalStrut(10), gbc);

        add(createInputField("Hệ số phục hồi:", coefficientOfRestitutionField = createStyledTextField("0.5")), gbc);

        add(Box.createVerticalStrut(10), gbc);

        String[] scenarios = {"Va chạm xe - xe", "Va chạm xe - chướng ngại vật"};
        scenarioComboBox = new JComboBox<>(scenarios);
        scenarioComboBox.setMaximumSize(new Dimension(200, 30));
        scenarioComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectedScenario = (String) scenarioComboBox.getSelectedItem();
                mainFrame.setScenarioType(selectedScenario);
                boolean enableCar2 = selectedScenario.equals("Va chạm xe - xe");
                massCar2Field.setEnabled(enableCar2);
                speedCar2Field.setEnabled(enableCar2);
                numPassengersCar2Field.setEnabled(enableCar2);
                safetyFactorCar2Field.setEnabled(enableCar2);
                hardnessCar2Field.setEnabled(enableCar2);
                obstacleTypeComboBox.setEnabled(!enableCar2);
            }
        });
        JPanel scenarioPanel = new JPanel();
        scenarioPanel.setBackground(mainFrame.getPanelBackgroundColor());
        JLabel scenarioLabel = new JLabel("Loại tai nạn:");
        scenarioLabel.setForeground(mainFrame.getTextColor());
        scenarioLabel.setPreferredSize(new Dimension(150, 25));
        scenarioLabel.setFont(scenarioLabel.getFont().deriveFont(12f));
        scenarioPanel.add(scenarioLabel);
        scenarioPanel.add(scenarioComboBox);
        add(scenarioPanel, gbc);

        add(Box.createVerticalStrut(10), gbc);

        String[] obstacles = {"Cây cối", "Rào chắn"};
        obstacleTypeComboBox = new JComboBox<>(obstacles);
        obstacleTypeComboBox.setMaximumSize(new Dimension(200, 30));
        obstacleTypeComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectedObstacle = (String) obstacleTypeComboBox.getSelectedItem();
                mainFrame.setObstacleType(selectedObstacle);
            }
        });
        JPanel obstaclePanel = new JPanel();
        obstaclePanel.setBackground(mainFrame.getPanelBackgroundColor());
        JLabel obstacleLabel = new JLabel("Loại chướng ngại vật:");
        obstacleLabel.setForeground(mainFrame.getTextColor());
        obstacleLabel.setPreferredSize(new Dimension(150, 25));
        obstacleLabel.setFont(obstacleLabel.getFont().deriveFont(12f));
        obstaclePanel.add(obstacleLabel);
        obstaclePanel.add(obstacleTypeComboBox);
        obstacleTypeComboBox.setEnabled(mainFrame.getScenarioType().equals("Va chạm xe - chướng ngại vật"));
        add(obstaclePanel, gbc);

        add(Box.createVerticalStrut(10), gbc);
        setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    private JPanel createInputField(String labelText, JTextField textField) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(mainFrame.getPanelBackgroundColor());
        JLabel label = new JLabel(labelText);
        label.setForeground(mainFrame.getTextColor());
        label.setPreferredSize(new Dimension(150, 25));
        label.setFont(label.getFont().deriveFont(12f));
        panel.add(label);
        textField.setPreferredSize(new Dimension(100, 25));
        panel.add(textField);
        return panel;
    }

    private JTextField createStyledTextField(String defaultValue) {
        JTextField textField = new JTextField(defaultValue);
        textField.setPreferredSize(new Dimension(100, 25));
        textField.setBackground(mainFrame.getInputFieldBackgroundColor());
        textField.setForeground(mainFrame.getTextColor());
        textField.setCaretColor(Color.WHITE);
        textField.setFont(textField.getFont().deriveFont(12f));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80)),
                new EmptyBorder(5, 5, 5, 5)
        ));
        return textField;
    }

    public double getMassCar1() {
        return Double.parseDouble(massCar1Field.getText());
    }
    public double getSpeedCar1() {
        return Double.parseDouble(speedCar1Field.getText());
    }
    public double getMassCar2() {
        return Double.parseDouble(massCar2Field.getText());
    }
    public double getSpeedCar2() {
        return Double.parseDouble(speedCar2Field.getText());
    }
    public int getNumPassengersCar1() {
        return Integer.parseInt(numPassengersCar1Field.getText());
    }
    public int getNumPassengersCar2() {
        return Integer.parseInt(numPassengersCar2Field.getText());
    }
    public double getSafetyFactorCar1() {
        return Double.parseDouble(safetyFactorCar1Field.getText());
    }
    public double getSafetyFactorCar2() {
        return Double.parseDouble(safetyFactorCar2Field.getText());
    }
    public double getHardnessCar1() {
        return Double.parseDouble(hardnessCar1Field.getText());
    }
    public double getHardnessCar2() {
        return Double.parseDouble(hardnessCar2Field.getText());
    }
    public double getHardnessObject() {
        return Double.parseDouble(hardnessObjectField.getText());
    }
    public double getCoefficientOfRestitution() {
        return Double.parseDouble(coefficientOfRestitutionField.getText());
    }

    // Các phương thức setter để cập nhật trường khi chọn mẫu xe
    public void setMassCar1Field(String value) {
        massCar1Field.setText(value);
    }
    public void setSafetyFactorCar1Field(String value) {
        safetyFactorCar1Field.setText(value);
    }
    public void setHardnessCar1Field(String value) {
        hardnessCar1Field.setText(value);
    }
    public void setMassCar2Field(String value) {
        massCar2Field.setText(value);
    }
    public void setSafetyFactorCar2Field(String value) {
        safetyFactorCar2Field.setText(value);
    }
    public void setHardnessCar2Field(String value) {
        hardnessCar2Field.setText(value);
    }
}

