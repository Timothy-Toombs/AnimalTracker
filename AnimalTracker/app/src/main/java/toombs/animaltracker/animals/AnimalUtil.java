package toombs.animaltracker.animals;

import android.content.Context;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Objects;

public class AnimalUtil {
    public static String animalFilePath = "/files/AnimalData/";
    public static String animalSetFilePath = "/files/AnimalData/animalSet.ser";
    public static void serializeObject(Context context, Object obj, String fileName){
        try {
            String s = context.getApplicationInfo().dataDir + animalFilePath;
            FileOutputStream fos = new FileOutputStream(new File(s , fileName));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            fos.close();
        }catch (IOException e){
            Log.i("Exception error:", Objects.requireNonNull(e.getMessage()));
        }
    }


    public static Animal deserializeAnimal(Context context, String fileName){
        try{
            String s = context.getApplicationInfo().dataDir + animalFilePath;
            FileInputStream fis = new FileInputStream(new File(s, fileName));
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object deserializeAnimal = ois.readObject();
            ois.close();
            return (Animal) deserializeAnimal;
        }catch(IOException | ClassNotFoundException e){
            Log.i("Exception error:", Objects.requireNonNull(e.getMessage()));
            return null;
        }

    }


    //Suppressed warning unchecked because Java doesn't like me returning an unchecked HashSet. We need to check if it's a String hashset.
    @SuppressWarnings(value = "unchecked")
    public static HashSet<String> deserializeAnimalSet(Context context){


        try{
            String s = context.getApplicationInfo().dataDir + animalSetFilePath;
            FileInputStream fis = new FileInputStream(s);
            //FileInputStream fis = new FileInputStream("animalSet.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object deserializeAnimalSet = ois.readObject();
            ois.close();

            return (HashSet<String>) deserializeAnimalSet;
        }catch(IOException | ClassNotFoundException e){
            Log.i("Exception error:", Objects.requireNonNull(e.getMessage()));
            return null;
        }
    }

    public static String hashObject(byte[] byteArray){

        try{
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(byteArray);
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while(hashtext.length() < 32){
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
    }

    public static byte[] ObjectToByteArray(Object obj){
        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(obj);
            return out.toByteArray();

        }catch(IOException e){
            Log.i("Exception Error:", e.getMessage());
            return null;
        }
    }


    public static String createFileName(Animal animal) {
        return animal.getPetName() + "_" + animal.getScientificName() + ".ser";
    }


}
