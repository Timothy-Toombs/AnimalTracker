package toombs.animaltracker;

public class AnimalItem {
    private byte[] imageResource;
    private String animalPetName;
    private String animalCommonName;
    private String animalUUID;

    public AnimalItem(byte[] imageResource, String animalPetName, String animalCommonName, String animalUUID) {
        this.imageResource = imageResource;
        this.animalPetName = animalPetName;
        this.animalCommonName = animalCommonName;
        this.animalUUID = animalUUID;
    }

    public byte[] getImageResource() {
        return imageResource;
    }

    public String getAnimalPetName() {
        return animalPetName;
    }

    public String getAnimalCommonName() {
        return animalCommonName;
    }

    public String getAnimalUUID() { return animalUUID; }
}
