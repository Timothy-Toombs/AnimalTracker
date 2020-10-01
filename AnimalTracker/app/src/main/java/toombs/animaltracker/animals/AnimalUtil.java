//package toombs.animaltracker.animals;
//
//import android.content.Context;
//import android.util.Log;
//
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//
//import toombs.animaltracker.wrappers.Wrapper;
//import toombs.animaltracker.wrappers.WrapperUtil;
//
//public class AnimalUtil {
//
//    static private void serializeAnimal(Context context, Animal animal)  {
//        try {
//            FileOutputStream fos = context.openFileOutput(animal.getPetName(), Context.MODE_PRIVATE);
//            ObjectOutputStream os = new ObjectOutputStream(fos);
//            os.writeObject(animal);
//            os.close();
//        }catch (FileNotFoundException e) {
//            Log.e("error:FileNotFoundException",e.getMessage());ls
//        } catch (IOException e) {
//            Log.e("error:IOException",e.getMessage());
//        }
//    }
//
//    private static  Animal loadAnimal(Context context, String pathName) {
//        Animal animal = null;
//        try {
//            FileInputStream fis = context.openFileInput(pathName);
//            ObjectInputStream is = new ObjectInputStream(fis);
//
//            wrapper = (Wrapper) is.readObject();
//
//            is.close();
//            fis.close();
//        }catch (FileNotFoundException e) {
//            Log.e("error:FileNotFoundException",e.getMessage());
//        } catch (IOException e) {
//            Log.e("error:IOException",e.getMessage());
//        } catch (ClassNotFoundException e) {
//            Log.e("error:ClassNotFoundException",e.getMessage());
//        }
//
//        return wrapper;
//    }
//}
