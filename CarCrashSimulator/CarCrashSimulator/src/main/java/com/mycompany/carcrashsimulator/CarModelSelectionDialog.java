package com.mycompany.carcrashsimulator;

import DAO.DatabaseConnection;
import Models.Vehicle;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CarModelSelectionDialog extends JDialog {
    private JComboBox<String> vehicleDropdown;
    private JTextField massField, speedField, passengersField, safetyFactorField, hardnessField;
    private List<Vehicle> vehicles;

    public CarModelSelectionDialog(MainFrame mainFrame) {
        super(mainFrame, "Chọn mẫu xe", true);
        initComponents();
        loadVehicleData();
    }

    private void initComponents() {
        vehicleDropdown = new JComboBox<>();
        massField = new JTextField(10);
        speedField = new JTextField(10);
        passengersField = new JTextField(10);
        safetyFactorField = new JTextField(10);
        hardnessField = new JTextField(10);

        // Giao diện dùng GroupLayout để dễ dàng căn chỉnh
        setLayout(new GroupLayout(getContentPane()));

        GroupLayout layout = (GroupLayout) getContentPane().getLayout();
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(new JLabel("Chọn Xe"))
                .addComponent(vehicleDropdown)
                .addComponent(new JLabel("Khối Lượng"))
                .addComponent(massField)
                .addComponent(new JLabel("Tốc Độ"))
                .addComponent(speedField)
                .addComponent(new JLabel("Số Người"))
                .addComponent(passengersField)
                .addComponent(new JLabel("Hệ Số An Toàn"))
                .addComponent(safetyFactorField)
                .addComponent(new JLabel("Độ Cứng"))
                .addComponent(hardnessField)
            )
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
            .addComponent(new JLabel("Chọn Xe"))
            .addComponent(vehicleDropdown)
            .addComponent(new JLabel("Khối Lượng"))
            .addComponent(massField)
            .addComponent(new JLabel("Tốc Độ"))
            .addComponent(speedField)
            .addComponent(new JLabel("Số Người"))
            .addComponent(passengersField)
            .addComponent(new JLabel("Hệ Số An Toàn"))
            .addComponent(safetyFactorField)
            .addComponent(new JLabel("Độ Cứng"))
            .addComponent(hardnessField)
        );

        pack();
        setLocationRelativeTo(null);  // Đảm bảo Dialog xuất hiện ở giữa màn hình

        vehicleDropdown.addActionListener(e -> fillVehicleData());
    }

    private void loadVehicleData() {
        DatabaseConnection db = new DatabaseConnection(); // Tạo instance của DatabaseConnection
        vehicles = db.getAllVehicles(); // Lấy danh sách xe
        for (Vehicle vehicle : vehicles) {
            vehicleDropdown.addItem(vehicle.getName());
        }

        // Kiểm tra nếu không có xe nào được tải
        if (vehicleDropdown.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "Không có mẫu xe nào để chọn!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            this.dispose(); // Đóng dialog nếu không có xe
        }
    }

    private void fillVehicleData() {
        String selectedName = (String) vehicleDropdown.getSelectedItem();
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getName().equals(selectedName)) {
                massField.setText(String.valueOf(vehicle.getWeight()));
                speedField.setText(String.valueOf(vehicle.getSpeed()));
                passengersField.setText(String.valueOf(vehicle.getPassengers()));
                safetyFactorField.setText(String.valueOf(vehicle.getSafetyFactor()));
                hardnessField.setText(String.valueOf(vehicle.getHardness()));
                break;
            }
        }
    }

    public String getSelectedModel() {
        return (String) vehicleDropdown.getSelectedItem();
    }

    public int getSelectedCarNumber() {
        return vehicleDropdown.getSelectedIndex() + 1;
    }
}