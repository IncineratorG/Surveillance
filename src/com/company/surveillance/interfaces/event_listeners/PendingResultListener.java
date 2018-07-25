package com.company.surveillance.interfaces.event_listeners;

import com.company.surveillance.interfaces.data.PendingResult;

/**
 * Created by Alexander on 25.07.2018.
 */
public interface PendingResultListener {
    void onResultAcquired(PendingResult result);
}
