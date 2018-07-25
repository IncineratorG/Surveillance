package com.company.surveillance.interfaces.data;

import com.company.surveillance.data.MotionData;

/**
 * Created by Alexander on 03.07.2018.
 */
public interface ProcessedFrame extends Frame {
    MotionData getMotionData();
}
