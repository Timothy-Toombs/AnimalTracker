package toombs.animaltracker.wrappers;

import android.util.Log;

import toombs.animaltracker.wrappers.infoClasses.LogInfo;

public class LogInfoWrapper extends Wrapper {
    /**
     *
     * @param UID the unique identifier.
     * @param prevID the previous element's identifier.
     * @param nextID the next element's identifier.
     * @param resource the resource.
     */
    public LogInfoWrapper(long UID, long prevID, long nextID,Object resource) {
        super(UID,prevID,nextID,resource);
        if (!(resource instanceof LogInfo)) {
            Log.e("Type error", "object of incorrect type was passed in to " +
                    "LogInfoWrapper contructor.");
            throw new Error("LogInfoWrapper object mismatch");
        }
    }
}
