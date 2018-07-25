package com.company.surveillance.managers;

import com.company.surveillance.data.FirebaseResult;
import com.company.surveillance.helpers.SerializationHelper;
import com.company.surveillance.interfaces.data.PendingResult;
import com.company.surveillancedata.data_calsses.CommunicationMessage;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FirebaseManager {
    private String CLASS_NAME = "FirebaseManager";

    private volatile static FirebaseManager instance;
    private static Firebase firebaseDatabase;
    private static final String FIREBASE_URL = "https://surveillance-136a9.firebaseio.com/";

    private static String USER_NAME = "USER";
    private static String PASSWORD = "PASSWORD";
    private static String SERVER_NAME = "TEST_SERVER";
    private static final String INCOMING_FIELD = "INCOMING_FIELD";
    private static final String OUTGOING_FIELD = INCOMING_FIELD;

    private ValueEventListener firebaseEventListener;



    private FirebaseManager() {
        firebaseDatabase = new Firebase(FIREBASE_URL);

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
            public void onCancelled(FirebaseError firebaseError) {

            }
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

        Firebase field = getField(fieldPathArgs);
        field.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null || dataSnapshot.getValue() == null || dataSnapshot.getValue().toString().isEmpty())
                    pendingResult.setResultStatus(PendingResult.Result.BAD);
                else
                    pendingResult.setResultStatus(PendingResult.Result.OK);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return pendingResult;
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

    public void sendMessage(CommunicationMessage message) {
        String messageData = SerializationHelper.objectToString(message);
        setOutgoingData(messageData);
    }


    private void setOutgoingData(String data) {
        firebaseDatabase
                .child(USER_NAME)
                .child(PASSWORD)
                .child(SERVER_NAME)
                .child(OUTGOING_FIELD)
                .setValue(data);
    }

    private Firebase getIncomingCommunicationField() {
        List<String> fields = Arrays.asList(USER_NAME, PASSWORD, SERVER_NAME, INCOMING_FIELD);
        return getField(fields);
    }

    private Firebase getOutgoingCommunicationField() {
        List<String> fields = Arrays.asList(USER_NAME, PASSWORD, SERVER_NAME, OUTGOING_FIELD);
        return getField(fields);
    }

    private Firebase getField(List<String> fieldPathArgs) {
        String METHOD_NAME = ".getField()";

        Firebase communicationField = new Firebase(FIREBASE_URL);

        for (String fieldPath : fieldPathArgs)
            communicationField = communicationField.child(fieldPath);

        return communicationField;
    }
}


//public class FirebaseManager {
//    private volatile static FirebaseManager instance;
//    private static Firebase firebaseDatabase;
//    private static final String FIREBASE_URL = "https://surveillance-136a9.firebaseio.com/";
//    private static final String FIREBASE_FIELD = "TEST";
//
//
//    private FirebaseManager() {
//        firebaseDatabase = new Firebase(FIREBASE_URL);
//        firebaseDatabase.child(FIREBASE_FIELD).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot == null || dataSnapshot.getValue() == null) {
//                    System.out.println("NO_DATA");
//                    return;
//                }
//
//                CommunicationMessage message = (CommunicationMessage) SerializationHelper.objectFromString(dataSnapshot.getValue().toString());
//                if (message == null) {
//                    System.out.println("MESSAGE_IS_NULL");
//                    return;
//                }
//
////                switch (message.getCommandType()) {
////                    case START:
////                        System.out.println("COMMAND_START");
////                        break;
////
////                    case STOP:
////                        System.out.println("COMMAND_STOP");
////                        break;
////
////                    default:
////                        System.out.println("UNKNOWN_COMMAND");
////                }
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
//    }
//
//    public static FirebaseManager getInstance() {
//        if (instance == null) {
//            synchronized (FirebaseManager.class) {
//                if (instance == null)
//                    instance = new FirebaseManager();
//            }
//        }
//
//        return instance;
//    }
//
//    public void closeConnection() {
//        if (firebaseDatabase != null)
//            firebaseDatabase.goOffline();
//    }
//}

//public class FirebaseManager {
//    private volatile static FirebaseManager instance;
//    private static Firebase firebaseDatabase;
//    private static final String FIREBASE_URL = "https://surveillance-136a9.firebaseio.com/";
//    private static final String FIREBASE_FIELD = "TEST";
//
//
//    private FirebaseManager() {
//        firebaseDatabase = new Firebase(FIREBASE_URL);
//        firebaseDatabase.child(FIREBASE_FIELD).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot == null || dataSnapshot.getValue() == null) {
//                    System.out.println("NO_DATA");
//                    return;
//                }
//
//                CommunicationMessage message = (CommunicationMessage) SerializationHelper.objectFromString(dataSnapshot.getValue().toString());
//                if (message == null) {
//                    System.out.println("MESSAGE_IS_NULL");
//                    return;
//                }
//
//                switch (message.getCommandType()) {
//                    case START:
//                        System.out.println("COMMAND_START");
//                        break;
//
//                    case STOP:
//                        System.out.println("COMMAND_STOP");
//                        break;
//
//                    default:
//                        System.out.println("UNKNOWN_COMMAND");
//                }
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
//    }
//
//    public static FirebaseManager getInstance() {
//        if (instance == null) {
//            synchronized (FirebaseManager.class) {
//                if (instance == null)
//                    instance = new FirebaseManager();
//            }
//        }
//
//        return instance;
//    }
//
//    public void closeConnection() {
//        if (firebaseDatabase != null)
//            firebaseDatabase.goOffline();
//    }
//
//
//    public void sendMessage() {
//        firebaseDatabase.child(FIREBASE_FIELD).setValue("TEST_VALUE");
//    }
//
//
//    public void clearField() {
//        System.out.println("CLEARING_FIELD");
//
//        firebaseDatabase.child(FIREBASE_FIELD).setValue(null);
//    }
//
//
//    public void testFunction() {
//        long x = System.currentTimeMillis();
//        long y = System.currentTimeMillis();
//
//        System.out.println("TEST_FUNCTION-> " + (x + y));
//    }
//}
