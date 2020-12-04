package toombs.animaltracker.animals;

import android.content.Context;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.UUID;


public class AnimalUtil {
    public static final String animalSetPath = "animalSet";

    public static void insertAnimal(Context context, Animal animal) {
        HashSet<String> animalSet = loadAnimalSet(context, animalSetPath);
        if (animalSet == null) {
            animalSet = new HashSet<>();
        }
        animal.setAnimalUUID(UUID.randomUUID().toString());
        animalSet.add(animal.getAnimalUUID());
        serializeObject(context, animalSetPath, animalSet);
        serializeObject(context, animal.getAnimalUUID(), animal);
    }

    public static void removeAnimal(Context context, Animal animal) {
        HashSet<String> animalSet = loadAnimalSet(context, animalSetPath);
        if (!animalSet.isEmpty()) {
            animalSet.remove(animal.getAnimalUUID());
        }
        context.deleteFile(animal.getAnimalUUID());
        serializeObject(context, animalSetPath, animalSet);
    }

    public static void updateAnimal(Context context, Animal animal) {
        serializeObject(context, animal.getAnimalUUID(), animal);
    }

    private static void serializeObject(Context context, String pathName, Object object) {
        try {
            FileOutputStream fos = context.openFileOutput(pathName, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(object);
            os.close();
        } catch (FileNotFoundException e) {
            Log.e("error:FileNotFoundException", e.getMessage());
        } catch (IOException e) {
            Log.e("error:IOException", e.getMessage());
        }
    }

    private static Object loadObject(Context context, String pathName) {
        Object object = null;
        try {
            FileInputStream fis = context.openFileInput(pathName);
            ObjectInputStream is = new ObjectInputStream(fis);

            object = is.readObject();

            is.close();
            fis.close();
        } catch (FileNotFoundException e) {
            Log.e("error:FileNotFoundException", e.getMessage());
        } catch (IOException e) {
            Log.e("error:IOException", e.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("error:ClassNotFoundException", e.getMessage());
        }

        return object;
    }

    public static HashSet<String> loadAnimalSet(Context context, String pathName) {
        return (HashSet<String>) loadObject(context, pathName);
    }

    public static Animal loadAnimal(Context context, String pathName) {
        return (Animal) loadObject(context, pathName);
    }
}
