package com.company.surveillance.controllers;

import com.company.surveillance.SurveillanceEngine;
import com.company.surveillance.commands.StartCameraCommand;
import com.company.surveillance.data.Account;
import com.company.surveillance.data.FirebaseResult;
import com.company.surveillance.devices.post_processors.AdvancedMotionDetectingPostProcessor;
import com.company.surveillance.devices.video_panels.ProcessedFrameVideoPanel;
import com.company.surveillance.interfaces.data.PendingResult;
import com.company.surveillance.interfaces.devices.FrameProvider;
import com.company.surveillance.interfaces.devices.PostProcessor;
import com.company.surveillance.managers.AccountManager;
import com.company.surveillance.managers.CameraManager;
import com.company.surveillance.managers.FirebaseManager;
import com.company.surveillancedata.data_calsses.ObjectId;
import com.company.surveillancedata.interfaces.Command;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

/**
 * Created by Alexander on 25.07.2018.
 */
public class TestPaneController_V7 implements Initializable {
    private String CLASS_NAME = "TestPaneController_V7";

    public ScrollPane scrollPane;
    public ImageView imageView;
    private ProcessedFrameVideoPanel videoPanel;
    private CameraManager cameraManager;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String METHOD_NAME = ".initialize()";
        System.out.println(CLASS_NAME + METHOD_NAME);
    }

    public void onButtonOneClicked() {
        String METHOD_NAME = ".onButtonOneClicked()";
        System.out.println(CLASS_NAME + METHOD_NAME);

        FirebaseManager firebaseManager = FirebaseManager.getInstance();
    }

    public void onButtonTwoClicked() {
        String METHOD_NAME = ".onButtonTwoClicked()";
        System.out.println(CLASS_NAME + METHOD_NAME);
    }

    public void onChooseButtonClicked() {
        String METHOD_NAME = ".onChooseButtonClicked()";
        System.out.println(CLASS_NAME + METHOD_NAME);
    }

    public void onSetButtonClicked() {
        String METHOD_NAME = ".onSetButtonClicked()";
        System.out.println(CLASS_NAME + METHOD_NAME);

        AccountManager accountManager = AccountManager.getInstance();

        Account currentAccount = new Account("a", "b");
        accountManager.checkAccount(currentAccount).addPendingResultListener(result -> {
            if (result.getResultStatus() == PendingResult.Result.OK) {
                System.out.println(CLASS_NAME + METHOD_NAME + "->ACCOUNT_EXISTED");
                accountManager.setCurrentAccount(currentAccount);
            } else
                System.out.println(CLASS_NAME + METHOD_NAME + "->ACCOUNT_NOT_EXISTED");
        });
    }
}
