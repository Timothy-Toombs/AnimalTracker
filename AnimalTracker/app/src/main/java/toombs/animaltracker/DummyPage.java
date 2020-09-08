package toombs.animaltracker;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import java.util.GregorianCalendar;
import java.util.logging.Logger;

import toombs.animaltracker.wrappers.Wrapper;
import toombs.animaltracker.wrappers.LogInfoWrapper;
import toombs.animaltracker.wrappers.WrapperUtil;
import toombs.animaltracker.wrappers.infoClasses.LogInfo;

public class DummyPage extends AppCompatActivity {
    static int g = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
}