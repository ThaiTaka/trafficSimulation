/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.carcrashsimulator;
import java.awt.*;
import java.awt.Graphics2D;

public class Debris {
    double x, y;
    double vx, vy;
    double size;
    int alpha;
    Color color;
    double gravity = 0.6;
    double decay = 0.97;

    public Debris(double x, double y, double speed, double angle) {
        this.x = x;
        this.y = y;
        this.vx = speed * Math.cos(angle);
        this.vy = speed * Math.sin(angle);
        this.size = Math.random() * 10 + 5;
        this.alpha = 255;
        this.color = new Color((int) (Math.random() * 100 + 50),
                               (int) (Math.random() * 100 + 50),
                               (int) (Math.random() * 100 + 50));
    }

    public void update() {
        x += vx;
        y += vy;
        vy += gravity;
        vx *= decay;
        vy *= decay;
        size *= 0.99;
        alpha = (int) (alpha * 0.96);
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
        g2d.fillRect((int) x, (int) y, (int) size, (int) size);
    }
}
