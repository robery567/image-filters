package com.robertcolca;

import java.awt.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

class Image extends JPanel {
    private BufferedImage inputImage;
    private BufferedImage floydSteinbergImage;
    private RgbColor[][] floydSteinbergRgbPixelMatrix;
    private int inputImageWidth = 0;
    private int inputImageHeight = 0;

    public Image() {
        loadImage();
        createImageUsingFloydSteinberg();
        setSurfaceSize();
    }

    private void loadImage() {
        try {
            File file = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("cat.jpeg")).getFile());
            this.inputImage = ImageIO.read(file);
            this.floydSteinbergImage = this.inputImage;

            // We keep the image width and height for further usage
            this.inputImageWidth = this.inputImage.getWidth();
            this.inputImageHeight = this.inputImage.getHeight();

            this.generateFloydSteinbergRgb2dMatrix();
        } catch (IOException ex) {
            Logger.getLogger(Image.class.getName()).log(
                    Level.WARNING, null, ex);
        }
    }

    private void generateFloydSteinbergRgb2dMatrix() {
        this.floydSteinbergRgbPixelMatrix = new RgbColor[this.inputImageHeight][this.inputImageWidth];

        for (int y = 0; y < this.inputImageHeight; y++) {
            for (int x = 0; x < this.inputImageWidth; x++) {
                this.floydSteinbergRgbPixelMatrix[y][x] = new RgbColor(this.inputImage.getRGB(x, y));
            }
        }
    }

    /**
     * As mentioned in the wiki: https://en.wikipedia.org/wiki/Floyd%E2%80%93Steinberg_dithering
     */
    private void createImageUsingFloydSteinberg() {
        for (int y = 0; y < this.inputImageHeight; ++y) {
            for (int x = 0; x < this.inputImageWidth; ++x) {
                RgbColor oldPixel = this.floydSteinbergRgbPixelMatrix[y][x];
                RgbColor newPixel = this.findClosestColor(oldPixel);
                System.out.println(
                        "R: " + (oldPixel.getRed() - newPixel.getRed())
                                + " ,G: " + (oldPixel.getGreen() - newPixel.getGreen())
                                + " ,B: " + (oldPixel.getBlue() - newPixel.getBlue())
                );

                RgbColor quantError = new RgbColor(
                        oldPixel.getRed() - newPixel.getRed(),
                        oldPixel.getGreen() - newPixel.getGreen(),
                        oldPixel.getBlue() - newPixel.getBlue()
                );

                RgbColor newPixelColors = new RgbColor(newPixel.toRGB());
                this.floydSteinbergImage.setRGB(x, y, new Color(
                        RgbColor.colorCorrection(newPixelColors.getRed()),
                        RgbColor.colorCorrection(newPixelColors.getGreen()),
                        RgbColor.colorCorrection(newPixelColors.getBlue())
                ).getRGB());

                // Floyd Steinberg
                if (x + 1 < this.inputImageWidth) {
                    RgbColor color = this.floydSteinbergRgbPixelMatrix[y][x + 1];

                    this.floydSteinbergRgbPixelMatrix[y][x + 1] = new RgbColor(
                            this.correctOverflowColor((int) (color.getRed() + 7.0 / 16 * quantError.getRed())),
                            this.correctOverflowColor((int) (color.getGreen() + 7.0 / 16 * quantError.getGreen())),
                            this.correctOverflowColor((int) (color.getBlue() + 7.0 / 16 * quantError.getBlue()))
                    );
                }

                if (((x - 1) >= 0) && ((y + 1) < this.inputImageHeight)) {
                    RgbColor color = this.floydSteinbergRgbPixelMatrix[y + 1][x - 1];

                    this.floydSteinbergRgbPixelMatrix[y + 1][x - 1] = new RgbColor(
                            this.correctOverflowColor((int) (color.getRed() + 3.0 / 16 * quantError.getRed())),
                            this.correctOverflowColor((int) (color.getGreen() + 3.0 / 16 * quantError.getGreen())),
                            this.correctOverflowColor((int) (color.getBlue() + 3.0 / 16 * quantError.getBlue()))
                    );
                }

                if (y + 1 < this.inputImageHeight) {
                    RgbColor color = this.floydSteinbergRgbPixelMatrix[y + 1][x];

                    this.floydSteinbergRgbPixelMatrix[y + 1][x] = new RgbColor(
                            this.correctOverflowColor((int) (color.getRed() + 5.0 / 16 * quantError.getRed())),
                            this.correctOverflowColor((int) (color.getGreen() + 5.0 / 16 * quantError.getGreen())),
                            this.correctOverflowColor((int) (color.getBlue() + 5.0 / 16 * quantError.getBlue()))
                    );
                }

                if (((x + 1) < this.inputImageWidth) && ((y + 1) < inputImageHeight)) {
                    RgbColor color = this.floydSteinbergRgbPixelMatrix[y + 1][x + 1];

                    this.floydSteinbergRgbPixelMatrix[y + 1][x + 1] = new RgbColor(
                            this.correctOverflowColor((int) (color.getRed() + 1.0 / 16 * quantError.getRed())),
                            this.correctOverflowColor((int) (color.getGreen() + 1.0 / 16 * quantError.getGreen())),
                            this.correctOverflowColor((int) (color.getBlue() + 1.0 / 16 * quantError.getBlue()))
                    );
                }
            }
        }
    }

    private int correctOverflowColor(int colorValue) {
        if (colorValue > 255) {
            System.out.println("Overflow: " + colorValue);
            return 255;
        }

        return colorValue;
    }

    private RgbColor findClosestColor(RgbColor oldPixel) {
        // Palette colors
        RgbColor[] palette = {
                new RgbColor(0, 0, 0), // black
                new RgbColor(255, 0, 0), // red
                new RgbColor(0, 255, 0), // blue
                new RgbColor(0, 0, 255), // green
                new RgbColor(255, 255, 0), // yellow
                new RgbColor(255, 0, 255), // purple
                new RgbColor(0, 255, 255), // cyan
                new RgbColor(255, 255, 255)  // white
        };


        RgbColor closest = palette[0];

        for (RgbColor color : palette) {
            double closestDifference = this.getDistanceBetweenTwoColors(closest, oldPixel);
            double colorDifference = this.getDistanceBetweenTwoColors(color, oldPixel);

            if (colorDifference < closestDifference) {
                closest = color;
            }
        }

        return closest;
    }

    private double getDistanceBetweenTwoColors(RgbColor firstColor, RgbColor secondColor) {
        int redDifference = firstColor.getRed() - secondColor.getRed();
        int greenDifference = firstColor.getGreen() - secondColor.getGreen();
        int blueDifference = firstColor.getBlue() - secondColor.getBlue();

        return redDifference * redDifference + greenDifference * greenDifference + blueDifference * blueDifference;
    }

    private void setSurfaceSize() {
        Dimension d = new Dimension();
        d.width = inputImage.getWidth(null);
        d.height = inputImage.getHeight(null);
        setPreferredSize(d);
    }

    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(floydSteinbergImage, null, 0, 0);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }
}