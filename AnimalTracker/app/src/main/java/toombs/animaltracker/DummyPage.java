package toombs.animaltracker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

import toombs.animaltracker.wrappers.Animal;
import toombs.animaltracker.wrappers.Wrapper;
import toombs.animaltracker.wrappers.LogInfoWrapper;
import toombs.animaltracker.wrappers.WrapperUtil;
import toombs.animaltracker.wrappers.infoClasses.LogInfo;

public class DummyPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private ImageView animalPicture;
    private EditText petName, scientificName, commonName, weight, age;
    private Button add, cancel;
    private Spinner spinner;
    private static final int PICK_IMAGE = 1;
    static int g = 0;
    Uri imageUri;
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

        FloatingActionButton addAnimal = findViewById(R.id.addAnimal);
        addAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewAnimalDialog();
            }
        });
    }

    public void addNewAnimalDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View animalPopupView = getLayoutInflater().inflate(R.layout.popup, null);

        animalPicture = animalPopupView.findViewById(R.id.newanimalicon);
        animalPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_IMAGE);
            }
        });

        petName = animalPopupView.findViewById(R.id.petname);
        scientificName = animalPopupView.findViewById(R.id.scientificname);
        commonName = animalPopupView.findViewById(R.id.commonname);
        weight = animalPopupView.findViewById(R.id.weight);
        age = animalPopupView.findViewById(R.id.age);

        spinner = animalPopupView.findViewById(R.id.sexspinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sex, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        cancel = animalPopupView.findViewById(R.id.cancel);
        add = animalPopupView.findViewById(R.id.add);

        dialogBuilder.setView(animalPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //define add button here
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                animalPicture.setImageBitmap(bitmap);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    static private void serializeAnimal(Context context, String pathName, Animal animal)  {
        try {
            FileOutputStream fos = context.openFileOutput(pathName + , Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(wrapper);
            os.close();
        }catch (FileNotFoundException e) {
            Log.e("error:FileNotFoundException",e.getMessage());
        } catch (IOException e) {
            Log.e("error:IOException",e.getMessage());
        }
    }
}