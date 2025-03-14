package com.mycompany.carcrashsimulator;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Spark {
    private double x, y;
    private double vx, vy;
    private double size;
    private int alpha;
    private Color color;

    public Spark(double x, double y, double speed, double angle) {
        this.x = x;
        this.y = y;
        this.vx = speed * Math.cos(angle);
        this.vy = speed * Math.sin(angle);
        this.size = Math.random() * 5 + 3;
        this.alpha = 255;
        this.color = Color.ORANGE;
    }

    public void update() {
        x += vx;
        y += vy;
        vx *= 0.95;
        vy *= 0.95;
        size *= 0.98;
        alpha = (int) (alpha * 0.95);
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getSize() { return size; }
    public int getAlpha() { return alpha; }
    public Color getColor() { return color; }
}
