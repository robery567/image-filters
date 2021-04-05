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

public class BaseKernelOperation extends JPanel {
    protected BufferedImage inputImage;
    protected BufferedImage modifiedImage;
    protected RgbColor[][] rgbPixelMatrix;
    protected int inputImageWidth = 0;
    protected int inputImageHeight = 0;
    protected double[] blurKernel;

    public BaseKernelOperation(double[] blurKernel) {
        this.blurKernel = blurKernel;

        loadImage();
        createImageUsingKernel();
        setSurfaceSize();
    }

    protected void loadImage() {
        try {
            File file = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("cat.jpeg")).getFile());
            this.inputImage = ImageIO.read(file);
            this.modifiedImage = this.inputImage;

            // We keep the image width and height for further usage
            this.inputImageWidth = this.inputImage.getWidth();
            this.inputImageHeight = this.inputImage.getHeight();

            this.generateRgb2dMatrix();
        } catch (IOException ex) {
            Logger.getLogger(Image.class.getName()).log(
                    Level.WARNING, null, ex);
        }
    }

    protected void generateRgb2dMatrix() {
        this.rgbPixelMatrix = new RgbColor[this.inputImageHeight][this.inputImageWidth];

        for (int y = 0; y < this.inputImageHeight; y++) {
            for (int x = 0; x < this.inputImageWidth; x++) {
                this.rgbPixelMatrix[y][x] = new RgbColor(this.inputImage.getRGB(x, y));
            }
        }
    }

    protected void createImageUsingKernel() {
        for (int y = 0; y < this.inputImageHeight; ++y) {
            for (int x = 0; x < this.inputImageWidth; ++x) {
                int[] color = new int[3];

                for (int kernelRow = 0; kernelRow < 3; ++kernelRow) {
                    for (int kernelCol = 0; kernelCol < 3; ++kernelCol) {
                        double currentKernelValue = this.blurKernel[3 * kernelRow + kernelCol];
                        int xValue = this.getCoordinateToPick(x, kernelRow, 1, this.inputImageWidth);
                        int yValue = this.getCoordinateToPick(y, kernelCol, 1, this.inputImageHeight);

                        color[0] += (int) (this.rgbPixelMatrix[yValue][xValue].getRed() * currentKernelValue);
                        color[1] += (int) (this.rgbPixelMatrix[yValue][xValue].getGreen() * currentKernelValue);
                        color[2] += (int) (this.rgbPixelMatrix[yValue][xValue].getBlue() * currentKernelValue);
                    }
                }

                this.modifiedImage.setRGB(x, y, new RgbColor(color[0], color[1], color[2]).toRGB());
            }
        }
    }

    protected int getCoordinateToPick(int index, int kernelIndex, int offset, int size) {
        int valueToPick = index + kernelIndex - offset;

        return (valueToPick < 0) ? 0 : ((valueToPick == size) ? (size - 1) : valueToPick);
    }

    protected void setSurfaceSize() {
        Dimension d = new Dimension();
        d.width = inputImage.getWidth(null);
        d.height = inputImage.getHeight(null);
        setPreferredSize(d);
    }

    protected void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(modifiedImage, null, 0, 0);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }
}
