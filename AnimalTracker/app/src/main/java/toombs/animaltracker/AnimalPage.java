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

public class AnimalPage extends AppCompatActivity {
    private ImageView animalPic;
    private TextView petName, commonName;
    private Animal animal;
    private byte[] byteArray;
    private static final int TAKE_IMAGE = 0;
    private static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Animal Page");
        setSupportActionBar(toolbar);

        animalPic = findViewById(R.id.animalPage_pic);
        petName = findViewById(R.id.animalPage_petName);
        commonName = findViewById(R.id.animalPage_commonName);

        Intent intent = getIntent();
        String animalUUID = intent.getStringExtra("ANIMAL_UUID");

        animal = AnimalUtil.loadAnimal(AnimalPage.this, animalUUID);
        byte[] mAnimalPic = ((PictureInfo) WrapperUtil.loadPictureWrapper(AnimalPage.this, animal.getAnimalUUID() +
                WrapperUtil.picPathDirName, animal.getPictureUUID()).getResource()).getPicture();
        String mPetName = animal.getPetName();
        String mCommonName = animal.getCommonName();

        Bitmap bitmap = BitmapFactory.decodeByteArray(mAnimalPic, 0, mAnimalPic.length);

        animalPic.setImageBitmap(bitmap);
        petName.setText(mPetName);
        commonName.setText(mCommonName);

        invalidateOptionsMenu();
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
                addAnimalLog();
                return true;
            case R.id.add_new_animal_pic:
                addAnimalPic();
                return true;
            case R.id.archive_animal:
                animal.setArchived(!animal.isArchived());
                AnimalUtil.updateAnimal(AnimalPage.this, animal);
                if (!animal.isArchived()) {
                    item.setTitle(R.string.archive_animal);
                    Toast.makeText(AnimalPage.this, animal.getPetName() + " is unarchived",
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

    private void addAnimalLog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(AnimalPage.this);
        builder.setTitle("Log");
        builder.setMessage("Enter Log Message");
        builder.setPositiveButton("Save", null);
        builder.setNegativeButton("Cancel", null);

        final EditText input = new EditText(AnimalPage.this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(layoutParams);
        builder.setView(input);


        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String log = input.getText().toString();
                if (log.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter a valid log message",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (animal.getLogInfoUUID() == -1) {
                        WrapperUtil.insertLogInfo(AnimalPage.this, animal.getAnimalUUID() +
                                WrapperUtil.logPathDirName, new LogInfoWrapper(animal.getLogInfoUUID() + 1,
                                Wrapper.WRAPPER_START_SENTINEL, Wrapper.WRAPPER_END_SENTINEL,
                                new LogInfo(new GregorianCalendar(), log)));
                    } else {
                        WrapperUtil.insertLogInfo(AnimalPage.this, animal.getAnimalUUID() +
                                WrapperUtil.logPathDirName, new LogInfoWrapper(animal.getLogInfoUUID() + 1,
                                Wrapper.WRAPPER_START_SENTINEL, animal.getLogInfoUUID(),
                                new LogInfo(new GregorianCalendar(), log)));
                    }
                    animal.setLogInfoUUID(animal.getLogInfoUUID() + 1);
                    AnimalUtil.updateAnimal(AnimalPage.this, animal);
                    dialog.dismiss();
                }
            }
        });
    }

    private void addAnimalPic() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AnimalPage.this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap bitmap = (Bitmap) extras.get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byteArray = stream.toByteArray();
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    try {
                        Uri imageUri = data.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byteArray = stream.toByteArray();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }

        if (byteArray != null) {
            if (animal.getPictureUUID() == -1) {
                WrapperUtil.insertPictureInfo(AnimalPage.this, animal.getAnimalUUID() +
                        WrapperUtil.picPathDirName, new PictureWrapper(animal.getPictureUUID() + 1,
                        Wrapper.WRAPPER_START_SENTINEL, Wrapper.WRAPPER_END_SENTINEL,
                        new PictureInfo(new GregorianCalendar(), byteArray)));
            } else {
                WrapperUtil.insertPictureInfo(AnimalPage.this, animal.getAnimalUUID() +
                        WrapperUtil.picPathDirName, new PictureWrapper(animal.getPictureUUID() + 1,
                        Wrapper.WRAPPER_START_SENTINEL, animal.getPictureUUID(),
                        new PictureInfo(new GregorianCalendar(), byteArray)));
            }
            animal.setPictureUUID(animal.getPictureUUID() + 1);
            AnimalUtil.updateAnimal(AnimalPage.this, animal);
            byteArray = null;
        }
    }
}