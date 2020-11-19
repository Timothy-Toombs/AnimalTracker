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


public class AnimalUtil {
    private static final String animalSetPath = "animalSet";

    public static void insertAnimal(Context context, Animal animal) {
        HashSet<String> animalSet = loadAnimalSet(context, animalSetPath);
        if (animalSet == null) {
            animalSet = new HashSet<>();
        }
        String hashName = hashedAnimalName(animalToByteArray(animal));
        animalSet.add(hashName);
        serializeObject(context, animalSetPath, animalSet);
        serializeObject(context, hashName, animal);
    }

    public static void removeAnimal(Context context, Animal animal) {
        HashSet<String> animalSet = loadAnimalSet(context, animalSetPath);
        String hashName = hashedAnimalName(animalToByteArray(animal));
        if (!animalSet.isEmpty()) {
            animalSet.remove(hashName);
        }
        context.deleteFile(hashName);
        serializeObject(context, animalSetPath, animalSet);
    }

    private static String hashedAnimalName(byte[] byteArray) {
        StringBuffer str = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(byteArray);
            BigInteger no = new BigInteger(1, messageDigest);
            String hashName = no.toString(16);
            str.append(hashName);
            while (str.length() < 32) {
                str.insert(0, "0");
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("error:NoSuchAlgorithmException",e.getMessage());
        }
        return str.toString();
    }

    private static byte[] animalToByteArray(Animal animal) {
        byte[] animalBytes = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(animal);
            os.flush();
            animalBytes = out.toByteArray();
            out.close();
        } catch (IOException e){
            Log.e("error:IOException",e.getMessage());
        }
        return animalBytes;
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
