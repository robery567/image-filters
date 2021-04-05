package com.robertcolca;

import javax.swing.*;

public class AntialiasingFrame extends JFrame {
    public AntialiasingFrame() {
        setTitle("Antialiasing Blur");
        double[] kernel = {0.60 / 9, 1.5 / 9, 0.60 / 9, 1.5 / 9, 0.60 / 9, 1.5 / 9, 0.60 / 9, 1.5 / 9, 0.60 / 9};
        add(new BaseKernelOperation(kernel));
        pack();

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
