package com.company.surveillance.controllers;

import com.company.surveillance.devices.cameras.Camera;
import com.company.surveillance.devices.post_processors.AdvancedMotionDetectingPostProcessor;
import com.company.surveillance.devices.post_processors.MotionDetectingPostProcessor;
import com.company.surveillance.devices.video_panels.ProcessedFrameVideoPanel;
import com.company.surveillance.devices.video_panels.SimpleVideoPanel;
import com.company.surveillance.managers.CameraManager;
import com.company.surveillancedata.data_calsses.ObjectId;
import com.github.sarxos.webcam.Webcam;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Alexander on 17.07.2018.
 */
public class TestPaneController_V3 implements Initializable {
    private String CLASS_NAME = "TestPaneController_V3.";

    public ScrollPane scrollPane;
    public ImageView imageView;
    private ProcessedFrameVideoPanel videoPanel;
    private Camera camera;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("TEST_PANE_CONTROLLER_V3_INITIALIZED");

        imageView.fitWidthProperty().bind(scrollPane.widthProperty());
        imageView.fitHeightProperty().bind(scrollPane.heightProperty());

        camera = new Camera(Webcam.getDefault().getName(), new AdvancedMotionDetectingPostProcessor());
//        camera = new Camera(Webcam.getDefault().getName());
//        camera.setPostProcessor(new AdvancedMotionDetectingPostProcessor());

        videoPanel = new ProcessedFrameVideoPanel(imageView, camera, camera);
    }

    public void onButtonOneClicked() {
        System.out.println("onButtonOneClicked()");
        camera.start();
    }

    public void onButtonTwoClicked() {
        System.out.println("onButtonTwoClicked");
        camera.stop();
    }

    public void onChooseButtonClicked() {
        String METHOD_NAME = "onChooseButtonClicked()";

        System.out.println(METHOD_NAME);

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
    }
}
