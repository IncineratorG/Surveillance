package com.company.surveillance.devices.post_processors;

import com.company.surveillance.data.ProcessedData;
import com.company.surveillance.interfaces.data.Frame;
import com.company.surveillance.interfaces.devices.PostProcessor;
import com.company.surveillance.interfaces.event_listeners.FrameProcessedEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Alexander on 03.07.2018.
 */
public class DoNothingPostProcessor implements PostProcessor {
    private ProcessedData processedData;
    private List<FrameProcessedEventListener> listeners;
    private Frame originalFrame;


    public DoNothingPostProcessor() {
        processedData = new ProcessedData();
        listeners = new ArrayList<>();
    }


    @Override
    public void addFrameProcessedEventListener(FrameProcessedEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void deleteFrameProcessedEventListener(FrameProcessedEventListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void notifyListeners() {
        for (FrameProcessedEventListener listener : listeners)
            listener.onFrameProcessed(originalFrame, processedData);
    }

    @Override
    public void onNewFrame(Frame frame) {
        originalFrame = frame;
        notifyListeners();
    }

    @Override
    public List<FrameProcessedEventListener> getFrameProcessedEventListeners() {
        return listeners;
    }
}
