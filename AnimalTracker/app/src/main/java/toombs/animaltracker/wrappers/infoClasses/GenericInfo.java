package toombs.animaltracker.wrappers.infoClasses;

import java.io.Serializable;
import java.util.GregorianCalendar;

/**
 * @author  Tim Toombs
 * A class the wrap generic information. Associates a calendar date with the object. Is a superclass
 * of other **T** Info classes that have information associated with the date as well.
 */
public class GenericInfo implements Serializable {
    /**
     * The date to associated with a given piece of information.
     */
    private GregorianCalendar infoDate;

    /**
     * getter method
     * @return date associated with corresponding information.
     */
    public GregorianCalendar getInfoDate() {
        return infoDate;
    }

    /**
     *
     * @param infoDate The date to associated with a given piece of information.
     */
    public  GenericInfo(GregorianCalendar infoDate){
        this.infoDate = infoDate;
    }
}
