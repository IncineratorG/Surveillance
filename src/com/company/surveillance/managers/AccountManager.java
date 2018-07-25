package com.company.surveillance.managers;

import com.company.surveillance.data.Account;
import com.company.surveillance.data.FirebaseResult;
import com.company.surveillance.interfaces.data.PendingResult;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 25.07.2018.
 */
public class AccountManager {
    private static final String CLASS_NAME = "AccountManager";
    private volatile static AccountManager instance;
    private Account currentAccount;


    private AccountManager() {
        currentAccount = new Account();
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


    public Account getCurrentAccount() {
        return currentAccount;
    }

    public void setCurrentAccount(Account currentAccount) {
        this.currentAccount = currentAccount;
    }

    public PendingResult checkAccount(Account account) {
        List<String> firebaseFieldPathArgs = new ArrayList<>();
        firebaseFieldPathArgs.add(account.getUserName());
        firebaseFieldPathArgs.add(account.getPassword());

        return FirebaseManager.getInstance().existField(firebaseFieldPathArgs);
    }
}
