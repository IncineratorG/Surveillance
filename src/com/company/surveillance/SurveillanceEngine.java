package com.company.surveillance;

import com.company.surveillancedata.interfaces.Command;

/**
 * Created by Alexander on 22.07.2018.
 */
public class SurveillanceEngine {
    private volatile static SurveillanceEngine instance;


    private SurveillanceEngine() {

    }

    public static SurveillanceEngine getInstance() {
        if (instance == null) {
            synchronized (SurveillanceEngine.class) {
                if (instance == null)
                    instance = new SurveillanceEngine();
            }
        }

        return instance;
    }


    public void executeCommand(Command command) {
        command.execute();
    }
}
