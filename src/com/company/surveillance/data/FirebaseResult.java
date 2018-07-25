package com.company.surveillance.data;

import com.company.surveillance.interfaces.data.PendingResult;
import com.company.surveillance.interfaces.event_listeners.PendingResultListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 25.07.2018.
 */
public class FirebaseResult implements PendingResult {
    private static String CLASS_NAME = "FirebaseResult";
    private List<PendingResultListener> listeners;
    private Result currentStatus;


    public FirebaseResult() {
        listeners = new ArrayList<>();
        currentStatus = Result.BAD;
    }


    @Override
    public Result getResultStatus() {
        return currentStatus;
    }

    @Override
    public void setResultStatus(Result status) {
        currentStatus = status;
        notifyListeners();
    }

    @Override
    public void addPendingResultListener(PendingResultListener listener) {
        listeners.add(listener);
    }

    @Override
    public void deletePendingResultListener(PendingResultListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void notifyListeners() {
        listeners.stream().forEach(listener -> listener.onResultAcquired(this));
    }
}
