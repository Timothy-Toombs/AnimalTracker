package toombs.animaltracker.wrappers;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import toombs.animaltracker.wrappers.Wrapper;
import toombs.animaltracker.wrappers.infoClasses.LogInfo;
import toombs.animaltracker.wrappers.infoClasses.PictureInfo;
import toombs.animaltracker.wrappers.infoClasses.WeightInfo;

public class WrapperUtil {
    //TODO use getFilesDir
    //File directory = new File(Environment.getExternalStorageDirectory() + "/images");
    //directory.mkdirs();
    //use to uhhhh :))))
    public static final String logPathDirName = "-LOGS_";
    public static final String picPathDirName = "-PICS_";
    public static final String weightPathDirName = "-WEIGHTS_";

    static void pairWrappers(Wrapper first, Wrapper second) {

        first.setNextID(second.getUID());
        second.setPrevID(first.getUID());
    }

    //TODO implement
    private static LogInfoWrapper initializeLogInfoLinkedList(Context context, String pathName) {
        //CHECK PATHNAME MAKE SURE IT ENDS WITH LOGS
        if (!checkLogPath(pathName)) ;
        // If already initalized in path, return current start Sentinel.
        LogInfoWrapper startSentinel = loadLogInfoWrapper(context, pathName,
                Wrapper.WRAPPER_START_SENTINEL);
        if (startSentinel != null)
            return startSentinel;
        //Initialize start node, Initialize EndNode
        //Connect the two
        startSentinel = new LogInfoWrapper(Wrapper.WRAPPER_START_SENTINEL,
                Wrapper.WRAPPER_START_SENTINEL, Wrapper.WRAPPER_END_SENTINEL, new LogInfo(null, null));
        LogInfoWrapper endSentinel = new LogInfoWrapper(Wrapper.WRAPPER_END_SENTINEL,
                Wrapper.WRAPPER_START_SENTINEL, Wrapper.WRAPPER_END_SENTINEL, new LogInfo(null, null));
        //Serialize both
        serializeWrapper(context, pathName, startSentinel);
        serializeWrapper(context, pathName, endSentinel);
        //Return start Node.
        return startSentinel;
    }

    //TODO implement
    private static PictureWrapper initializePicInfoLinkedList(Context context, String pathName) {
        //CHECK PATHNAME MAKE SURE IT ENDS WITH PICS
        if (!checkPicPath(pathName)) ;
        // If already initalized in path, return current start Sentinel.
        PictureWrapper startSentinel = loadPictureWrapper(context, pathName,
                Wrapper.WRAPPER_START_SENTINEL);
        if (startSentinel != null)
            return startSentinel;
        //Initialize start node, Initialize EndNode
        //Connect the two
        startSentinel = new PictureWrapper(Wrapper.WRAPPER_START_SENTINEL,
                Wrapper.WRAPPER_START_SENTINEL, Wrapper.WRAPPER_END_SENTINEL, new PictureInfo(null, null));
        PictureWrapper endSentinel = new PictureWrapper(Wrapper.WRAPPER_END_SENTINEL,
                Wrapper.WRAPPER_START_SENTINEL, Wrapper.WRAPPER_END_SENTINEL, new PictureInfo(null, null));
        //Serialize both
        serializeWrapper(context, pathName, startSentinel);
        serializeWrapper(context, pathName, endSentinel);
        //Return start Node.
        return startSentinel;
    }

    //TODO implement
    private static WeightWrapper initializeWeightInfoLinkedList(Context context, String pathName) {
        //CHECK PATHNAME MAKE SURE IT ENDS WITH WEIGHTS
        if (!checkWeightPath(pathName)) ;
        // If already initalized in path, return current start Sentinel.
        WeightWrapper startSentinel = loadWeightWrapper(context, pathName,
                Wrapper.WRAPPER_START_SENTINEL);
        if (startSentinel != null)
            return startSentinel;
        //Initialize start node, Initialize EndNode
        //Connect the two
        startSentinel = new WeightWrapper(Wrapper.WRAPPER_START_SENTINEL,
                Wrapper.WRAPPER_START_SENTINEL, Wrapper.WRAPPER_END_SENTINEL, new WeightInfo(null, 0, 0));
        WeightWrapper endSentinel = new WeightWrapper(Wrapper.WRAPPER_END_SENTINEL,
                Wrapper.WRAPPER_START_SENTINEL, Wrapper.WRAPPER_END_SENTINEL, new WeightInfo(null, 0, 0));
        //Serialize both
        serializeWrapper(context, pathName, startSentinel);
        serializeWrapper(context, pathName, endSentinel);
        //Return start Node.
        return startSentinel;
    }


    private static void insertGeneric(Context context, String pathName, Wrapper wrapper) {
        Wrapper startNode = loadWrapper(context, pathName, Wrapper.WRAPPER_START_SENTINEL);
        Wrapper firstNode = loadWrapper(context, pathName, startNode.getNextID());

        startNode.setNextID(wrapper.getUID());
        wrapper.setPrevID(startNode.getUID());

        firstNode.setPrevID(wrapper.getUID());
        wrapper.setNextID(firstNode.getUID());

        serializeWrapper(context, pathName, startNode);
        serializeWrapper(context, pathName, wrapper);
        serializeWrapper(context, pathName, firstNode);
    }

    public static void insertWeightInfo(Context context, String pathName, WeightWrapper wrapper) {
        //CHECK pathName make sure it ends with WEIGHTS
        if (!checkWeightPath(pathName)) {
            return;
        }
        if (loadWeightWrapper(context, pathName, Wrapper.WRAPPER_START_SENTINEL) == null)
            initializeWeightInfoLinkedList(context, pathName);
        insertGeneric(context, pathName, wrapper);


    }

    public static void insertLogInfo(Context context, String pathName, LogInfoWrapper wrapper) {
        if (!checkLogPath(pathName)) {
            return;
        }
        if (loadLogInfoWrapper(context, pathName, Wrapper.WRAPPER_START_SENTINEL) == null)
            initializeLogInfoLinkedList(context, pathName);
        insertGeneric(context, pathName, wrapper);


    }

    public static void insertPictureInfo(Context context, String pathName, PictureWrapper wrapper) {
        if (!checkPicPath(pathName)) {
            return;
        }
        if (loadPictureWrapper(context, pathName, Wrapper.WRAPPER_START_SENTINEL) == null)
            initializePicInfoLinkedList(context, pathName);
        insertGeneric(context, pathName, wrapper);
    }

    //TODO implement
    public static void removeWeightInfo(Context context, String pathName, long UID) {
        //CHECK PATHNAME MAKE SURE IT ENDS WITH WEIGHTS
        if (checkWeightPath(pathName)) {
            Wrapper currNode = loadWeightWrapper(context, pathName, UID);
            Wrapper prevNode = loadWeightWrapper(context, pathName, currNode.getPrevID());
            Wrapper nextNode = loadWeightWrapper(context, pathName, currNode.getNextID());

            if (currNode.getNextID() == Wrapper.WRAPPER_END_SENTINEL && currNode.getPrevID() == Wrapper.WRAPPER_START_SENTINEL) {
                context.deleteFile(pathName + UID);
                context.deleteFile(pathName + Wrapper.WRAPPER_START_SENTINEL);
                context.deleteFile(pathName + Wrapper.WRAPPER_END_SENTINEL);
            } else {
                prevNode.setNextID(nextNode.getUID());
                nextNode.setPrevID(prevNode.getUID());

                context.deleteFile(pathName + UID);

                serializeWrapper(context, pathName, prevNode);
                serializeWrapper(context, pathName, nextNode);
            }
        }
    }

    //TODO implement
    public static void removePicInfo(Context context, String pathName, long UID) {
        if (checkPicPath(pathName)) {
            Wrapper currNode = loadPictureWrapper(context, pathName, UID);
            Wrapper prevNode = loadPictureWrapper(context, pathName, currNode.getPrevID());
            Wrapper nextNode = loadPictureWrapper(context, pathName, currNode.getNextID());

            if (currNode.getNextID() == Wrapper.WRAPPER_END_SENTINEL && currNode.getPrevID() == Wrapper.WRAPPER_START_SENTINEL) {
                context.deleteFile(pathName + UID);
                context.deleteFile(pathName + Wrapper.WRAPPER_START_SENTINEL);
                context.deleteFile(pathName + Wrapper.WRAPPER_END_SENTINEL);
            } else {
                prevNode.setNextID(nextNode.getUID());
                nextNode.setPrevID(prevNode.getUID());

                context.deleteFile(pathName + UID);

                serializeWrapper(context, pathName, prevNode);
                serializeWrapper(context, pathName, nextNode);
            }

        }
    }

    //TODO implement
    public static void removeLogInfo(Context context, String pathName, long UID) {
        //CHECK PATHNAME MAKE SURE IT ENDS WITH LOGS
        if (checkLogPath(pathName)) {
            Wrapper currNode = loadLogInfoWrapper(context, pathName, UID);
            Wrapper prevNode = loadLogInfoWrapper(context, pathName, currNode.getPrevID());
            Wrapper nextNode = loadLogInfoWrapper(context, pathName, currNode.getNextID());

            if (currNode.getNextID() == Wrapper.WRAPPER_END_SENTINEL && currNode.getPrevID() == Wrapper.WRAPPER_START_SENTINEL) {
                context.deleteFile(pathName + UID);
                context.deleteFile(pathName + Wrapper.WRAPPER_START_SENTINEL);
                context.deleteFile(pathName + Wrapper.WRAPPER_END_SENTINEL);
            } else {
                prevNode.setNextID(nextNode.getUID());
                nextNode.setPrevID(prevNode.getUID());

                context.deleteFile(pathName + UID);

                serializeWrapper(context, pathName, prevNode);
                serializeWrapper(context, pathName, nextNode);
            }
        }
    }

    private static String getDirName(String pathName) {
        Path logPath = Paths.get(pathName);
        return logPath.getFileName().toString();
    }


    private static boolean checkLogPath(String pathName) {
        String curDir = getDirName(pathName);
        return curDir.endsWith(logPathDirName);
    }


    private static boolean checkPicPath(String pathName) {
        String curDir = getDirName(pathName);
        return curDir.endsWith(picPathDirName);
    }

    private static boolean checkWeightPath(String pathName) {
        String curDir = getDirName(pathName);
        return curDir.endsWith(weightPathDirName);
    }


    static private void serializeWrapper(Context context, String pathName, Wrapper wrapper) {
        try {
            FileOutputStream fos = context.openFileOutput(pathName + wrapper.getUID(), Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(wrapper);
            os.close();
        } catch (FileNotFoundException e) {
            Log.e("error:FileNotFoundException", e.getMessage());
        } catch (IOException e) {
            Log.e("error:IOException", e.getMessage());
        }
    }

    private static Wrapper loadWrapper(Context context, String pathName, long UID) {
        Wrapper wrapper = null;
        try {
            FileInputStream fis = context.openFileInput(pathName + UID);
            ObjectInputStream is = new ObjectInputStream(fis);

            wrapper = (Wrapper) is.readObject();

            is.close();
            fis.close();
        } catch (FileNotFoundException e) {
            Log.e("error:FileNotFoundException", e.getMessage());
        } catch (IOException e) {
            Log.e("error:IOException", e.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("error:ClassNotFoundException", e.getMessage());
        }

        return wrapper;
    }

    public static LogInfoWrapper loadLogInfoWrapper(Context context, String pathName, long UID) {
        return (LogInfoWrapper) loadWrapper(context, pathName, UID);
    }

    public static PictureWrapper loadPictureWrapper(Context context, String pathName, long UID) {
        return (PictureWrapper) loadWrapper(context, pathName, UID);
    }

    public static WeightWrapper loadWeightWrapper(Context context, String pathName, long UID) {
        return (WeightWrapper) loadWrapper(context, pathName, UID);
    }
}
