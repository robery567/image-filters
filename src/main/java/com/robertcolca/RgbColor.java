package com.robertcolca;

import java.awt.*;

public class RgbColor {
    private int red;
    private int green;
    private int blue;

    public RgbColor(int c) {
        Color color = new Color(c);
        this.red = color.getRed();
        this.green = color.getGreen();
        this.blue = color.getBlue();
    }

    public RgbColor(int r, int g, int b) {
        this.red = r;
        this.green = g;
        this.blue = b;
    }

    public int getRed() {
        return this.red;
    }

    public int getBlue() {
        return this.blue;
    }

    public int getGreen() {
        return this.green;
    }

    public static int colorCorrection(int colorValue) {
        return Math.max(0, Math.min(255, colorValue));
    }

    public Color toColor() {
        return new Color(
                colorCorrection(this.red),
                colorCorrection(this.green),
                colorCorrection(this.blue)
        );
    }

    public int toRGB() {
        return this.toColor().getRGB();
    }
}
