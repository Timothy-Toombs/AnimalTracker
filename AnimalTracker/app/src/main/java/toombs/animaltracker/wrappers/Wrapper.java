package toombs.animaltracker.wrappers;

import java.io.Serializable;

/**
 * @author Tim Toombs
 * @version  0.1
 * @since 0.0
 *
 * A Wrapper class for resources that have an ordering to them that could potentially become too
 * large to load in their entirety. The Wrapper will create a linked list of individual serialized
 * resources.
 */
public abstract class Wrapper implements Serializable {
    /**
     * The unique identifier for a Wrapper and its corresponding resource.
     */
    final private long UID;

    /**
     * The value of the prevID for Wrappers at the start of a list.
     */
    static final public long WRAPPER_START_SENTINEL = -2;

    /**
     * The value of the nextID for Wrappers at the end of a list.
     */
    static final public long WRAPPER_END_SENTINEL = -1;
    /**
     * The unique identifier for the Wrapper that occurs earlier in the linked list. This value
     * defaults to WRAPPER_START_SENTINEL when the current Wrapper is the first element in the linked list.
     */
    private long prevID;

    /**
     * The unique identifier for the Wrapper that occurs later in the linked list. This value
     * defaults to WRAPPER_END_SENTINEL when the current Wrapper is the last element in the linked list.
     */
    private long nextID;

    /**
     * The Object being wrapped.
     */
    private Object resource;
    /**
     *
     * @param UID the unique identifier.
     * @param prevID the previous element's identifier.
     * @param nextID the next element's identifier.
     * @param resource the resource.
     */
    public Wrapper(long UID, long prevID, long nextID,Object resource) {
        this.UID = UID;
        this.prevID = prevID;
        this.nextID = nextID;
        this.resource = resource;
    }
    /*getters*/

    /**
     *
     * @return the nextID of the object or -1 if the object is last in the list.
     */
    public long getNextID() {
        return nextID;
    }

    /**
     *
     * @return the prevID of the object or -2 if the object is first in the list.
     */
    public long getPrevID() {
        return prevID;
    }

    /**
     *
     * @return the UID of the object.
     */
    public long getUID() {
        return UID;
    }

    /*setters*/

    /**
     *
     * @param nextID the new nextID.
     */
    public void setNextID(long nextID) {
        this.nextID = nextID;
    }

    /**
     *
     * @param prevID the new prevID.
     */
    public void setPrevID(long prevID) {
        this.prevID = prevID;
    }

    /**
     * Tells whether a Wrapper has no prevID and no nextID. This means the corresponding resource
     * can be deleted.
     * @return True if the Wrapper is has no previous or next resource.
     */
    public boolean isOrphaned() {
        return  (nextID == prevID) && prevID == -1L;
    }


}
