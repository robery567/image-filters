package com.robertcolca;

import javax.swing.*;

public class SharpenImageFrame extends JFrame {
    public SharpenImageFrame() {
        setTitle("Sharpen Filter");
        /*
         * @Description: As mentioned in this article, we use this kernel:
         * https://www.javaworld.com/article/2076764/image-processing-with-java-2d.html
         */
        double[] sharpKernel = {
                0.0f, -1.0f, 0.0f,
                -1.0f, 5.0f, -1.0f,
                0.0f, -1.0f, 0.0f
        };
        add(new BaseKernelOperation(sharpKernel));
        pack();

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
