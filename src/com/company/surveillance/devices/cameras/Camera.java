package com.company.surveillance.devices.cameras;

import com.company.surveillance.data.CameraFrame;
import com.company.surveillance.devices.post_processors.DoNothingPostProcessor;
import com.company.surveillance.interfaces.data.Frame;
import com.company.surveillance.interfaces.devices.FrameAcceptor;
import com.company.surveillance.interfaces.devices.FrameProvider;
import com.company.surveillance.interfaces.devices.PostProcessor;
import com.company.surveillance.interfaces.event_listeners.FrameProcessedEventListener;
import com.company.surveillance.interfaces.event_listeners.NewFrameEventListener;
import com.company.surveillancedata.data_calsses.ObjectId;
import com.github.sarxos.webcam.Webcam;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 10.07.2018.
 */
public class Camera implements FrameProvider, FrameAcceptor, PostProcessor {
    private List<NewFrameEventListener> newFrameEventListeners;
    private List<FrameProcessedEventListener> frameProcessedEventListeners;
    private CameraFrame frame;
    private Webcam camera;
    private Service<CameraFrame> cameraService;
    private PostProcessor postProcessor;
    private ObjectId identifier;


    public Camera(String cameraName) {
        newFrameEventListeners = new ArrayList<>();
        frameProcessedEventListeners = new ArrayList<>();

        camera = Webcam.getWebcamByName(cameraName);

        frame = new CameraFrame();
        frame.addNewFrameEventListener(this);

        postProcessor = new DoNothingPostProcessor();
        addNewFrameEventListener(postProcessor);

        identifier = new ObjectId(cameraName);
    }

    public Camera(String cameraName, PostProcessor postProcessor) {
        newFrameEventListeners = new ArrayList<>();
        frameProcessedEventListeners = new ArrayList<>();

        camera = Webcam.getWebcamByName(cameraName);

        frame = new CameraFrame();
        frame.addNewFrameEventListener(this);

        this.postProcessor = postProcessor;
        addNewFrameEventListener(this.postProcessor);

        identifier = new ObjectId(cameraName);
    }


    public void setPostProcessor(PostProcessor postProcessor) {
        for (FrameProcessedEventListener listener : frameProcessedEventListeners)
            this.postProcessor.deleteFrameProcessedEventListener(listener);

        deleteNewFrameEventListener(this.postProcessor);

        this.postProcessor = postProcessor;
        addNewFrameEventListener(this.postProcessor);

        for (FrameProcessedEventListener listener : frameProcessedEventListeners)
            this.postProcessor.addFrameProcessedEventListener(listener);
    }


    public void start() {
        if (cameraService != null)
            cameraService.cancel();
        if (camera.isOpen())
            camera.close();

        Dimension[] dimensions = camera.getViewSizes();
        if (dimensions.length > 0)
            camera.setViewSize(dimensions[dimensions.length - 1]);

        camera.open();

        cameraService = new Service<CameraFrame>() {
            @Override
            protected Task<CameraFrame> createTask() {
                return new Task<CameraFrame>() {
                    @Override
                    protected CameraFrame call() throws Exception {
                        while (true) {
                            if (!camera.isOpen())
                                break;

                            BufferedImage image = camera.getImage();
                            if (image == null)
                                break;

                            frame.setImage(SwingFXUtils.toFXImage(image, null));
                            frame.setTimeStamp(System.currentTimeMillis());
                            frame.setId(new ObjectId());                        }

                        return null;
                    }
                };
            }
        };

        cameraService.start();
    }

    public void stop() {
        if (cameraService != null)
            cameraService.cancel();

        camera.close();
    }


    public ObjectId getIdentifier() {
        return identifier;
    }

    public void setIdentifier(ObjectId identifier) {
        this.identifier = identifier;
    }


    @Override
    public void addNewFrameEventListener(NewFrameEventListener listener) {
        newFrameEventListeners.add(listener);
    }

    @Override
    public void deleteNewFrameEventListener(NewFrameEventListener listener) {
        newFrameEventListeners.remove(listener);
    }


    @Override
    public void addFrameProcessedEventListener(FrameProcessedEventListener listener) {
        postProcessor.addFrameProcessedEventListener(listener);
        frameProcessedEventListeners.add(listener);
    }

    @Override
    public void deleteFrameProcessedEventListener(FrameProcessedEventListener listener) {
        postProcessor.deleteFrameProcessedEventListener(listener);
        frameProcessedEventListeners.remove(listener);
    }


    @Override
    public void notifyListeners() {
        newFrameEventListeners.stream().forEach(listener -> listener.onNewFrame(frame));
    }

    @Override
    public List<FrameProcessedEventListener> getFrameProcessedEventListeners() {
        return postProcessor.getFrameProcessedEventListeners();
    }

    @Override
    public void onNewFrame(Frame frame) {
        notifyListeners();
    }
}
