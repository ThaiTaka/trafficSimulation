package com.mycompany.carcrashsimulator;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class CarAnimationPanel extends JPanel {
    private MainFrame mainFrame;

    public CarAnimationPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setDoubleBuffered(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ nền và đường
        g2d.setColor(mainFrame.getBackgroundColor());
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setColor(mainFrame.getRoadColor());
        g2d.fillRect(0, 400, getWidth(), 100);
        g2d.setColor(Color.WHITE);
        for (int x = 0; x < getWidth(); x += 50) {
            g2d.fillRect(x, 445, 30, 5);
        }

        // Vẽ xe hoặc xe & chướng ngại vật theo tình huống
        if (mainFrame.isSimulationStarted()) {
            if (mainFrame.getScenarioType().equals("Va chạm xe - chướng ngại vật")) {
                drawObstacle(g2d);
                drawCar(g2d, (int) mainFrame.getCar1X(), (int) mainFrame.getCar1Y(), mainFrame.getCar1Color(), false, mainFrame.isDeformedCar1());
            } else {
                drawCar(g2d, (int) mainFrame.getCar1X(), (int) mainFrame.getCar1Y(), mainFrame.getCar1Color(), false, mainFrame.isDeformedCar1());
                drawCar(g2d, (int) mainFrame.getCar2X(), (int) mainFrame.getCar2Y(), mainFrame.getCar2Color(), true, mainFrame.isDeformedCar2());
            }

            if (mainFrame.isCollisionOccurred()) {
                Composite originalComposite = g2d.getComposite();
                for (Spark spark : mainFrame.getSparks()) {
                    float glowAlpha = spark.getAlpha() / 255f * 0.3f;
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, glowAlpha));
                    g2d.setColor(new Color(255, 200, 0, spark.getAlpha()));
                    double glowSize = spark.getSize() * 3;
                    g2d.fill(new Ellipse2D.Double(spark.getX() - glowSize / 2, spark.getY() - glowSize / 2, glowSize, glowSize));
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, spark.getAlpha() / 255f));
                    g2d.setColor(spark.getColor());
                    g2d.fill(new Ellipse2D.Double(spark.getX() - spark.getSize() / 2, spark.getY() - spark.getSize() / 2, spark.getSize(), spark.getSize()));
                }
                g2d.setComposite(originalComposite);
                for (Debris d : mainFrame.getDebrisList()) {
                    d.draw(g2d);
                }
            }
        }
    }

    void createSparks() {
        double collisionSpeed = Math.abs(mainFrame.getCar1Speed() - mainFrame.getCar2Speed());
        int numSparks = (int) (collisionSpeed * 2);
        double collisionX, collisionY;
        if (mainFrame.getScenarioType().equals("Va chạm xe - chướng ngại vật")) {
            collisionX = mainFrame.getCar1X() + 120;
            collisionY = mainFrame.getCar1Y() + 20;
        } else {
            collisionX = (mainFrame.getCar1X() + mainFrame.getCar2X()) / 2;
            collisionY = (mainFrame.getCar1Y() + mainFrame.getCar2Y()) / 2;
        }
        for (int i = 0; i < numSparks; i++) {
            double angle = Math.random() * Math.PI * 2;
            double speed = Math.random() * 10 + (collisionSpeed / 20);
            mainFrame.getSparks().add(new Spark(collisionX, collisionY, speed, angle));
        }
    }

    void createDebris() {
        double collisionSpeed = Math.abs(mainFrame.getCar1Speed() - mainFrame.getCar2Speed());
        int numDebris = (int) (collisionSpeed) + 5;
        double collisionX, collisionY;
        if (mainFrame.getScenarioType().equals("Va chạm xe - chướng ngại vật")) {
            collisionX = mainFrame.getCar1X() + 120;
            collisionY = mainFrame.getCar1Y();
        } else {
            collisionX = (mainFrame.getCar1X() + mainFrame.getCar2X()) / 2;
            collisionY = (mainFrame.getCar1Y() + mainFrame.getCar2Y()) / 2;
        }
        for (int i = 0; i < numDebris; i++) {
            double angle = Math.random() * Math.PI * 2;
            double speed = Math.random() * 8 + (collisionSpeed / 30);
            mainFrame.getDebrisList().add(new Debris(collisionX, collisionY, speed, angle));
        }
    }

    private void drawObstacle(Graphics2D g2d) {
        Rectangle2D obstacleRect = mainFrame.getObstacleRect();
        if (mainFrame.getObstacleType().equals("Cây cối")) {
            g2d.setColor(new Color(101, 67, 33));
            g2d.fillRect((int) (obstacleRect.getX() + obstacleRect.getWidth() / 3),
                         (int) (obstacleRect.getY() + obstacleRect.getHeight() - 40),
                         (int) (obstacleRect.getWidth() / 3), 40);
            g2d.setColor(new Color(34, 139, 34));
            g2d.fillOval((int) obstacleRect.getX(), (int) (obstacleRect.getY() - 30),
                         (int) (obstacleRect.getWidth() + 20), (int) obstacleRect.getHeight());
        } else {
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRect((int) obstacleRect.getX(), (int) obstacleRect.getY(),
                         (int) obstacleRect.getWidth(), (int) obstacleRect.getHeight());
            g2d.setColor(Color.LIGHT_GRAY);
            for (int i = (int) obstacleRect.getY(); i < obstacleRect.getY() + obstacleRect.getHeight(); i += 10) {
                g2d.drawLine((int) obstacleRect.getX(), i, (int) (obstacleRect.getX() + obstacleRect.getWidth()), i);
            }
        }
    }

    private void drawCar(Graphics2D g2d, int x, int y, Color color, boolean facingRight, boolean isDeformed) {
        int carWidth = 140;
        int carHeight = 60;
        int wheelRadius = 20;
        int deformationFactor = isDeformed ? mainFrame.getDeformationIntensity() : 0;
        AffineTransform oldTransform = g2d.getTransform();
        if (!facingRight) {
            g2d.translate(x + carWidth, 0);
            g2d.scale(-1, 1);
            x = 0;
        }
        g2d.setColor(color);
        Polygon carBody = new Polygon();
        carBody.addPoint(x, y + carHeight - deformationFactor);
        carBody.addPoint(x + (int)(carWidth * 0.2), y - deformationFactor);
        carBody.addPoint(x + (int)(carWidth * 0.8), y - deformationFactor);
        carBody.addPoint(x + carWidth, y + carHeight - deformationFactor);
        g2d.fillPolygon(carBody);
        g2d.setColor(Color.DARK_GRAY);
        Ellipse2D wheel1 = new Ellipse2D.Double(x + carWidth * 0.15 - wheelRadius,
                                                  y + carHeight * 0.85 - wheelRadius,
                                                  2 * wheelRadius, 2 * wheelRadius);
        Ellipse2D wheel2 = new Ellipse2D.Double(x + carWidth * 0.75 - wheelRadius,
                                                  y + carHeight * 0.85 - wheelRadius,
                                                  2 * wheelRadius, 2 * wheelRadius);
        g2d.fill(wheel1);
        g2d.fill(wheel2);
        g2d.setColor(mainFrame.getCarWindowColor());
        RoundRectangle2D window = new RoundRectangle2D.Double(x + carWidth * 0.35,
                                                               y - 15 - deformationFactor,
                                                               carWidth * 0.3, 20, 5, 5);
        g2d.fill(window);
        if (facingRight) {
            g2d.setColor(Color.YELLOW);
            RoundRectangle2D headlight = new RoundRectangle2D.Double(x + carWidth - 25,
                                                                    y + carHeight * 0.4, 20, 8, 5, 5);
            g2d.fill(headlight);
            g2d.setColor(new Color(255, 50, 50));
            RoundRectangle2D taillight = new RoundRectangle2D.Double(x + 5,
                                                                      y + carHeight * 0.5, 8, 6, 3, 3);
            g2d.fill(taillight);
        } else {
            g2d.setColor(Color.YELLOW);
            RoundRectangle2D headlight = new RoundRectangle2D.Double(x + 5,
                                                                    y + carHeight * 0.4, 20, 8, 5, 5);
            g2d.fill(headlight);
            g2d.setColor(new Color(255, 50, 50));
            RoundRectangle2D taillight = new RoundRectangle2D.Double(x + carWidth - 25,
                                                                      y + carHeight * 0.5, 8, 6, 3, 3);
            g2d.fill(taillight);
        }
        g2d.setTransform(oldTransform);
    }
}
