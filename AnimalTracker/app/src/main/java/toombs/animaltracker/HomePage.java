package toombs.animaltracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

import toombs.animaltracker.animals.Animal;
import toombs.animaltracker.animals.AnimalUtil;
import toombs.animaltracker.wrappers.PictureWrapper;
import toombs.animaltracker.wrappers.Wrapper;
import toombs.animaltracker.wrappers.WrapperUtil;
import toombs.animaltracker.wrappers.infoClasses.PictureInfo;

public class HomePage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private ImageView animalPicture;
    private EditText petName, scientificName, commonName, age, weight;
    private Button add, cancel, search;
    private Spinner spinner;
    private static final int TAKE_IMAGE = 0;
    private static final int PICK_IMAGE = 1;
    private byte[] byteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        search = findViewById(R.id.searchButton);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCurrentAnimals();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            //NOTE THAT THIS ONLY CONFIRMS THAT THE INFO LOADS AND SAVES.
            @Override
            public void onClick(View view) { //TODO this is just a test of functionality. du
//                GregorianCalendar calendar = new GregorianCalendar();
//                calendar.set(Calendar.YEAR, 2020);
//                calendar.set(Calendar.MONTH, 11);
//                calendar.set(Calendar.DAY_OF_MONTH, 19);
//                Animal animal = new Animal("Dog", "Bernese Mountain Dog", "Charlie", calendar, "M", false);
//                AnimalUtil.insertAnimal(DummyPage.this, animal);
//                Animal animal1 = AnimalUtil.loadAnimal(DummyPage.this, animal.getAnimalUUID());
//                WrapperUtil.insertPictureInfo(getApplicationContext(), "DAISY" + WrapperUtil.picPathDirName, new PictureWrapper(0, Wrapper.WRAPPER_START_SENTINEL, Wrapper.WRAPPER_END_SENTINEL,
//                        new PictureInfo(new GregorianCalendar(), null)));
                PictureWrapper wrapper = WrapperUtil.loadPictureWrapper(getApplicationContext(), "Snickers" + WrapperUtil.picPathDirName, 0);
                Object object = wrapper.getResource();
                String log = ((PictureInfo) object).getPicture().toString();
                Logger.getAnonymousLogger().info("WRAPPER HAS A UID OF " + wrapper.getUID());
                //Logger.getAnonymousLogger().info("THIS ANIMAL'S LOG MSG IS: " + log);
                //Object object = wrapper.getResource();
                Logger.getAnonymousLogger().info("THIS IS THE PICTURE STRING " + log);
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

    private void openCurrentAnimals() {
        Intent intent = new Intent(this, SearchPage.class);
        startActivity(intent);
    }

    TextWatcher tw = new TextWatcher() {
        private String current = "";
        private String ddmmyyyy = "DDMMYYYY";
        private GregorianCalendar calendar = new GregorianCalendar();

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!s.toString().equals(current)) {
                String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                int cl = clean.length();
                int sel = cl;
                for (int i = 2; i <= cl && i < 6; i += 2) {
                    sel++;
                }

                if (clean.equals(cleanC)) sel--;

                if (clean.length() < 8) {
                    clean = clean + ddmmyyyy.substring(clean.length());
                } else {
                    int day = Integer.parseInt(clean.substring(0, 2));
                    int month = Integer.parseInt(clean.substring(2, 4));
                    int year = Integer.parseInt(clean.substring(4, 8));

                    month = month < 1 ? 1 : Math.min(month, 12);
                    calendar.set(Calendar.MONTH, month - 1);
                    year = (year < 1900) ? 1900 : Math.min(year, 2100);
                    calendar.set(Calendar.YEAR, year);

                    day = Math.min(day, calendar.getActualMaximum(Calendar.DATE));
                    //clean = String.format("%02d%02d%02d", day, month, year);
                }

                clean = String.format("%s%s%s", clean.substring(0, 2), clean.substring(2, 4),
                        clean.substring(4, 8));

                sel = Math.max(sel, 0);
                current = clean;
                age.setText(current);
                age.setSelection(Math.min(sel, current.length()));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void addNewAnimalDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View animalPopupView = getLayoutInflater().inflate(R.layout.popup, null);

        animalPicture = animalPopupView.findViewById(R.id.newAnimal_picture);
        animalPicture.setImageResource(R.drawable.ic_baseline_add_a_photo_24);
        animalPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animalImage();
            }
        });

        petName = animalPopupView.findViewById(R.id.newAnimal_petName);
        scientificName = animalPopupView.findViewById(R.id.newAnimal_scientificName);
        commonName = animalPopupView.findViewById(R.id.newAnimal_commonName);
        age = animalPopupView.findViewById(R.id.newAnimal_age);
        age.addTextChangedListener(tw);
        weight = animalPopupView.findViewById(R.id.newAnimal_weight);

        spinner = animalPopupView.findViewById(R.id.sexSpinner);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sex, android.R.layout.simple_spinner_item);
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
                Animal animal = new Animal(scientificName.getText().toString(), commonName.getText().toString(), petName.getText().toString(),
                        new GregorianCalendar(), spinner.getSelectedItem().toString(), false);
                AnimalUtil.insertAnimal(getApplicationContext(), animal);
                WrapperUtil.insertPictureInfo(getApplicationContext(), animal.getAnimalUUID() + WrapperUtil.picPathDirName,
                        new PictureWrapper(animal.getPictureUUID(), Wrapper.WRAPPER_START_SENTINEL, Wrapper.WRAPPER_END_SENTINEL,
                                new PictureInfo(new GregorianCalendar(), byteArray)));
                dialog.dismiss();
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

        Bitmap bitmap;
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    bitmap = (Bitmap) extras.get("data");
                    animalPicture.setImageBitmap(bitmap);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byteArray = stream.toByteArray();
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    try {
                        Uri imageUri = data.getData();
                        animalPicture.setImageURI(imageUri);
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byteArray = stream.toByteArray();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public void animalImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Add a picture");

        dialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals("Take Photo")) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, TAKE_IMAGE);
                } else if (options[which].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, PICK_IMAGE);
                } else if (options[which].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        dialogBuilder.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}