package com.company.surveillance.interfaces.devices;

import com.company.surveillance.interfaces.event_listeners.FrameProcessedEventListener;

import java.util.List;

/**
 * Created by Alexander on 03.07.2018.
 */
public interface PostProcessor extends FrameAcceptor {
    void addFrameProcessedEventListener(FrameProcessedEventListener listener);
    void deleteFrameProcessedEventListener(FrameProcessedEventListener listener);
    void notifyListeners();
    List<FrameProcessedEventListener> getFrameProcessedEventListeners();
}
