package toombs.animaltracker.wrappers.infoClasses;

import android.media.Image;

import java.util.GregorianCalendar;

public class PictureInfo extends  GenericInfo {
    /**
     * The image that is being saved.
     */
    private byte[] picture;

    /**
     *
     * @param infoDate the date corresponding to the message.
     * @param picture the image that is being saved.
     */
    public PictureInfo(GregorianCalendar infoDate, byte[] picture) {
        super(infoDate);
        this.picture = picture;
    }

    /**
     *
     * @return the image that is saved.
     */
    public byte[] getPicture() {
        return picture;
    }
}