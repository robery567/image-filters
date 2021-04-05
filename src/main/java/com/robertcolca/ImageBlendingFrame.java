package com.robertcolca;

import javax.swing.*;

public class ImageBlendingFrame extends JFrame {
    public ImageBlendingFrame() {
        setTitle("Image Blend");
        add(new ImageBlend());
        pack();

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
