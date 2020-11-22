package toombs.animaltracker;

public class AnimalItem {
    private byte[] mImageResource;
    private String mText1;
    private String mText2;
    private String mAnimalUUID;

    public AnimalItem(byte[] imageResource, String text1, String text2, String animalUUID) {
        mImageResource = imageResource;
        mText1 = text1;
        mText2 = text2;
        mAnimalUUID = animalUUID;
    }

    public byte[] getImageResource() {
        return mImageResource;
    }

    public String getText1() {
        return mText1;
    }

    public String getText2() {
        return mText2;
    }

    public String getAnimalUUID() {return mAnimalUUID;}
}
