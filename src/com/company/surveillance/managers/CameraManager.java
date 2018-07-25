package com.company.surveillance.managers;


import com.company.surveillance.devices.cameras.Camera;
import com.company.surveillance.interfaces.devices.FrameProvider;
import com.company.surveillance.interfaces.devices.PostProcessor;
import com.company.surveillancedata.data_calsses.ObjectId;
import com.github.sarxos.webcam.Webcam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CameraManager {
    private String CLASS_NAME = "CameraManager";
    private volatile static CameraManager instance;
    private List<Camera> camerasList;


    private CameraManager() {
        camerasList = new ArrayList<>();
        updateCamerasList();
    }

    public static CameraManager getInstance() {
        if (instance == null) {
            synchronized (CameraManager.class) {
                if (instance == null)
                    instance = new CameraManager();
            }
        }

        return instance;
    }


    public void startCamera(ObjectId cameraId) {
        String METHOD_NAME = ".startCamera()";
        System.out.println(CLASS_NAME + METHOD_NAME + "->CAMERA_ID: " + cameraId.getIntegerId());

        camerasList.stream()
                .filter(camera -> camera.getIdentifier() == cameraId)
                .forEach(camera -> camera.start());
    }

    public void stopCamera(ObjectId cameraId) {
        String METHOD_NAME = ".stopCamera()";
        System.out.println(CLASS_NAME + METHOD_NAME + "->CAMERA_ID: " + cameraId.getIntegerId());

        camerasList.stream()
                .filter(camera -> camera.getIdentifier() == cameraId)
                .forEach(camera -> camera.stop());
    }

    public void setCameraPostProcessor(ObjectId cameraId, PostProcessor postProcessor) {
        String METHOD_NAME = ".setCameraPostProcessor()";
        System.out.println(CLASS_NAME + METHOD_NAME + "->CAMERA_ID: " + cameraId.getIntegerId());

        camerasList.stream()
                .filter(camera -> camera.getIdentifier() == cameraId)
                .forEach(camera -> camera.setPostProcessor(postProcessor));
    }

    public List<ObjectId> getCamerasIds() {
        return camerasList.stream()
                .map(Camera::getIdentifier)
                .collect(Collectors.toList());
    }

    public FrameProvider getCameraVideoFeed(ObjectId cameraId) {
        for (int i = 0; i < camerasList.size(); ++i)
            if (camerasList.get(i).getIdentifier() == cameraId)
                return camerasList.get(i);

        return null;
    }

    public PostProcessor getCameraProcessedVideoFeed(ObjectId cameraId) {
        for (int i = 0; i < camerasList.size(); ++i)
            if (camerasList.get(i).getIdentifier() == cameraId)
                return camerasList.get(i);

        return null;
    }


    private void updateCamerasList() {
        camerasList.clear();

        Webcam.getWebcams().stream()
                .forEach(webcam -> camerasList.add(new Camera(webcam.getName())));
    }
}
