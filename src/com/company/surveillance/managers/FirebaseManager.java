package com.company.surveillance.managers;

import com.company.surveillance.data.Account;
import com.company.surveillance.data.FirebaseResult;
import com.company.surveillance.helpers.SerializationHelper;
import com.company.surveillance.interfaces.data.PendingResult;
import com.company.surveillance.interfaces.event_listeners.AccountSetEventListener;
import com.company.surveillancedata.data_calsses.CommunicationMessage;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FirebaseManager implements AccountSetEventListener {
    private String CLASS_NAME = "FirebaseManager";

    private volatile static FirebaseManager instance;
    private static Firebase firebaseDatabase;
    private static final String FIREBASE_URL = "https://surveillance-136a9.firebaseio.com/";

    private List<String> incomingFieldArgs;
    private List<String> outgoingFieldArgs;
    private List<String> statusFieldArgs;
    private final String INCOMING_FIELD = "INCOMING_FIELD";
    private final String OUTGOING_FIELD = INCOMING_FIELD;
    private final String STATUS_FIELD = "SERVER_STATUS";
    private boolean accountSet = false;

    private ValueEventListener firebaseEventListener;



    private FirebaseManager() {
        incomingFieldArgs = new ArrayList<>();
        outgoingFieldArgs = new ArrayList<>();
        statusFieldArgs = new ArrayList<>();

        firebaseDatabase = new Firebase(FIREBASE_URL);

        AccountManager.getInstance().addAccountSetEventListener(this);

        firebaseEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String METHOD_NAME = ".firebaseEventListener->onDataChanged()";

                if (dataSnapshot == null || dataSnapshot.getValue() == null) {
                    System.out.println(CLASS_NAME + METHOD_NAME + "->NO_DATA");
                    return;
                }

                CommunicationMessage message = (CommunicationMessage) SerializationHelper.objectFromString(dataSnapshot.getValue().toString());
                if (message == null) {
                    System.out.println(CLASS_NAME + METHOD_NAME + "->MESSAGE_IS_NULL");
                    return;
                }

                switch (message.getSender()) {
                    case UNKNOWN:
                        System.out.println(CLASS_NAME + METHOD_NAME + "->SENDER_UNKNOWN");
                        break;

                    case SERVER:
                        System.out.println(CLASS_NAME + METHOD_NAME + "->SENDER_IS_SERVER");
                        break;

                    case CLIENT:
                        System.out.println(CLASS_NAME + METHOD_NAME + "->SENDER_IS_CLIENT");
                        break;

                    default:
                        System.out.println(CLASS_NAME + METHOD_NAME + "->UNKNOWN_SENDER_TYPE: " + message.getSender());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        };
    }

    public static FirebaseManager getInstance() {
        if (instance == null) {
            synchronized (FirebaseManager.class) {
                if (instance == null)
                    instance = new FirebaseManager();
            }
        }

        return instance;
    }


    public void closeConnection() {
        if (firebaseDatabase != null)
            firebaseDatabase.goOffline();
    }


    public PendingResult existField(List<String> fieldPathArgs) {
        String METHOD_NAME = ".existField()";

        FirebaseResult pendingResult = new FirebaseResult();

        Firebase field = getFirebaseField(fieldPathArgs);
        field.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null || dataSnapshot.getValue() == null || dataSnapshot.getValue().toString().isEmpty())
                    pendingResult.setResultStatus(PendingResult.Result.BAD);
                else
                    pendingResult.setResultStatus(PendingResult.Result.OK);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });

        return pendingResult;
    }

    public void sendMessage(CommunicationMessage message) {
        String messageData = SerializationHelper.objectToString(message);
//        setOutgoingData(messageData);
    }

    public void sendTestString(String string) {
        String METHOD_NAME = ".sendTestString()";

        if (!accountSet) {
            System.out.println(CLASS_NAME + METHOD_NAME + "->ACCOUNT_NOT_SET");
            return;
        }

        getFirebaseField(outgoingFieldArgs).setValue(string);
    }

    public void sendStatus() {

    }


    private void setOutgoingData(String data) {
//        firebaseDatabase
//                .child(USER_NAME)
//                .child(PASSWORD)
//                .child(SERVER_NAME)
//                .child(OUTGOING_FIELD)
//                .setValue(data);
    }

    private Firebase getIncomingCommunicationField() {
//        List<String> fields = Arrays.asList(USER_NAME, PASSWORD, SERVER_NAME, INCOMING_FIELD);
//        return getFirebaseField(fields);

        return null;
    }

    private Firebase getOutgoingCommunicationField() {
//        List<String> fields = Arrays.asList(USER_NAME, PASSWORD, SERVER_NAME, OUTGOING_FIELD);
//        return getFirebaseField(fields);

        return null;
    }

    private Firebase getFirebaseField(List<String> fieldPathArgs) {
        String METHOD_NAME = ".getFirebaseField()";

        Firebase communicationField = new Firebase(FIREBASE_URL);

        for (String fieldPath : fieldPathArgs)
            communicationField = communicationField.child(fieldPath);

        return communicationField;
    }


    @Override
    public void onAccountSet(Account account) {
        String METHOD_NAME = ".onAccountSet()";
        System.out.println(CLASS_NAME + METHOD_NAME);

        if (account.isEmpty())
            System.out.println(CLASS_NAME + METHOD_NAME + "->ACCOUNT_IS_EMPTY");

        List<String> baseFieldsArgs = new ArrayList<>();
        baseFieldsArgs.add(account.getUserName());
        baseFieldsArgs.add(account.getPassword());
        baseFieldsArgs.add(account.getServerName());

        incomingFieldArgs = new ArrayList<>(baseFieldsArgs);
        incomingFieldArgs.add(INCOMING_FIELD);

        outgoingFieldArgs = new ArrayList<>(baseFieldsArgs);
        outgoingFieldArgs.add(OUTGOING_FIELD);

        statusFieldArgs = new ArrayList<>(baseFieldsArgs);
        statusFieldArgs.add(STATUS_FIELD);

        accountSet = true;
    }
}





//    public void setUser(String userName, String password, String machineName) {
//        if (firebaseEventListener != null)
//            getIncomingCommunicationField().removeEventListener(firebaseEventListener);
//
//        USER_NAME = userName;
//        PASSWORD = password;
//        SERVER_NAME = machineName;
//
//        getIncomingCommunicationField().addValueEventListener(firebaseEventListener);
//    }
//    public void setUser(String userName, String password) {
//        if (firebaseEventListener != null)
//            firebaseDatabase
//                    .child(USER_NAME)
//                    .child(PASSWORD)
//                    .child(SERVER_NAME)
//                    .child(INCOMING_FIELD)
//                    .removeEventListener(firebaseEventListener);
//
//        USER_NAME = userName;
//        PASSWORD = password;
//
//        firebaseDatabase
//                .child(USER_NAME)
//                .child(PASSWORD)
//                .child(SERVER_NAME)
//                .child(INCOMING_FIELD)
//                .addValueEventListener(firebaseEventListener);
//    }
