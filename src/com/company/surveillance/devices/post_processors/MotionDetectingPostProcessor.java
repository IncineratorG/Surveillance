package com.company.surveillance.devices.post_processors;

import com.company.surveillance.data.ProcessedData;
import com.company.surveillance.helpers.OpenCVHelper;
import com.company.surveillance.interfaces.data.Frame;
import com.company.surveillance.interfaces.devices.PostProcessor;
import com.company.surveillance.interfaces.event_listeners.FrameProcessedEventListener;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractor;
import org.opencv.video.Video;

import java.util.ArrayList;
import java.util.List;



public class MotionDetectingPostProcessor implements PostProcessor {
    private String CLASS_NAME = "MotionDetectingPostProcessor";
    private ProcessedData processedData;
    private List<FrameProcessedEventListener> listeners;
    private Frame originalFrame;


    public MotionDetectingPostProcessor() {
        System.out.println(CLASS_NAME);

        processedData = new ProcessedData();
        listeners = new ArrayList<>();
    }


    @Override
    public void addFrameProcessedEventListener(FrameProcessedEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void deleteFrameProcessedEventListener(FrameProcessedEventListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void notifyListeners() {
        for (FrameProcessedEventListener listener : listeners)
            listener.onFrameProcessed(originalFrame, processedData);
    }

    @Override
    public void onNewFrame(Frame frame) {
        originalFrame = frame;

        Mat src = OpenCVHelper.image2Mat(frame.getImage());

        Imgproc.pyrDown(src, src, new Size(src.cols() / 2, src.rows() / 2));
        Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2GRAY);


//        src = gammaCorrection(src);


        processedData.setProcessedImage(OpenCVHelper.mat2Image(src));

        notifyListeners();
    }

    @Override
    public List<FrameProcessedEventListener> getFrameProcessedEventListeners() {
        return listeners;
    }



    private Mat gammaCorrection(Mat src) {
        Mat lookUpTable = new Mat(1, 256, CvType.CV_8U);
        byte[] lookUpTableData = new byte[(int) (lookUpTable.total()*lookUpTable.channels())];
        for (int i = 0; i < lookUpTable.cols(); i++) {
            lookUpTableData[i] = saturate(Math.pow(i / 255.0, 2) * 255.0);
        }
        lookUpTable.put(0, 0, lookUpTableData);
        Mat img = new Mat();
        Core.LUT(src, lookUpTable, img);

        return img;
    }
    private byte saturate(double val) {
        int iVal = (int) Math.round(val);
        iVal = iVal > 255 ? 255 : (iVal < 0 ? 0 : iVal);
        return (byte) iVal;
    }


    private Mat lightCorrection(Mat src) {
        Mat labImage = new Mat();

        Imgproc.cvtColor(src, labImage, Imgproc.COLOR_BGR2Lab);

        List<Mat> labPlanes = new ArrayList<>(3);
        Core.split(labImage, labPlanes);

        CLAHE clashe =  Imgproc.createCLAHE();
        clashe.setClipLimit(4);
        Mat dst = new Mat();
        clashe.apply(labPlanes.get(0), dst);

        dst.copyTo(labPlanes.get(0));
        Core.merge(labPlanes, labImage);

        Mat imageClash = new Mat();
        Imgproc.cvtColor(labImage, imageClash, Imgproc.COLOR_Lab2BGR);

        return imageClash;
    }
}
