package com.company.surveillance.data;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Alexander on 25.07.2018.
 */
public class Account {
    private static final String CLASS_NAME = "Account";
    private String userName = "";
    private String password = "";
    private String serverName = "";


    public Account() {

    }

    public Account(String userName, String password) {
        this.userName = userName;
        this.password = password;
        try {
            this.serverName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public Account(Account other) {
        this.userName = other.userName;
        this.password = other.password;
        this.serverName = other.serverName;
    }


    public boolean isEmpty() {
        return userName.isEmpty() || password.isEmpty();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
}
