package toombs.animaltracker;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.time.zone.ZoneRulesException;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.logging.Logger;

import toombs.animaltracker.animals.Animal;
import toombs.animaltracker.animals.AnimalUtil;
import toombs.animaltracker.wrappers.Wrapper;
import toombs.animaltracker.wrappers.LogInfoWrapper;
import toombs.animaltracker.wrappers.WrapperUtil;
import toombs.animaltracker.wrappers.infoClasses.LogInfo;

import static toombs.animaltracker.animals.AnimalUtil.ObjectToByteArray;
import static toombs.animaltracker.animals.AnimalUtil.deserializeAnimal;
import static toombs.animaltracker.animals.AnimalUtil.deserializeAnimalSet;
import static toombs.animaltracker.animals.AnimalUtil.hashObject;
import static toombs.animaltracker.animals.AnimalUtil.serializeObject;

public class DummyPage extends AppCompatActivity {
    public static final String animalSet = "animalSet.ser";
    static int g = 0;
    SharedPreferences prefs = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //My very ugly testing of the animal class
        prefs = getSharedPreferences("animalPreferences", MODE_PRIVATE); // creates animalPreferences file and has parameter firstRun to check if app has ran before
        if(prefs.getBoolean("firstRun", false)){
        }else{
            HashSet<String> animalListSet = new HashSet<String>();
            Animal A = new Animal("Dingus", "N/A", "Zeus", new GregorianCalendar(2020, 9, 5, 5, 5), "M", true, null);
            String animalFileName = AnimalUtil.createFileName(A);
            animalListSet.add(animalFileName);
            File destination = new File(getFilesDir(), "/AnimalData");
            destination.mkdir();
            String h = hashObject(ObjectToByteArray(A));
            A.setSha1(h);
            serializeObject(getApplicationContext(), A, animalFileName);
            serializeObject(getApplicationContext(), animalListSet, animalSet);
            HashSet<String> deserializeHashSet = deserializeAnimalSet(getApplicationContext());
            deserializeAnimal(getApplicationContext(), animalFileName);
            Log.i("HashValueAnimalHASH", h);
            Log.i("HashValueAnimalGET", A.getSha1());
            Log.i("HashValueSet", hashObject((ObjectToByteArray(animalListSet))));
            Log.i("HashSet", deserializeHashSet.toString());
            prefs.edit().putBoolean("firstRun", false).apply();
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            //NOTE THAT THIS ONLY CONFIRMS THAT THE INFO LOADS AND SAVES.
            @Override
            public void onClick(View view) { //TODO this is just a test of functionality. du
                WrapperUtil.insertLogInfo(getApplicationContext(),"DAISY-" + WrapperUtil.logPathDirName,new LogInfoWrapper(DummyPage.g, Wrapper.WRAPPER_START_SENTINEL,Wrapper.WRAPPER_END_SENTINEL,
                        new LogInfo(new GregorianCalendar(),"THIS IS THE LOG MESSAGE")));
                LogInfoWrapper wrapper = (LogInfoWrapper) WrapperUtil.loadLogInfoWrapper(getApplicationContext(),"DAISY-"+ WrapperUtil.logPathDirName,DummyPage.g);
                Logger.getAnonymousLogger().info("THIS IS THE ID OF THE PREV AND THE NEXT, RESPECTIVELY: " +wrapper.getPrevID() + ", " + wrapper.getNextID());
                DummyPage.g++;
            }
        }); //TODO REFACTOR THIS >:)

    }

    @Override
    protected void onResume() {
        super.onResume();


    }
}