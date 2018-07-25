package com.company.surveillance.interfaces.event_listeners;

import com.company.surveillance.interfaces.data.Frame;

/**
 * Created by Alexander on 03.07.2018.
 */
public interface NewFrameEventListener {
    void onNewFrame(Frame frame);
}
