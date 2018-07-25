package com.company.surveillance.controllers;

import com.company.surveillance.devices.cameras.Camera;
import com.company.surveillance.devices.video_panels.SimpleVideoPanel;
import com.github.sarxos.webcam.Webcam;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;



public class TestPaneController_V2 implements Initializable {
    public ScrollPane scrollPane;
    public ImageView imageView;
    private SimpleVideoPanel videoPanel;
    private Camera camera;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("TEST_PANE_CONTROLLER_V2_INITIALIZED");

        imageView.fitWidthProperty().bind(scrollPane.widthProperty());
        imageView.fitHeightProperty().bind(scrollPane.heightProperty());

        camera = new Camera(Webcam.getDefault().getName());

        videoPanel = new SimpleVideoPanel(camera, imageView);
    }

    public void onButtonOneClicked() {
        System.out.println("onButtonOneClicked()");
        camera.start();
    }

    public void onButtonTwoClicked() {
        System.out.println("onButtonTwoClicked");
        camera.stop();
    }
}
