package com.company.surveillance;

import com.company.surveillance.managers.FirebaseManager;
import com.company.surveillancedata.data_calsses.ObjectId;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.features2d.Features2d;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.Objdetect;
import org.opencv.video.Video;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class Main extends Application {
    private static final String OPEN_CV_FOLDER = "C:\\Users\\Alexander\\IdeaProjects\\Surveillance\\ExternalLibs\\OpenCV\\x64\\opencv_java330.dll";

    public static void main(String[] args) {
        System.load(OPEN_CV_FOLDER);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        test();

        Parent root = FXMLLoader.load(getClass().getResource("views/test_pane.fxml"));

        primaryStage.setTitle("TEST");
        primaryStage.setMinHeight(480);
        primaryStage.setMinWidth(640);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    // =====
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
