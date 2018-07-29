package com.company.surveillance.controllers;

import com.company.surveillance.SurveillanceEngine;
import com.company.surveillance.commands.StartCameraCommand;
import com.company.surveillance.data.Account;
import com.company.surveillance.data.FirebaseResult;
import com.company.surveillance.devices.post_processors.AdvancedMotionDetectingPostProcessor;
import com.company.surveillance.devices.video_panels.ProcessedFrameVideoPanel;
import com.company.surveillance.helpers.OpenCVHelper;
import com.company.surveillance.interfaces.data.PendingResult;
import com.company.surveillance.interfaces.devices.FrameProvider;
import com.company.surveillance.interfaces.devices.PostProcessor;
import com.company.surveillance.managers.AccountManager;
import com.company.surveillance.managers.CameraManager;
import com.company.surveillance.managers.FirebaseManager;
import com.company.surveillancedata.data_calsses.ObjectId;
import com.company.surveillancedata.interfaces.Command;
import com.sun.deploy.util.SystemUtils;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.features2d.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Alexander on 25.07.2018.
 */
public class TestPaneController_V8 implements Initializable {
    private String CLASS_NAME = "TestPaneController_V8";

    public ScrollPane scrollPane;
    public ImageView imageView;
    private ProcessedFrameVideoPanel videoPanel;
    private CameraManager cameraManager;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String METHOD_NAME = ".initialize()";
        System.out.println(CLASS_NAME + METHOD_NAME);
    }

    public void onButtonOneClicked() {
        String METHOD_NAME = ".onButtonOneClicked()";
        System.out.println(CLASS_NAME + METHOD_NAME);
    }

    public void onButtonTwoClicked() {
        String METHOD_NAME = ".onButtonTwoClicked()";
        System.out.println(CLASS_NAME + METHOD_NAME);

        String largeImagePath = "C:\\Empty files\\Photos\\P1010048.JPG";
        String smallImagePath = "C:\\Empty files\\Photos\\P2240005.JPG";

        Mat largeImage = Imgcodecs.imread(largeImagePath);
        Mat smallImage = Imgcodecs.imread(smallImagePath);

        Imgproc.cvtColor(largeImage, largeImage, Imgproc.COLOR_RGB2GRAY);
        Imgproc.cvtColor(smallImage, smallImage, Imgproc.COLOR_RGB2GRAY);

        System.out.println(CLASS_NAME + METHOD_NAME + "->SAME_TYPE: " + (largeImage.type() == smallImage.type()));

        FeatureDetector detector = FeatureDetector.create(FeatureDetector.ORB);
        DescriptorExtractor descriptor = DescriptorExtractor.create(DescriptorExtractor.ORB);
        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);

        Mat descriptors1 = new Mat();
        Mat descriptors2 = new Mat();
        MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
        MatOfKeyPoint keypoints2 = new MatOfKeyPoint();

        detector.detect(largeImage, keypoints1);
        descriptor.compute(largeImage, keypoints1, descriptors1);

        detector.detect(smallImage, keypoints2);
        descriptor.compute(smallImage, keypoints2, descriptors2);

        MatOfDMatch matches = new MatOfDMatch();

        matcher.match(descriptors1, descriptors2, matches);

        List<DMatch> matchesList = matches.toList();

        Double max_dist = 0.0;
        Double min_dist = 100.0;

        for (int i = 0; i < matchesList.size(); i++) {
            Double dist = (double) matchesList.get(i).distance;
            if (dist < min_dist)
                min_dist = dist;
            if (dist > max_dist)
                max_dist = dist;
        }

        List<DMatch> good_matches = new ArrayList<>();
        for (int i = 0; i < matchesList.size(); i++) {
            if (matchesList.get(i).distance <= (1.5 * min_dist))
                good_matches.add(matchesList.get(i));
        }

        MatOfDMatch goodMatches = new MatOfDMatch();
        goodMatches.fromList(good_matches);

        Mat outputImg = new Mat();
        MatOfByte drawnMatches = new MatOfByte();

        Features2d.drawMatches(largeImage, keypoints1,
                smallImage, keypoints2,
                goodMatches, outputImg,
                new Scalar(240, 58, 100), new Scalar(255, 120, 46),
                drawnMatches, Features2d.NOT_DRAW_SINGLE_POINTS);

//        Calib3d.findHomography()

        imageView.fitWidthProperty().bind(scrollPane.widthProperty());
        imageView.fitHeightProperty().bind(scrollPane.heightProperty());
        imageView.setImage(OpenCVHelper.mat2Image(outputImg));
    }

    public void onChooseButtonClicked() {
        String METHOD_NAME = ".onChooseButtonClicked()";
        System.out.println(CLASS_NAME + METHOD_NAME);

        String largeImagePath = "C:\\OpenCV_3.3.0\\sources\\samples\\data\\box_in_scene.png";
        String smallImagePath = "C:\\OpenCV_3.3.0\\sources\\samples\\data\\box.png";

        Mat largeImage = Imgcodecs.imread(largeImagePath);
        Mat smallImage = Imgcodecs.imread(smallImagePath);

        FeatureDetector fd = FeatureDetector.create(FeatureDetector.ORB);
        final MatOfKeyPoint keyPointsLarge = new MatOfKeyPoint();
        final MatOfKeyPoint keyPointsSmall = new MatOfKeyPoint();

        fd.detect(largeImage, keyPointsLarge);
        fd.detect(smallImage, keyPointsSmall);

        System.out.println("keyPoints.size() : "+keyPointsLarge.size());
        System.out.println("keyPoints2.size() : "+keyPointsSmall.size());

        Mat descriptorsLarge = new Mat();
        Mat descriptorsSmall = new Mat();

        DescriptorExtractor extractor = DescriptorExtractor.create(DescriptorExtractor.ORB);
        extractor.compute(largeImage, keyPointsLarge, descriptorsLarge);
        extractor.compute(smallImage, keyPointsSmall, descriptorsSmall);

        System.out.println("descriptorsA.size() : "+descriptorsLarge.size());
        System.out.println("descriptorsB.size() : "+descriptorsSmall.size());

        MatOfDMatch matches = new MatOfDMatch();

        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
        matcher.match(descriptorsLarge, descriptorsSmall, matches);
//        matcher.knnMatch(descriptorsLarge, descriptorsSmall, matches, 2);

        System.out.println("matches.size() : "+matches.size());

        MatOfDMatch matchesFiltered = new MatOfDMatch();

        List<DMatch> matchesList = matches.toList();
        List<DMatch> bestMatches= new ArrayList<DMatch>();

        Double max_dist = 0.0;
        Double min_dist = 100.0;

        for (int i = 0; i < matchesList.size(); i++)
        {
            Double dist = (double) matchesList.get(i).distance;

            if (dist < min_dist && dist != 0)
            {
                min_dist = dist;
            }

            if (dist > max_dist)
            {
                max_dist = dist;
            }

        }

        System.out.println("max_dist : "+max_dist);
        System.out.println("min_dist : "+min_dist);

        // ===
        Mat outImage = new Mat();

        Features2d.drawMatches(largeImage, keyPointsLarge, smallImage, keyPointsSmall, matches, outImage);

        imageView.fitWidthProperty().bind(scrollPane.widthProperty());
        imageView.fitHeightProperty().bind(scrollPane.heightProperty());
        imageView.setImage(OpenCVHelper.mat2Image(outImage));
        // ===

        if(min_dist > 50 )
        {
            System.out.println("No match found");
            System.out.println("Just return ");
            return;
//            return false;
        }

        double threshold = 3 * min_dist;
        double threshold2 = 2 * min_dist;

        if (threshold > 75)
        {
            threshold  = 75;
        }
        else if (threshold2 >= max_dist)
        {
            threshold = min_dist * 1.1;
        }
        else if (threshold >= max_dist)
        {
            threshold = threshold2 * 1.4;
        }

        System.out.println("Threshold : "+threshold);

        for (int i = 0; i < matchesList.size(); i++)
        {
            Double dist = (double) matchesList.get(i).distance;

            if (dist < threshold)
            {
                bestMatches.add(matches.toList().get(i));
                //System.out.println(String.format(i + " best match added : %s", dist));
            }
        }

        matchesFiltered.fromList(bestMatches);

        System.out.println("matchesFiltered.size() : " + matchesFiltered.size());


        if(matchesFiltered.rows() >= 4)
        {
            System.out.println("match found");
            return;
//            return true;
        }
        else
        {
            System.out.println("no match found");
            return;
//            return false;
        }
    }

    public void onSetButtonClicked() {
        String METHOD_NAME = ".onSetButtonClicked()";
        System.out.println(CLASS_NAME + METHOD_NAME);

        String classifierPath = "C:\\Empty files\\face-detection-master\\resources\\haarcascades\\haarcascade_frontalface_alt2.xml";
        String imagePath = "C:\\Empty files\\Photos\\P2240019.JPG";
        Mat frame = Imgcodecs.imread(imagePath);

        CascadeClassifier faceCascade = new CascadeClassifier();
        faceCascade.load(classifierPath);
        int absoluteFaceSize = 0;


        MatOfRect faces = new MatOfRect();
        Mat grayFrame = new Mat();

        // convert the frame in gray scale
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
        // equalize the frame histogram to improve the result
        Imgproc.equalizeHist(grayFrame, grayFrame);

        // compute minimum face size (20% of the frame height, in our case)
        if (absoluteFaceSize == 0)
        {
            int height = grayFrame.rows();
            if (Math.round(height * 0.2f) > 0)
                absoluteFaceSize = Math.round(height * 0.2f);
        }

        // detect faces
        faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
                new Size(absoluteFaceSize, absoluteFaceSize), new Size());

        // each rectangle in faces is a face: draw them!
        Rect[] facesArray = faces.toArray();
        for (int i = 0; i < facesArray.length; i++)
            Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0), 3);


        imageView.fitWidthProperty().bind(scrollPane.widthProperty());
        imageView.fitHeightProperty().bind(scrollPane.heightProperty());
        imageView.setImage(OpenCVHelper.mat2Image(frame));
    }
}
