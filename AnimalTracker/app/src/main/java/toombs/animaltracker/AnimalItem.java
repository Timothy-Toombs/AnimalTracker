package toombs.animaltracker;

public class AnimalItem {
    private byte[] mImageResource;
    private String mText1;
    private String mText2;

    public AnimalItem(byte[] imageResource, String text1, String text2) {
        mImageResource = imageResource;
        mText1 = text1;
        mText2 = text2;
    }

    public void changeText1(String text) {
        mText1 = text;
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
}
