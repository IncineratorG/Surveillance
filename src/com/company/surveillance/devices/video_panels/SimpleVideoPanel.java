package com.company.surveillance.devices.video_panels;

import com.company.surveillance.interfaces.data.Frame;
import com.company.surveillance.interfaces.devices.FrameAcceptor;
import com.company.surveillance.interfaces.devices.FrameProvider;
import javafx.application.Platform;
import javafx.scene.image.ImageView;



public class SimpleVideoPanel implements FrameAcceptor {
    private ImageView imageView;
    private FrameProvider framesSource;


    public SimpleVideoPanel(FrameProvider videoSource, ImageView imageView) {
        this.framesSource = videoSource;
        this.framesSource.addNewFrameEventListener(this);

        this.imageView = imageView;
    }


    @Override
    public void onNewFrame(Frame frame) {
        Platform.runLater(() -> imageView.setImage(frame.getImage()));
    }
}
