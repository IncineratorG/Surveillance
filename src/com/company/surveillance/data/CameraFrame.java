package com.company.surveillance.data;

import com.company.surveillance.interfaces.data.Frame;
import com.company.surveillance.interfaces.devices.FrameProvider;
import com.company.surveillance.interfaces.event_listeners.NewFrameEventListener;
import com.company.surveillancedata.data_calsses.ObjectId;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Alexander on 03.07.2018.
 */
public class CameraFrame implements Frame, FrameProvider {
    private List<NewFrameEventListener> listeners;
    private AtomicReference<Image> currentFrame;
    private long timeStamp;
    private ObjectId id;


    public CameraFrame() {
        currentFrame = new AtomicReference<>();
        listeners = new ArrayList<>();

        this.timeStamp = System.currentTimeMillis();
        this.id = new ObjectId();
    }

    public CameraFrame(long timeStamp, ObjectId id) {
        currentFrame = new AtomicReference<>();
        listeners = new ArrayList<>();

        this.timeStamp = timeStamp;
        this.id = id;
    }

    public CameraFrame(CameraFrame other) {
        this.listeners = other.listeners;
        this.currentFrame = other.currentFrame;
        this.timeStamp = other.timeStamp;
        this.id = other.id;
    }


    @Override
    public void setImage(Image image) {
        currentFrame.set(image);
        notifyListeners();
    }

    @Override
    public Image getImage() {
        return currentFrame.get();
    }


    @Override
    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public long getTimeStamp() {
        return timeStamp;
    }


    @Override
    public void setId(ObjectId id) {
        this.id = id;
    }

    @Override
    public ObjectId getId() {
        return id;
    }


    @Override
    public void addNewFrameEventListener(NewFrameEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void deleteNewFrameEventListener(NewFrameEventListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void notifyListeners() {
        for (NewFrameEventListener listener : listeners)
            listener.onNewFrame(this);
    }
}
