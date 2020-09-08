package toombs.animaltracker.wrappers;

import android.util.AndroidException;
import android.util.Log;

import toombs.animaltracker.wrappers.infoClasses.PictureInfo;

public class PictureWrapper extends Wrapper {
    /**
     *
     * @param UID the unique identifier.
     * @param prevID the previous element's identifier.
     * @param nextID the next element's identifier.
     * @param resource the resource.
     */
    public PictureWrapper(long UID, long prevID, long nextID,Object resource) {
        super(UID,prevID,nextID,resource);
        if (!(resource instanceof PictureInfo)) {
            Log.e("Type error", "object of incorrect type was passed in to " +
                    "PictureWrapper contructor.");
            throw new Error("PictureWrapper object mismatch");
        }
    }
}
