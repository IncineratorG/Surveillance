package com.company.surveillance.managers;

import com.company.surveillance.data.Account;
import com.company.surveillance.interfaces.data.AccountEventsProvider;
import com.company.surveillance.interfaces.data.PendingResult;
import com.company.surveillance.interfaces.event_listeners.AccountSetEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 25.07.2018.
 */
public class AccountManager implements AccountEventsProvider {
    private static final String CLASS_NAME = "AccountManager";
    private volatile static AccountManager instance;
    private Account currentAccount;
    private List<AccountSetEventListener> listeners;


    private AccountManager() {
        currentAccount = new Account();
        listeners = new ArrayList<>();
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
        notifyAccountSetListeners();
    }

    public PendingResult checkAccount(Account account) {
        List<String> firebaseFieldPathArgs = new ArrayList<>();
        firebaseFieldPathArgs.add(account.getUserName());
        firebaseFieldPathArgs.add(account.getPassword());

        return FirebaseManager.getInstance().existField(firebaseFieldPathArgs);
    }


    @Override
    public void addAccountSetEventListener(AccountSetEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void deleteAccountSetEventListener(AccountSetEventListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void notifyAccountSetListeners() {
        listeners.stream().forEach(listener -> listener.onAccountSet(currentAccount));
    }
}
