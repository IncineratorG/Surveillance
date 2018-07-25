package com.company.surveillance.interfaces.event_listeners;

import com.company.surveillance.data.ProcessedData;
import com.company.surveillance.interfaces.data.Frame;


public interface FrameProcessedEventListener {
    void onFrameProcessed(Frame originalFrame, ProcessedData data);
}
