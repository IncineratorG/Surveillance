package com.company.surveillance.data;

import javafx.scene.image.Image;

/**
 * Created by Alexander on 05.07.2018.
 */
public class ProcessedData {
    private double val;
    private Image processedImage;


    public ProcessedData() {
        val = 0;
    }


    public Image getProcessedImage() {
        return processedImage;
    }

    public void setProcessedImage(Image processedImage) {
        this.processedImage = processedImage;
    }


    public double getVal() {
        return val;
    }

    public void setVal(double val) {
        this.val = val;
    }
}
