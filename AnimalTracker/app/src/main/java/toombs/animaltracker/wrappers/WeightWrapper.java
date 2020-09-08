package toombs.animaltracker.wrappers;

import android.util.Log;

import toombs.animaltracker.wrappers.infoClasses.WeightInfo;

public class WeightWrapper extends Wrapper {
    /**
     *
     * @param UID the unique identifier.
     * @param prevID the previous element's identifier.
     * @param nextID the next element's identifier.
     * @param resource the resource.
     */
    public WeightWrapper(long UID, long prevID, long nextID,Object resource) {
        super(UID,prevID,nextID,resource);
        if (!(resource instanceof WeightInfo)) {
            Log.e("Type error", "object of incorrect type was passed in to " +
                    "WeightWrapper contructor.");
            throw new Error("WeightWrapper object mismatch");
        }
    }
}
