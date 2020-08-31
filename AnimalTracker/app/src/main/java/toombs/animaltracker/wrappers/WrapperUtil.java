package toombs.animaltracker.wrappers;

import android.content.Context;
import android.provider.Contacts;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

import toombs.animaltracker.Wrappers.Wrapper;

public class WrapperUtil {
    //TODO use getFilesDir
    //File directory = new File(Environment.getExternalStorageDirectory() + "/images");
    //directory.mkdirs();
    //use to uhhhh :))))
    static final  String logPathDirName = "LOGS";
    static final String picPathDirName = "PICS";
    static final String weightPathDirName = "WEIGHTS";

    static void pairWrappers(toombs.animaltracker.Wrappers.Wrapper first, toombs.animaltracker.Wrappers.Wrapper second) {

        first.setNextID(second.getUID());
        second.setPrevID(first.getUID());
    }

    //TODO implement
    static LogInfoWrapper initializeLogInfoLinkedList(Context context, String pathName) {
        //CHECK PATHNAME MAKE SURE IT ENDS WITH LOGS
        if (!checkLogPath(pathName));
        // If already initalized in path, return current start Sentinel.
//        File file = context.getFileStreamPath(pathName + "/-2");
//        if (file.exists())
//            return loadLogInfoWrapper(context, pathName, Wrapper.WRAPPER_START_SENTINEL);
        if (loadLogInfoWrapper(context, pathName, Wrapper.WRAPPER_START_SENTINEL) != null)
            return loadLogInfoWrapper(context, pathName, Wrapper.WRAPPER_START_SENTINEL);
        //Initialize start node, Initialize EndNode
        //Connect the two
        LogInfoWrapper startSentinel = new LogInfoWrapper(Wrapper.WRAPPER_START_SENTINEL,
                Wrapper.WRAPPER_START_SENTINEL, Wrapper.WRAPPER_END_SENTINEL, null);
        LogInfoWrapper endSentinel = new LogInfoWrapper(Wrapper.WRAPPER_END_SENTINEL,
                Wrapper.WRAPPER_START_SENTINEL, Wrapper.WRAPPER_END_SENTINEL, null);
        //Serialize both
        serializeWrapper(context, pathName, startSentinel);
        serializeWrapper(context, pathName, endSentinel);
        //Return start Node.
        return startSentinel;
    }
    //TODO implement
    static PictureWrapper initializePicInfoLinkedList(Context context,String pathName) {
        //CHECK PATHNAME MAKE SURE IT ENDS WITH PICS
        if (!checkPicPath(pathName));
        // If already initalized in path, return current start Sentinel.
//        File file = context.getFileStreamPath(pathName + "/-2");
//        if (file.exists())
//            return loadPictureWrapper(context, pathName, Wrapper.WRAPPER_START_SENTINEL);
        if (loadPictureWrapper(context, pathName, Wrapper.WRAPPER_START_SENTINEL) != null)
            return loadPictureWrapper(context, pathName, Wrapper.WRAPPER_START_SENTINEL);
        //Initialize start node, Initialize EndNode
        //Connect the two
        PictureWrapper startSentinel = new PictureWrapper(Wrapper.WRAPPER_START_SENTINEL,
                Wrapper.WRAPPER_START_SENTINEL, Wrapper.WRAPPER_END_SENTINEL, null);
        PictureWrapper endSentinel = new PictureWrapper(Wrapper.WRAPPER_END_SENTINEL,
                Wrapper.WRAPPER_START_SENTINEL, Wrapper.WRAPPER_END_SENTINEL, null);
        //Serialize both
        serializeWrapper(context, pathName, startSentinel);
        serializeWrapper(context, pathName, endSentinel);
        //Return start Node.
        return startSentinel;
    }
    //TODO implement
    static WeightWrapper initializeWeightInfoLinkedList(Context context,String pathName) {
        //CHECK PATHNAME MAKE SURE IT ENDS WITH WEIGHTS
        if (!checkWeightPath(pathName));
        // If already initalized in path, return current start Sentinel.
//        File file = context.getFileStreamPath(pathName + "/-2");
//        if (file.exists())
//            return loadWeightWrapper(context, pathName, Wrapper.WRAPPER_START_SENTINEL);
        if (loadWeightWrapper(context, pathName, Wrapper.WRAPPER_START_SENTINEL) != null)
            return loadWeightWrapper(context, pathName, Wrapper.WRAPPER_START_SENTINEL);
        //Initialize start node, Initialize EndNode
        //Connect the two
        WeightWrapper startSentinel = new WeightWrapper(Wrapper.WRAPPER_START_SENTINEL,
                Wrapper.WRAPPER_START_SENTINEL, Wrapper.WRAPPER_END_SENTINEL, null);
        WeightWrapper endSentinel = new WeightWrapper(Wrapper.WRAPPER_END_SENTINEL,
                Wrapper.WRAPPER_START_SENTINEL, Wrapper.WRAPPER_END_SENTINEL, null);
        //Serialize both
        serializeWrapper(context, pathName, startSentinel);
        serializeWrapper(context, pathName, endSentinel);
        //Return start Node.
        return startSentinel;
    }


    static void insertGeneric(Context context,String pathName,Wrapper wrapper) {
        Wrapper startNode = loadWeightWrapper(context, pathName, Wrapper.WRAPPER_START_SENTINEL);
        Wrapper firstNode = loadWeightWrapper(context,pathName,startNode.getNextID());

        startNode.setNextID(wrapper.getUID());
        wrapper.setPrevID(startNode.getUID());

        firstNode.setPrevID(wrapper.getUID());
        wrapper.setNextID(firstNode.getUID());

        serializeWrapper(context,pathName,startNode);
        serializeWrapper(context,pathName,wrapper);
        serializeWrapper(context,pathName,firstNode);
    }

    static void insertWeightInfo(Context context,String pathName, WeightWrapper wrapper){
        //CHECK pathName make sure it ends with WEIGHTS
        if (!checkWeightPath(pathName)){
            return;
        }
        if (loadWeightWrapper(context, pathName, Wrapper.WRAPPER_START_SENTINEL) == null)
            initializeWeightInfoLinkedList(context, pathName);
        insertGeneric(context,pathName,wrapper);


    }

    static void insertLogInfo(Context context,String pathName, LogInfoWrapper wrapper){
        if (!checkLogPath(pathName)){
            return;
        }
        if (loadLogInfoWrapper(context, pathName, Wrapper.WRAPPER_START_SENTINEL) == null)
            initializeLogInfoLinkedList(context, pathName);
        insertGeneric(context,pathName,wrapper);


    }

    static void insertPictureInfo(Context context,String pathName, PictureWrapper wrapper){
        if (!checkPicPath(pathName)){
            return;
        }
        if (loadPictureWrapper(context, pathName, Wrapper.WRAPPER_START_SENTINEL) == null)
            initializePicInfoLinkedList(context, pathName);
        insertGeneric(context,pathName,wrapper);
    }

    //TODO implement
    static  void removeWeightInfo(Context context,String pathName, int UID) {
        //CHECK PATHNAME MAKE SURE IT ENDS WITH WEIGHTS
        if (checkWeightPath(pathName)) {
            Wrapper currNode = loadWeightWrapper(context, pathName, UID);
            Wrapper prevNode = loadWeightWrapper(context, pathName, currNode.getPrevID());
            Wrapper nextNode = loadWeightWrapper(context, pathName, currNode.getNextID());

            prevNode.setNextID(nextNode.getUID());
            nextNode.setPrevID(prevNode.getUID());

            context.deleteFile(pathName + UID);
        }
    }

    //TODO implement
    static void removePicInfo(Context context,String pathName, int UID) {
        if (checkPicPath(pathName)) {
            Wrapper currNode = loadPictureWrapper(context, pathName, UID);
            Wrapper prevNode = loadPictureWrapper(context, pathName, currNode.getPrevID());
            Wrapper nextNode = loadPictureWrapper(context, pathName, currNode.getNextID());

            prevNode.setNextID(nextNode.getUID());
            nextNode.setPrevID(prevNode.getUID());

            context.deleteFile(pathName + UID);
        }
    }

    //TODO implement
    static void removeLogInfo(Context context,String pathName, int UID) {
        //CHECK PATHNAME MAKE SURE IT ENDS WITH LOGS
        if (checkLogPath(pathName)) {
            Wrapper currNode = loadLogInfoWrapper(context, pathName, UID);
            Wrapper prevNode = loadPictureWrapper(context, pathName, currNode.getPrevID());
            Wrapper nextNode = loadPictureWrapper(context, pathName, currNode.getNextID());

            prevNode.setNextID(nextNode.getUID());
            nextNode.setPrevID(prevNode.getUID());

            context.deleteFile(pathName + UID);
        }
    }

    static String getDirName(String pathName) {
        Path logPath = Paths.get(pathName);
        return logPath.getFileName().toString();
    }


    static boolean checkLogPath(String pathName) {
        String curDir = getDirName(pathName);
        return curDir.equals(logPathDirName);
    }


    static  boolean checkPicPath(String pathName) {
        String curDir = getDirName(pathName);
        return curDir.equals(picPathDirName);
    }

    static boolean checkWeightPath(String pathName) {
        String curDir = getDirName(pathName);
        return curDir.equals(weightPathDirName);
    }


    static private void serializeWrapper(Context context,String pathName, toombs.animaltracker.Wrappers.Wrapper wrapper)  {
        try {
            FileOutputStream fos = context.openFileOutput(pathName + wrapper.getUID(), Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(wrapper);
            os.close();
        }catch (FileNotFoundException e) {
            Log.e("error:FileNotFoundException",e.getMessage());
        } catch (IOException e) {
            Log.e("error:IOException",e.getMessage());
        }
    }

    static private Wrapper loadWrapper(Context context, String pathName, long UUID) {
        toombs.animaltracker.Wrappers.Wrapper wrapper= null;
        try {
            FileInputStream fis = context.openFileInput(pathName + UUID);
            ObjectInputStream is = new ObjectInputStream(fis);

            wrapper = (toombs.animaltracker.Wrappers.Wrapper) is.readObject();

            is.close();
            fis.close();
        }catch (FileNotFoundException e) {
            Log.e("error:FileNotFoundException",e.getMessage());
        } catch (IOException e) {
            Log.e("error:IOException",e.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("error:ClassNotFoundException",e.getMessage());
        }

       return wrapper;
    }
    static private LogInfoWrapper loadLogInfoWrapper(Context context,String pathName, long UUID) {
        return (LogInfoWrapper) loadWrapper(context,pathName,UUID);
    }
    static private PictureWrapper loadPictureWrapper(Context context,String pathName, long UUID) {
        return (PictureWrapper) loadWrapper(context,pathName,UUID);
    }
    static private WeightWrapper loadWeightWrapper(Context context, String pathName, long UUID) {
        return (WeightWrapper) loadWrapper(context,pathName,UUID);
    }
}
