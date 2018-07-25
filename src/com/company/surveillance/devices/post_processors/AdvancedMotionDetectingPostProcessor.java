package com.company.surveillance.devices.post_processors;

import com.company.surveillance.data.ProcessedData;
import com.company.surveillance.helpers.OpenCVHelper;
import com.company.surveillance.interfaces.data.Frame;
import com.company.surveillance.interfaces.devices.PostProcessor;
import com.company.surveillance.interfaces.event_listeners.FrameProcessedEventListener;
import org.opencv.core.*;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 24.07.2018.
 */
public class AdvancedMotionDetectingPostProcessor implements PostProcessor {
    private String CLASS_NAME = "AdvancedMotionDetectingPostProcessor";
    private ProcessedData processedData;
    private List<FrameProcessedEventListener> listeners;
    private Frame originalFrame;
    private String[] classNames;
    private final int IN_WIDTH = 300;
    private final int IN_HEIGHT = 300;
    private final float WH_RATIO = (float)IN_WIDTH / IN_HEIGHT;
    private final double IN_SCALE_FACTOR = 0.007843;
    private final double MEAN_VAL = 127.5;
    private final double THRESHOLD = 0.5;
    private String proto;
    private String model;
    private Net net;


    public AdvancedMotionDetectingPostProcessor() {
        System.out.println(CLASS_NAME);

        processedData = new ProcessedData();
        listeners = new ArrayList<>();

        classNames = new String[] {"background",
                "aeroplane", "bicycle", "bird", "boat",
                "bottle", "bus", "car", "cat", "chair", "cow", "diningtable",
                "dog", "horse", "motorbike", "person", "pottedplant", "sheep",
                "sofa", "train", "tvmonitor"};

        proto = "C:\\OpenCV_3.3.0\\sources\\samples\\data\\dnn\\internet\\MobileNetSSD_deploy.prototxt";
        model = "C:\\OpenCV_3.3.0\\sources\\samples\\data\\dnn\\internet\\MobileNetSSD_deploy.caffemodel";

        net = Dnn.readNetFromCaffe(proto, model);
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
    public List<FrameProcessedEventListener> getFrameProcessedEventListeners() {
        return listeners;
    }

    @Override
    public void onNewFrame(Frame frame) {
        originalFrame = frame;

        // Get a new frame
        Mat inputFrame = OpenCVHelper.image2Mat(frame.getImage());;
        Imgproc.cvtColor(inputFrame, inputFrame, Imgproc.COLOR_RGBA2RGB);

        // Forward image through network.
        Mat blob = Dnn.blobFromImage(inputFrame, IN_SCALE_FACTOR,
                new Size(IN_WIDTH, IN_HEIGHT),
                new Scalar(MEAN_VAL, MEAN_VAL, MEAN_VAL), false);
        net.setInput(blob);
        Mat detections = net.forward();

        int cols = inputFrame.cols();
        int rows = inputFrame.rows();

        Size cropSize;
        if ((float)cols / rows > WH_RATIO) {
            cropSize = new Size(rows * WH_RATIO, rows);
        } else {
            cropSize = new Size(cols, cols / WH_RATIO);
        }

        int y1 = (int)(rows - cropSize.height) / 2;
        int y2 = (int)(y1 + cropSize.height);
        int x1 = (int)(cols - cropSize.width) / 2;
        int x2 = (int)(x1 + cropSize.width);
        Mat subFrame = inputFrame.submat(y1, y2, x1, x2);

        cols = subFrame.cols();
        rows = subFrame.rows();
        detections = detections.reshape(1, (int)detections.total() / 7);

        for (int i = 0; i < detections.rows(); ++i) {
            double confidence = detections.get(i, 2)[0];
            if (confidence > THRESHOLD) {
                int classId = (int)detections.get(i, 1)[0];
                int xLeftBottom = (int)(detections.get(i, 3)[0] * cols);
                int yLeftBottom = (int)(detections.get(i, 4)[0] * rows);
                int xRightTop   = (int)(detections.get(i, 5)[0] * cols);
                int yRightTop   = (int)(detections.get(i, 6)[0] * rows);
                // Draw rectangle around detected object.
                Imgproc.rectangle(subFrame, new Point(xLeftBottom, yLeftBottom),
                        new Point(xRightTop, yRightTop),
                        new Scalar(0, 255, 0));
                String label = classNames[classId] + ": " + confidence;

                int[] baseLine = new int[1];
                Size labelSize = Imgproc.getTextSize(label, Core.FONT_HERSHEY_SIMPLEX, 0.5, 1, baseLine);
                // Draw background for label.
                Imgproc.rectangle(subFrame, new Point(xLeftBottom, yLeftBottom - labelSize.height),
                        new Point(xLeftBottom + labelSize.width, yLeftBottom + baseLine[0]),
                        new Scalar(255, 255, 255), Core.FILLED);
                // Write class name and confidence.
                Imgproc.putText(subFrame, label, new Point(xLeftBottom, yLeftBottom),
                        Core.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(0, 0, 0));
            }
        }

        processedData.setProcessedImage(OpenCVHelper.mat2Image(inputFrame));

        notifyListeners();
    }
}
