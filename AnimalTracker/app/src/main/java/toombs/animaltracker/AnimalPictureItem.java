package toombs.animaltracker;

import java.util.GregorianCalendar;

public class AnimalPictureItem {
    private byte[] imageResource;
    private GregorianCalendar infoDate;
    private long pictureWrapperUID;

    public AnimalPictureItem(byte[] imageResource, GregorianCalendar infoDate, long pictureWrapperUID) {
        this.imageResource = imageResource;
        this.infoDate = infoDate;
        this.pictureWrapperUID = pictureWrapperUID;
    }

    public byte[] getImageResource() {
        return imageResource;
    }

    public GregorianCalendar getInfoDate() {
        return infoDate;
    }

    public long getPictureWrapperUID() {
        return pictureWrapperUID;
    }
}
