package com.company.surveillance.helpers;

import java.io.*;
import java.util.Base64;

/**
 * Created by Alexander on 16.06.2018.
 */
public class SerializationHelper {
    public static String objectToString(Object object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (oos == null)
            return null;

        try {
            oos.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    public static Object objectFromString(String dataString) {
        Object object = null;
        byte[] dataBytes;
        try {
            dataBytes = Base64.getDecoder().decode(dataString);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }

        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(dataBytes));
            object = (Object) ois.readObject();
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (object == null) {
            System.out.println("SerializationHelper->MESSAGE_IS_NULL");
            return null;
        }

        return object;
    }
}
