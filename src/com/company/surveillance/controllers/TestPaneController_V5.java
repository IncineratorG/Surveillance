package com.company.surveillance.controllers;

import com.company.surveillance.devices.cameras.Camera;
import com.company.surveillance.devices.video_panels.ProcessedFrameVideoPanel;
import com.company.surveillance.helpers.OpenCVHelper;
import com.company.surveillance.interfaces.devices.FrameProvider;
import com.company.surveillance.interfaces.devices.PostProcessor;
import com.company.surveillance.managers.CameraManager;
import com.company.surveillancedata.data_calsses.ObjectId;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.*;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Alexander on 23.07.2018.
 */
public class TestPaneController_V5 implements Initializable {
    private String CLASS_NAME = "TestPaneController_V5";

    public ScrollPane scrollPane;
    public ImageView imageView;
    private ProcessedFrameVideoPanel videoPanel;
    private Camera camera;
    private CameraManager cameraManager;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String METHOD_NAME = ".initialize()";
        System.out.println(CLASS_NAME + METHOD_NAME);

        imageView.fitWidthProperty().bind(scrollPane.widthProperty());
        imageView.fitHeightProperty().bind(scrollPane.heightProperty());
    }

    public void onButtonOneClicked() {
        String METHOD_NAME = ".onButtonOneClicked()";
        System.out.println(CLASS_NAME + METHOD_NAME);

//        String proto = "C:\\OpenCV_3.3.0\\sources\\samples\\data\\dnn\\MobileNetSSD_300x300.prototxt";
//        String model = "C:\\OpenCV_3.3.0\\sources\\samples\\data\\dnn\\mobilenet_iter_73000.caffemodel";
//        String image = "C:\\OpenCV_3.3.0\\sources\\samples\\data\\dnn\\1.jpg";

        String proto = "C:\\OpenCV_3.3.0\\sources\\samples\\data\\dnn\\internet\\MobileNetSSD_deploy.prototxt";
        String model = "C:\\OpenCV_3.3.0\\sources\\samples\\data\\dnn\\internet\\MobileNetSSD_deploy.caffemodel";
        String image = "C:\\OpenCV_3.3.0\\sources\\samples\\data\\dnn\\internet\\example_02.jpg";

        String[] classes = {"background",
                "aeroplane", "bicycle", "bird", "boat",
                "bottle", "bus", "car", "cat", "chair", "cow", "diningtable",
                "dog", "horse", "motorbike", "person", "pottedplant", "sheep",
                "sofa", "train", "tvmonitor"};

        Net net = Dnn.readNetFromCaffe(proto, model);
//        Net net = Dnn.readNetFromCaffe(proto);

        Mat imageMat = Imgcodecs.imread(image);
        System.out.println("IMAGE_MAT -> " + imageMat);

        Mat resizedImageMat = new Mat();
        Imgproc.resize(imageMat, resizedImageMat, new Size(300, 300));
        System.out.println("RESIZED_IMAGE_MAT -> " + resizedImageMat);

//        Mat blobFromImage = Dnn.blobFromImage(resizedImageMat);
        Mat blobFromImage = Dnn.blobFromImage(imageMat, 0.007843, new Size(300, 300), new Scalar(127.5, 127.5, 127.5), false);
        System.out.println("BLOB -> " + blobFromImage);

        net.setInput(blobFromImage);
        Mat detections = net.forward();
        System.out.println("DETECTIONS -> " + detections);

        Mat reshaped = detections.reshape(1, 1);
        System.out.println("RESHAPED -> " + reshaped);


        Core.MinMaxLocResult result = Core.minMaxLoc(reshaped);
        System.out.println(result.maxLoc.x);

        for (int i = 0; i < reshaped.rows(); ++i) {
            for (int j = 0; j < reshaped.cols(); ++j) {
                double[] d = reshaped.get(i, j);

                for (int k = 0; k < d.length; ++k)
                    System.out.println(i + " - " + j + ": " + d[0]);
//                System.out.println(i + " - " + j + ": " + d[0]);
            }
        }
    }

    public void onButtonTwoClicked() {
        String METHOD_NAME = ".onButtonTwoClicked()";
        System.out.println(CLASS_NAME + METHOD_NAME);


        String[] classNames = {"background",
                "aeroplane", "bicycle", "bird", "boat",
                "bottle", "bus", "car", "cat", "chair", "cow", "diningtable",
                "dog", "horse", "motorbike", "person", "pottedplant", "sheep",
                "sofa", "train", "tvmonitor"};


        final int IN_WIDTH = 300;
        final int IN_HEIGHT = 300;
        final float WH_RATIO = (float)IN_WIDTH / IN_HEIGHT;
        final double IN_SCALE_FACTOR = 0.007843;
        final double MEAN_VAL = 127.5;
        final double THRESHOLD = 0.2;

        String proto = "C:\\OpenCV_3.3.0\\sources\\samples\\data\\dnn\\internet\\MobileNetSSD_deploy.prototxt";
        String model = "C:\\OpenCV_3.3.0\\sources\\samples\\data\\dnn\\internet\\MobileNetSSD_deploy.caffemodel";
        String image = "C:\\OpenCV_3.3.0\\sources\\samples\\data\\dnn\\internet\\example_01.jpg";

        Net net = Dnn.readNetFromCaffe(proto, model);

        Mat inputFrame = Imgcodecs.imread(image);

        // Get a new frame
        Mat frame_1 = inputFrame;
        Imgproc.cvtColor(frame_1, frame_1, Imgproc.COLOR_RGBA2RGB);

        // Forward image through network.
        Mat blob = Dnn.blobFromImage(frame_1, IN_SCALE_FACTOR,
                new Size(IN_WIDTH, IN_HEIGHT),
                new Scalar(MEAN_VAL, MEAN_VAL, MEAN_VAL), false);
        net.setInput(blob);
        Mat detections = net.forward();

        int cols = frame_1.cols();
        int rows = frame_1.rows();

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
        Mat subFrame = frame_1.submat(y1, y2, x1, x2);

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

                // ===
                System.out.println("RESULT: " + label);
                // ===

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


        // ====
        Image image1 = OpenCVHelper.mat2Image(frame_1);
        imageView.setImage(image1);
        // ====
    }

    public void onChooseButtonClicked() {
        String METHOD_NAME = ".onChooseButtonClicked()";
        System.out.println(CLASS_NAME + METHOD_NAME);
    }

    public void onSetButtonClicked() {
        String METHOD_NAME = ".onSetButtonClicked()";
        System.out.println(CLASS_NAME + METHOD_NAME);
    }
}
