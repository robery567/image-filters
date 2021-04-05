package com.robertcolca;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageBlend extends JPanel {
    private BufferedImage inputImage1;
    private BufferedImage blendedImage;
    private RgbColor[][] inputImage1RgbPixelMatrix;
    private RgbColor[][] inputImage2RgbPixelMatrix;
    private int imageHeight = 0;
    private int imageWidth = 0;

    public ImageBlend() {
        loadImage();
        runBlendOnTwoImages(100);
        setSurfaceSize();
    }

    private void loadImage() {
        try {
            File file1 = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("disaster.jpg")).getFile());
            File file2 = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("dogo1.jpg")).getFile());

            this.inputImage1 = ImageIO.read(file1);
            BufferedImage inputImage2 = ImageIO.read(file2);

            // We keep the image width and height for further usage
            int inputImage1Width = this.inputImage1.getWidth();
            int inputImage1Height = this.inputImage1.getHeight();
            int inputImage2Width = inputImage2.getWidth();
            int inputImage2Height = inputImage2.getHeight();

            if (inputImage1Width != inputImage2Width || inputImage1Height != inputImage2Height) {
                throw new IOException(
                        "File must have same dimensions, given: "
                                + inputImage1Width + ":" + inputImage1Height
                                + " AND " + inputImage2Width + ":" + inputImage2Height
                );
            }

            this.imageHeight = inputImage1Height;
            this.imageWidth = inputImage1Width;

            this.inputImage1RgbPixelMatrix = this.generateRgb2dMatrix(this.inputImage1);
            this.inputImage2RgbPixelMatrix = this.generateRgb2dMatrix(inputImage2);
        } catch (IOException ex) {
            Logger.getLogger(Image.class.getName()).log(
                    Level.WARNING, null, ex);
        }
    }

    private RgbColor[][] generateRgb2dMatrix(BufferedImage image) {
        int imageHeight = image.getHeight();
        int imageWidth = image.getWidth();

        RgbColor[][] image2dRgbMatrix = new RgbColor[imageHeight][imageWidth];

        for (int x = 0; x < imageWidth; ++x) {
            for (int y = 0; y < imageHeight; ++y) {
                image2dRgbMatrix[y][x] = new RgbColor(image.getRGB(x, y));
            }
        }

        return image2dRgbMatrix;
    }


    private void runBlendOnTwoImages(double weight) {
        this.blendedImage = new BufferedImage(this.imageWidth, this.imageHeight, BufferedImage.TYPE_INT_RGB);

        int[] blendedPixelsRow = new int[this.imageWidth];

        for (int row = 0; row < this.imageHeight; ++row) {
            for (int col = 0; col < this.imageWidth; ++col) {
                RgbColor image1CurrentPixel = this.inputImage1RgbPixelMatrix[row][col];
                RgbColor image2CurrentPixel = this.inputImage2RgbPixelMatrix[row][col];

                int blendedRedPixel = (int) (image1CurrentPixel.getRed() * weight + image2CurrentPixel.getRed() * (1.0 - weight));
                int blendedGreenPixel = (int) (image1CurrentPixel.getGreen() * weight + image2CurrentPixel.getGreen() * (1.0 - weight));
                int blendedBluePixel = (int) (image1CurrentPixel.getBlue() * weight + image2CurrentPixel.getBlue() * (1.0 - weight));

                blendedPixelsRow[col] = (blendedRedPixel << 16) | (blendedGreenPixel << 8) | blendedBluePixel;
            }

            this.blendedImage.setRGB(0, row, this.imageHeight, 1, blendedPixelsRow, 0, this.imageWidth);
        }
    }

    private void setSurfaceSize() {
        Dimension d = new Dimension();
        d.width = inputImage1.getWidth(null);
        d.height = inputImage1.getHeight(null);
        setPreferredSize(d);
    }

    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(blendedImage, null, 0, 0);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }
}
