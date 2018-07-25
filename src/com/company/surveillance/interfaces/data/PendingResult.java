package com.company.surveillance.interfaces.data;

import com.company.surveillance.interfaces.event_listeners.PendingResultListener;

/**
 * Created by Alexander on 25.07.2018.
 */
public interface PendingResult {
    enum Result {OK, BAD}
    Result getResultStatus();
    void setResultStatus(Result status);
    void addPendingResultListener(PendingResultListener listener);
    void deletePendingResultListener(PendingResultListener listener);
    void notifyListeners();
}
