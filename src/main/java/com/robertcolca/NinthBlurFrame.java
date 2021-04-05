package com.robertcolca;

import javax.swing.*;

public class NinthBlurFrame extends JFrame {
    public NinthBlurFrame() {
        setTitle("Ninth Blur");
        float ninth = 1.0f / 9.0f;
        double[] kernel = {
                ninth, ninth, ninth,
                ninth, ninth, ninth,
                ninth, ninth, ninth
        };

        add(new BaseKernelOperation(kernel));
        pack();

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
