package com.robertcolca;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    public Main() {
        setTitle("Floyd-Steinberg Dithering");
        add(new Image());
        pack();

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Main floydSteinberg = new Main();
            floydSteinberg.setVisible(true);

            ImageBlendingFrame blendingFrame = new ImageBlendingFrame();
            blendingFrame.setVisible(true);

            AntialiasingFrame antialiasingFrame = new AntialiasingFrame();
            antialiasingFrame.setVisible(true);

            SharpenImageFrame sharpenImageFrame = new SharpenImageFrame();
            sharpenImageFrame.setVisible(true);

            EdgeDetectionFrame edgeDetectionFrame = new EdgeDetectionFrame();
            edgeDetectionFrame.setVisible(true);

            NinthBlurFrame ninthBlurFrame = new NinthBlurFrame();
            ninthBlurFrame.setVisible(true);
        });
    }
}