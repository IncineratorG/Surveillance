package com.company.surveillance;

import com.company.surveillance.helpers.OpenCVHelper;
import com.company.surveillance.managers.FirebaseManager;
import com.company.surveillancedata.data_calsses.ObjectId;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.Objdetect;
import org.opencv.video.Video;

import java.util.*;
import java.util.stream.Collectors;


public class Main extends Application {
    private static final String CLASS_NAME = "Main";
    private static final String OPEN_CV_FOLDER = "C:\\Users\\Alexander\\IdeaProjects\\Surveillance\\ExternalLibs\\OpenCV\\x64\\opencv_java330.dll";

    private static final String OPEN_CV_FOLDER_32 = "C:\\Users\\Alexander\\IdeaProjects\\Surveillance\\ExternalLibs\\OpenCV\\x86\\opencv_java330.dll";
    private static final String OPEN_CV_FOLDER_64 = "C:\\Users\\Alexander\\IdeaProjects\\Surveillance\\ExternalLibs\\OpenCV\\x64\\opencv_java330.dll";

    public static void main(String[] args) {
        String architecture = System.getProperty("sun.arch.data.model");
        if (architecture.contains("64"))
            System.load(OPEN_CV_FOLDER_64);
        else
            System.load(OPEN_CV_FOLDER_32);

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        test();
//        test_2();

        Parent root = FXMLLoader.load(getClass().getResource("views/test_pane.fxml"));

        primaryStage.setTitle("TEST");
        primaryStage.setMinHeight(480);
        primaryStage.setMinWidth(640);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    // =====
    private void test_2() {
        String METHOD_NAME = ".test_2()";
        System.out.println(CLASS_NAME + METHOD_NAME);

        String largeImagePath = "C:\\Empty files\\im.jpg";
        String smallImagePath = "C:\\Empty files\\image_5_reduced.jpg";

        Mat largeImage = Imgcodecs.imread(largeImagePath);
        Mat smallImage = Imgcodecs.imread(smallImagePath);

        FeatureDetector  fd = FeatureDetector.create(FeatureDetector.BRISK);
        final MatOfKeyPoint keyPointsLarge = new MatOfKeyPoint();
        final MatOfKeyPoint keyPointsSmall = new MatOfKeyPoint();

        fd.detect(largeImage, keyPointsLarge);
        fd.detect(smallImage, keyPointsSmall);

        System.out.println("keyPoints.size() : "+keyPointsLarge.size());
        System.out.println("keyPoints2.size() : "+keyPointsSmall.size());

        Mat descriptorsLarge = new Mat();
        Mat descriptorsSmall = new Mat();

        DescriptorExtractor extractor = DescriptorExtractor.create(DescriptorExtractor.BRISK);
        extractor.compute(largeImage, keyPointsLarge, descriptorsLarge);
        extractor.compute(smallImage, keyPointsSmall, descriptorsSmall);

        System.out.println("descriptorsA.size() : "+descriptorsLarge.size());
        System.out.println("descriptorsB.size() : "+descriptorsSmall.size());

        MatOfDMatch matches = new MatOfDMatch();

        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMINGLUT);
        matcher.match(descriptorsLarge, descriptorsSmall, matches);

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
//    private void test_2() {
//        String METHOD_NAME = ".test_2()";
//        System.out.println(CLASS_NAME + METHOD_NAME);
//
//        String pathImage = "C:\\Empty files\\image.jpg";
//
//        Mat image = Imgcodecs.imread(pathImage);
//
//        MatOfKeyPoint imageKeyPoints = new MatOfKeyPoint();
//
//        FeatureDetector fd = FeatureDetector.create(FeatureDetector.BRISK);
//        fd.detect(image, imageKeyPoints);
//
//        Mat imageDescriptors = new Mat();
//
//        DescriptorExtractor extractor = DescriptorExtractor.create(DescriptorExtractor.BRISK);
//        extractor.compute(image, imageKeyPoints, imageDescriptors);
//
//
//    }
//    private void test_2() {
//        String METHOD_NAME = ".test_2()";
//        System.out.println(CLASS_NAME + METHOD_NAME);
//
//        FeatureDetector  fd = FeatureDetector.create(FeatureDetector.BRISK);
//        final MatOfKeyPoint keyPointsLarge = new MatOfKeyPoint();
//        final MatOfKeyPoint keyPointsSmall = new MatOfKeyPoint();
//
//        fd.detect(largeImage, keyPointsLarge);
//        fd.detect(smallImage, keyPointsSmall);
//
//        System.out.println("keyPoints.size() : "+keyPointsLarge.size());
//        System.out.println("keyPoints2.size() : "+keyPointsSmall.size());
//
//        Mat descriptorsLarge = new Mat();
//        Mat descriptorsSmall = new Mat();
//
//        DescriptorExtractor extractor = DescriptorExtractor.create(DescriptorExtractor.BRISK);
//        extractor.compute(largeImage, keyPointsLarge, descriptorsLarge);
//        extractor.compute(smallImage, keyPointsSmall, descriptorsSmall);
//
//        System.out.println("descriptorsA.size() : "+descriptorsLarge.size());
//        System.out.println("descriptorsB.size() : "+descriptorsSmall.size());
//
//        MatOfDMatch matches = new MatOfDMatch();
//
//        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMINGLUT);
//        matcher.match(descriptorsLarge, descriptorsSmall, matches);
//
//        System.out.println("matches.size() : "+matches.size());
//
//        MatOfDMatch matchesFiltered = new MatOfDMatch();
//
//        List<DMatch> matchesList = matches.toList();
//        List<DMatch> bestMatches= new ArrayList<DMatch>();
//
//        Double max_dist = 0.0;
//        Double min_dist = 100.0;
//
//        for (int i = 0; i < matchesList.size(); i++)
//        {
//            Double dist = (double) matchesList.get(i).distance;
//
//            if (dist < min_dist && dist != 0)
//            {
//                min_dist = dist;
//            }
//
//            if (dist > max_dist)
//            {
//                max_dist = dist;
//            }
//
//        }
//
//        System.out.println("max_dist : "+max_dist);
//        System.out.println("min_dist : "+min_dist);
//
//        double threshold = 3 * min_dist;
//        double threshold2 = 2 * min_dist;
//
//        if (threshold2 >= max_dist)
//        {
//            threshold = min_dist * 1.1;
//        }
//        else if (threshold >= max_dist)
//        {
//            threshold = threshold2 * 1.4;
//        }
//
//        System.out.println("Threshold : "+threshold);
//
//        for (int i = 0; i < matchesList.size(); i++)
//        {
//            Double dist = (double) matchesList.get(i).distance;
//            System.out.println(String.format(i + " match distance best : %s", dist));
//            if (dist < threshold)
//            {
//                bestMatches.add(matches.toList().get(i));
//                System.out.println(String.format(i + " best match added : %s", dist));
//            }
//        }
//
//
//        matchesFiltered.fromList(bestMatches);
//
//        System.out.println("matchesFiltered.size() : " + matchesFiltered.size());
//    }

    private void test() {
        System.out.println("TEST");

        List<ObjectId> list = new ArrayList<>();
        list.add(new ObjectId("ONE"));
        list.add(new ObjectId("TWO"));
        list.add(new ObjectId("THREE"));

//        Net n = Dnn.readNetFromCaffe()

//        List<Long> longList = list.stream()
//                                    .filter(objectId -> objectId.getObjectName().equals("TWO"))
//                                    .map(ObjectId::getIntegerId)
//                                    .collect(Collectors.toList());
//
//        longList.stream().forEach(val -> System.out.println(val));


//        list.stream()
//                .filter(i -> i.getObjectName().equals("TWO"))
//                .forEach(objectId -> System.out.println(objectId.getObjectName()));
    }
    // =====

    @Override
    public void stop() throws Exception {
        FirebaseManager.getInstance().closeConnection();
        super.stop();
    }
}
