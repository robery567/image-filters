package com.robertcolca;

import javax.swing.*;

public class EdgeDetectionFrame extends JFrame {
    public EdgeDetectionFrame() {
        setTitle("Edge Detection Frame");
        double[] kernel = {
                0.0f, -1.0f, 0.0f,
                -1.0f, 4.0f, -1.0f,
                0.0f, -1.0f, 0.0f
        };

        add(new BaseKernelOperation(kernel));
        pack();

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
