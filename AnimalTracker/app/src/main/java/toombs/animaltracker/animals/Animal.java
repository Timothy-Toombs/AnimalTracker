package toombs.animaltracker.animals;

import java.io.Serializable;
import java.util.GregorianCalendar;

import lombok.Getter;
import lombok.Setter;

public class Animal implements Serializable {

    /**
     * The animal's scientific name if provided
     */
   @Getter
   private String scientificName;
    /**
     * The animal's common name if provided, i.e. dog
     */
    @Getter
    private String commonName;
    /**
     * The animal's pet name.
     */
    @Getter
    private String petName;
    /**
     * The animal's date of birth
     */
    @Getter
    private GregorianCalendar dateOfBirth;
    /**
     * The animal's sex.
     */
    @Getter
    private String sex;
    /**
     * Whether or not an animal is archived or still in use.
     */
    @Getter
    private boolean archived;
    /**
     * Ids corresponding to the next available
     * UUID for an animal's wrappers (of each type, respectively).
     * These are expected to be updated whenever there is a new wrapper created for the animal.
     */
    @Getter @Setter
    private int WeightUUID;
    private int LogInfoUUID;
    private int PictureUUID;
    /**
     * @param scientificName The animal's scientific name if provided
     * @param commonName     The animal's common name if provided, i.e. dog
     * @param petName        The animal's pet name.
     * @param dateOfBirth    The animal's date of birth
     * @param sex            The animal's sex.
     * @param archived       whether or not an animal is archived or still in use.
     */
    public Animal(String scientificName, String commonName, String petName, GregorianCalendar dateOfBirth,
                  String sex, boolean archived) {
        this.WeightUUID = 0;
        this.LogInfoUUID = 0;
        this.PictureUUID = 0;
        this.scientificName = scientificName;
        this.commonName = commonName;
        this.petName = petName;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
        this.archived = archived;
    }




}
