package com.company.surveillance.controllers;

import com.company.surveillance.SurveillanceEngine;
import com.company.surveillance.commands.StartCameraCommand;
import com.company.surveillance.devices.post_processors.AdvancedMotionDetectingPostProcessor;
import com.company.surveillance.devices.video_panels.ProcessedFrameVideoPanel;
import com.company.surveillance.interfaces.devices.FrameProvider;
import com.company.surveillance.interfaces.devices.PostProcessor;
import com.company.surveillance.managers.CameraManager;
import com.company.surveillancedata.data_calsses.ObjectId;
import com.company.surveillancedata.interfaces.Command;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

/**
 * Created by Alexander on 25.07.2018.
 */
public class TestPaneController_V6 implements Initializable {
    private String CLASS_NAME = "TestPaneController_V6";

    public ScrollPane scrollPane;
    public ImageView imageView;
    private ProcessedFrameVideoPanel videoPanel;
    private CameraManager cameraManager;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String METHOD_NAME = ".initialize()";
        System.out.println(CLASS_NAME + METHOD_NAME);

        imageView.fitWidthProperty().bind(scrollPane.widthProperty());
        imageView.fitHeightProperty().bind(scrollPane.heightProperty());

        cameraManager = CameraManager.getInstance();

        ObjectId firstCameraId = cameraManager.getCamerasIds().get(0);

        cameraManager.setCameraPostProcessor(firstCameraId, new AdvancedMotionDetectingPostProcessor());

        FrameProvider frameProvider = cameraManager.getCameraVideoFeed(firstCameraId);
        PostProcessor processedFrameProvider = cameraManager.getCameraProcessedVideoFeed(firstCameraId);

        videoPanel = new ProcessedFrameVideoPanel(imageView, frameProvider, processedFrameProvider);
    }

    public void onButtonOneClicked() {
        String METHOD_NAME = ".onButtonOneClicked()";
        System.out.println(CLASS_NAME + METHOD_NAME);

        ObjectId firstCameraId = cameraManager.getCamerasIds().get(0);
        cameraManager.startCamera(firstCameraId);
    }

    public void onButtonTwoClicked() {
        String METHOD_NAME = ".onButtonTwoClicked()";
        System.out.println(CLASS_NAME + METHOD_NAME);

        ObjectId firstCameraId = cameraManager.getCamerasIds().get(0);
        cameraManager.stopCamera(firstCameraId);
    }

    public void onChooseButtonClicked() {
        String METHOD_NAME = ".onChooseButtonClicked()";
        System.out.println(CLASS_NAME + METHOD_NAME);

        ProcessedFrameVideoPanel.Source videoPanelSource = videoPanel.getFramesSource();
        switch (videoPanelSource) {
            case ORIGINAL:
                System.out.println(CLASS_NAME + METHOD_NAME + "->SWITCH_TO_PROCESSED");
                videoPanel.setFramesSource(ProcessedFrameVideoPanel.Source.PROCESSED);
                break;

            case PROCESSED:
                System.out.println(CLASS_NAME + METHOD_NAME + "->SWITCH_TO_ORIGINAL");
                videoPanel.setFramesSource(ProcessedFrameVideoPanel.Source.ORIGINAL);
                break;

            default:
                System.out.println(CLASS_NAME + METHOD_NAME + "->UNKNOWN_VIDEO_PANEL_SOURCE: " + videoPanelSource);
        }
    }

    public void onSetButtonClicked() {
        String METHOD_NAME = ".onSetButtonClicked()";
        System.out.println(CLASS_NAME + METHOD_NAME);

        ObjectId cameraId = cameraManager.getCamerasIds().get(0);

        Command startCommand = new StartCameraCommand(new ObjectId(), new ArrayList<>(Collections.singletonList(cameraId)));

        SurveillanceEngine engine = SurveillanceEngine.getInstance();
        engine.executeCommand(startCommand);
    }
}