package com.company.surveillance.managers;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by Alexander on 25.07.2018.
 */
public class AccountManager {
    private static final String CLASS_NAME = "AccountManager";
    private volatile static AccountManager instance;
    private String userName = "USER";
    private String userPassword = "PASSWORD";
    private String machineName = "TEST_SERVER";


    private AccountManager() {

    }

    public static AccountManager getInstance() {
        if (instance == null) {
            synchronized (AccountManager.class) {
                if (instance == null)
                    instance = new AccountManager();
            }
        }

        return instance;
    }


    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getMachineName() {
        machineName = "UNKNOWN_PC";
        try {
            machineName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return machineName;
    }
}
