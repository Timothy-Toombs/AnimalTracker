package toombs.animaltracker;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.util.GregorianCalendar;
import java.util.HashSet;

import lombok.SneakyThrows;
import toombs.animaltracker.animals.Animal;
import toombs.animaltracker.animals.AnimalUtil;
import toombs.animaltracker.wrappers.LogInfoWrapper;
import toombs.animaltracker.wrappers.PictureWrapper;
import toombs.animaltracker.wrappers.WeightWrapper;
import toombs.animaltracker.wrappers.Wrapper;
import toombs.animaltracker.wrappers.WrapperUtil;
import toombs.animaltracker.wrappers.infoClasses.LogInfo;
import toombs.animaltracker.wrappers.infoClasses.PictureInfo;
import toombs.animaltracker.wrappers.infoClasses.WeightInfo;

public class AnimalPage extends AppCompatActivity {
    private ImageView animalPic;
    private TextView petName, commonName, scientificName, animalAge, animalWeight, animalLog, animalSex;
    private Animal animal;
    private String animalUUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Animal's Page");
        setSupportActionBar(toolbar);

        animalPic = findViewById(R.id.animal_page_pic);
        petName = findViewById(R.id.animal_page_pet_name);
        commonName = findViewById(R.id.animal_page_common_name);
        scientificName = findViewById(R.id.animal_page_scientific_name);
        animalAge = findViewById(R.id.animal_page_age);
        animalSex = findViewById(R.id.animal_page_sex);
        animalWeight = findViewById(R.id.animal_page_weight);
        animalLog = findViewById(R.id.animal_page_log);
        animalLog.setText(R.string.animal_page_log_entry);

        Intent intent = getIntent();
        animalUUID = intent.getStringExtra("ANIMAL_UUID");
        animal = AnimalUtil.loadAnimal(AnimalPage.this, animalUUID);

        displayAnimal();

        petName.setText(animal.getPetName());
        commonName.setText(animal.getCommonName());
        scientificName.setText(animal.getScientificName());
        animalSex.setText(animal.getSex());

        animalPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnimalPage.this, AnimalPicturesPage.class);
                intent.putExtra("ANIMAL_UUID", animalUUID);
                intent.putExtra("PICTURE_UID", animal.getPictureUUID());
                startActivity(intent);
            }
        });

        animalWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnimalPage.this, AnimalWeightsPage.class);
                intent.putExtra("ANIMAL_UUID", animalUUID);
                startActivity(intent);
            }
        });

        animalLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnimalPage.this, AnimalLogsPage.class);
                intent.putExtra("ANIMAL_UUID", animalUUID);
                startActivity(intent);
            }
        });

        invalidateOptionsMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayAnimal();
    }

    private void displayAnimal() {
        animal = AnimalUtil.loadAnimal(AnimalPage.this, animalUUID);

        PictureWrapper pictureWrapper = WrapperUtil.loadPictureWrapper(AnimalPage.this, animal.getAnimalUUID() +
                WrapperUtil.picPathDirName, animal.getPictureUUID());
        byte[] mAnimalPic = ((PictureInfo) pictureWrapper.getResource()).getPicture();
        Bitmap bitmap = BitmapFactory.decodeByteArray(mAnimalPic, 0, mAnimalPic.length);
        animalPic.setImageBitmap(bitmap);

        LogInfoWrapper logInfoWrapper = WrapperUtil.loadLogInfoWrapper(AnimalPage.this, animal.getAnimalUUID() +
                WrapperUtil.logPathDirName, animal.getLogInfoUUID());
        if (logInfoWrapper == null)
            animalLog.setText(R.string.animal_page_log_entry);
        else {
            String calendar = DateUtil.logMsgInfoDateToString(((LogInfo) logInfoWrapper.getResource()).getInfoDate());
            String log = ((LogInfo) logInfoWrapper.getResource()).getLogMsg();
            animalLog.setText(getApplicationContext().getString(R.string.animal_log_message, calendar, log));
        }

        WeightWrapper weightWrapper = WrapperUtil.loadWeightWrapper(AnimalPage.this, animal.getAnimalUUID()+
                WrapperUtil.weightPathDirName, animal.getWeightUUID());
        double weightNumeric = ((WeightInfo) weightWrapper.getResource()).getWeight();
        String weightUnit = ((WeightInfo) weightWrapper.getResource()).getUnit();
        animalWeight.setText(getApplicationContext().getString(R.string.animal_page_weight, weightNumeric, weightUnit));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.archive_animal);
        if (!animal.isArchived())
            item.setTitle(R.string.archive_animal);
        else
            item.setTitle(R.string.unarchive_animal);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_new_animal_log:
                return true;
            case R.id.add_new_animal_pic:
                return true;
            case R.id.archive_animal:
                animal.setArchived(!animal.isArchived());
                AnimalUtil.updateAnimal(AnimalPage.this, animal);
                if (!animal.isArchived()) {
                    item.setTitle(R.string.archive_animal);
                    Toast.makeText(AnimalPage.this, animal.getPetName() + " is no longer archived",
                            Toast.LENGTH_SHORT).show();
                } else {
                    item.setTitle(R.string.unarchive_animal);
                    Toast.makeText(AnimalPage.this, animal.getPetName() + " is archived",
                            Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.delete_animal:
                deleteAnimal();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteAnimal() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AnimalPage.this);
        builder.setTitle("Delete Animal");
        builder.setMessage("Are you sure you want to delete this animal?");
        builder.setPositiveButton("Yes", null);
        builder.setNegativeButton("No", null);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean logUUID, picUUID, weightUUID;
                logUUID = picUUID = weightUUID = true;

                while (logUUID || picUUID || weightUUID) {
                    if (animal.getLogInfoUUID() != Wrapper.WRAPPER_END_SENTINEL) {
                        LogInfoWrapper logInfoWrapper = WrapperUtil.loadLogInfoWrapper(AnimalPage.this,
                                animal.getAnimalUUID() + WrapperUtil.logPathDirName, animal.getLogInfoUUID());
                        animal.setLogInfoUUID(logInfoWrapper.getNextID());
                        WrapperUtil.removeLogInfo(AnimalPage.this, animal.getAnimalUUID() + WrapperUtil.logPathDirName,
                                logInfoWrapper.getUID());
                    } else
                        logUUID = false;
                    if (animal.getPictureUUID() != Wrapper.WRAPPER_END_SENTINEL) {
                        PictureWrapper pictureWrapper = WrapperUtil.loadPictureWrapper(AnimalPage.this,
                                animal.getAnimalUUID() + WrapperUtil.picPathDirName, animal.getPictureUUID());
                        animal.setPictureUUID(pictureWrapper.getNextID());
                        WrapperUtil.removePicInfo(AnimalPage.this, animal.getAnimalUUID() + WrapperUtil.picPathDirName,
                                pictureWrapper.getUID());
                    } else
                        picUUID = false;
                    if (animal.getWeightUUID() != Wrapper.WRAPPER_END_SENTINEL) {
                        WeightWrapper weightWrapper = WrapperUtil.loadWeightWrapper(AnimalPage.this,
                                animal.getAnimalUUID() + WrapperUtil.weightPathDirName, animal.getWeightUUID());
                        animal.setWeightUUID(weightWrapper.getNextID());
                        WrapperUtil.removeWeightInfo(AnimalPage.this, animal.getAnimalUUID() + WrapperUtil.weightPathDirName,
                                weightWrapper.getUID());
                    } else
                        weightUUID = false;
                }

                AnimalUtil.removeAnimal(AnimalPage.this, animal);
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();
    }
}