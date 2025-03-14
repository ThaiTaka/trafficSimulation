package com.mycompany.carcrashsimulator;

import javax.swing.*;
import java.awt.*;
import java.awt.Cursor;

public class ButtonPanel extends JPanel {

    private final MainFrame mainFrame;

    public ButtonPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setBackground(mainFrame.getPanelBackgroundColor());
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton chooseCarModelButton = createStyledButton("Chọn mẫu xe", mainFrame.getButtonColor());
        chooseCarModelButton.addActionListener(e -> {
            // Mở dialog chọn mẫu xe trong EDT
            SwingUtilities.invokeLater(() -> {
                CarModelSelectionDialog dialog = new CarModelSelectionDialog(mainFrame);
                dialog.setModal(true);  // Đảm bảo dialog là modal
                dialog.pack();  // Tự động điều chỉnh kích thước cho dialog
                dialog.setLocationRelativeTo(mainFrame);  // Đảm bảo dialog mở ở vị trí chính giữa
                dialog.setVisible(true);  // Hiển thị dialog
            });
        });
        add(chooseCarModelButton);

        // Các nút khác...
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setMaximumSize(new Dimension(200, 35));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Tạo gradient cho nút
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, color.brighter(), 0, c.getHeight(), color.darker());
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 8, 8);
                g2.setColor(new Color(0, 0, 0, 50));
                g2.drawRoundRect(1, 1, c.getWidth() - 3, c.getHeight() - 3, 8, 8);
                super.paint(g2, c);
                g2.dispose();
            }
        });
        button.setContentAreaFilled(false);

        // Thêm hiệu ứng hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalColor = color;
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                originalColor = button.getBackground();
                button.setBackground(mainFrame.getButtonHoverColor());
                button.setLocation(button.getX(), button.getY() - 2);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor);
                button.setLocation(button.getX(), button.getY() + 2);
            }
        });

        return button;
    }
}