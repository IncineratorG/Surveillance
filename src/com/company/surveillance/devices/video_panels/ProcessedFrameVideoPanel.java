package com.company.surveillance.devices.video_panels;

import com.company.surveillance.data.ProcessedData;
import com.company.surveillance.interfaces.data.Frame;
import com.company.surveillance.interfaces.devices.FrameAcceptor;
import com.company.surveillance.interfaces.devices.FrameProvider;
import com.company.surveillance.interfaces.devices.PostProcessor;
import com.company.surveillance.interfaces.devices.ProcessedFrameAcceptor;
import javafx.application.Platform;
import javafx.scene.image.ImageView;

/**
 * Created by Alexander on 17.07.2018.
 */
public class ProcessedFrameVideoPanel implements FrameAcceptor, ProcessedFrameAcceptor {
    private String CLASS_NAME = "ProcessedFrameVideoPanel";
    private ImageView imageView;
    private FrameProvider framesSource;
    private PostProcessor processedFramesSource;
    private Source currentFramesSource;

    public enum Source {
        ORIGINAL, PROCESSED
    }


    public ProcessedFrameVideoPanel(ImageView imageView, FrameProvider framesSource, PostProcessor processedFramesSource) {
        this.imageView = imageView;
        this.framesSource = framesSource;
        this.processedFramesSource = processedFramesSource;
        this.currentFramesSource = Source.ORIGINAL;

        this.framesSource.addNewFrameEventListener(this);
        this.processedFramesSource.addFrameProcessedEventListener(this);
    }


    public Source getFramesSource() {
        return currentFramesSource;
    }

    public void setFramesSource(Source framesSource) {
        this.currentFramesSource = framesSource;
    }


    @Override
    public void onFrameProcessed(Frame originalFrame, ProcessedData data) {
        if (currentFramesSource == Source.PROCESSED) {
            Platform.runLater(() -> imageView.setImage(data.getProcessedImage()));
        }
    }

    @Override
    public void onNewFrame(Frame frame) {
        if (currentFramesSource == Source.ORIGINAL) {
            Platform.runLater(() -> imageView.setImage(frame.getImage()));
        }
    }
}
