package com.company.surveillance.interfaces.data;

import com.company.surveillance.interfaces.event_listeners.AccountSetEventListener;

/**
 * Created by Alexander on 25.07.2018.
 */
public interface AccountEventsProvider {
    void addAccountSetEventListener(AccountSetEventListener listener);
    void deleteAccountSetEventListener(AccountSetEventListener listener);
    void notifyAccountSetListeners();
}
