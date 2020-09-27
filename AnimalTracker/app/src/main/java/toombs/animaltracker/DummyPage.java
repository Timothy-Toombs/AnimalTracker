package toombs.animaltracker;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.GregorianCalendar;
import java.util.logging.Logger;

import toombs.animaltracker.ui.AnimalTrackerButton;
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
        ScrollView scrl=new ScrollView(this);
        final LinearLayout ll=new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(100, 500, 100, 200);
        scrl.addView(ll);
        AnimalTrackerButton add_btn=new AnimalTrackerButton(this,getString(R.string.create_animal_string));
        add_btn.setOnClickListener(new View.OnClickListener() {
            //NOTE THAT THIS ONLY CONFIRMS THAT THE INFO LOADS AND SAVES.
            @Override
            public void onClick(View view) {
                WrapperUtil.insertLogInfo(getApplicationContext(),"DAISY-" + WrapperUtil.logPathDirName,new LogInfoWrapper(DummyPage.g, Wrapper.WRAPPER_START_SENTINEL,Wrapper.WRAPPER_END_SENTINEL,
                        new LogInfo(new GregorianCalendar(),"THIS IS THE LOG MESSAGE")));
                LogInfoWrapper wrapper = (LogInfoWrapper) WrapperUtil.loadLogInfoWrapper(getApplicationContext(),"DAISY-"+ WrapperUtil.logPathDirName,DummyPage.g);
                Logger.getAnonymousLogger().info("THIS IS THE ID OF THE PREV AND THE NEXT, RESPECTIVELY: " +wrapper.getPrevID() + ", " + wrapper.getNextID());
                DummyPage.g++;
            }
        });
        ll.addView(add_btn, layoutParams);


        this.setContentView(scrl);
    }
}