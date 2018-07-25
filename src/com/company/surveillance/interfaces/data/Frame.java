package com.company.surveillance.interfaces.data;

import com.company.surveillancedata.data_calsses.ObjectId;
import javafx.scene.image.Image;

/**
 * Created by Alexander on 03.07.2018.
 */
public interface Frame {
    void setImage(Image image);
    Image getImage();

    void setTimeStamp(long timeStamp);
    long getTimeStamp();

    void setId(ObjectId id);
    ObjectId getId();
}
