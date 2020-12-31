package toombs.animaltracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import toombs.animaltracker.animals.Animal;
import toombs.animaltracker.animals.AnimalUtil;
import toombs.animaltracker.wrappers.PictureWrapper;
import toombs.animaltracker.wrappers.Wrapper;
import toombs.animaltracker.wrappers.WrapperUtil;
import toombs.animaltracker.wrappers.infoClasses.PictureInfo;

public class AnimalPicturesPage extends AppCompatActivity {
    private ArrayList<AnimalPictureItem> animalPicturesList;

    private RecyclerView recyclerView;
    private AnimalPicturesAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private String animalUUID;
    private long pictureUID;
    private Animal animal;
    private static final int TAKE_IMAGE = 0;
    private static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_pictures_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Pictures");
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        animalUUID = intent.getStringExtra("ANIMAL_UUID");
        pictureUID = intent.getLongExtra("PICTURE_UID", 0L);
        animal = AnimalUtil.loadAnimal(AnimalPicturesPage.this, animalUUID);

        animalPicturesList = new ArrayList<>();

        while (pictureUID != -1) {
            PictureWrapper wrapper = WrapperUtil.loadPictureWrapper(AnimalPicturesPage.this,
                    animalUUID + WrapperUtil.picPathDirName, pictureUID);
            animalPicturesList.add(new AnimalPictureItem(((PictureInfo) wrapper.getResource()).getPicture(),
                    new GregorianCalendar(), pictureUID));
            pictureUID = wrapper.getNextID();
        }

        inflateView();
    }

    private void inflateView() {
        recyclerView = findViewById(R.id.animal_pictures_page_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new AnimalPicturesAdapter(animalPicturesList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new AnimalPicturesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                pictureOptions(position, animalPicturesList.get(position).getPictureWrapperUID());
            }
        });
    }

    private void pictureOptions(final int position, final long pictureUID) {
        final CharSequence[] options = {"View Photo", "Delete", "Cancel"};

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AnimalPicturesPage.this);

        dialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals("View Photo")) {

                } else if (options[which].equals("Delete")) {
                    deletePicture(position, pictureUID);
                } else if (options[which].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        dialogBuilder.show();
    }

    private void deletePicture(int position, long pictureUID) {
        if (animalPicturesList.size() != 1) {
            animalPicturesList.remove(position);
            adapter.notifyItemRemoved(position);
            if (pictureUID == animal.getPictureUUID()) {
                PictureWrapper wrapper = WrapperUtil.loadPictureWrapper(AnimalPicturesPage.this,
                        animalUUID + WrapperUtil.picPathDirName, pictureUID);
                animal.setPictureUUID(wrapper.getNextID());
                AnimalUtil.updateAnimal(AnimalPicturesPage.this, animal);
            }
            WrapperUtil.removePicInfo(AnimalPicturesPage.this, animalUUID +
                            WrapperUtil.picPathDirName, pictureUID);
        } else
            Toast.makeText(AnimalPicturesPage.this, "Must insert another picture " +
                    "before you can delete this one", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_animal_wrapper, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        addAnimalPic();
        return super.onOptionsItemSelected(item);
    }

    private void addAnimalPic() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AnimalPicturesPage.this);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        byte[] byteArray = null;

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
            if (pictureUID == -1) {
                WrapperUtil.insertPictureInfo(AnimalPicturesPage.this, animal.getAnimalUUID() +
                        WrapperUtil.picPathDirName, new PictureWrapper(animal.getPictureUUID() + 1,
                        Wrapper.WRAPPER_START_SENTINEL, Wrapper.WRAPPER_END_SENTINEL,
                        new PictureInfo(new GregorianCalendar(), byteArray)));
            } else {
                WrapperUtil.insertPictureInfo(AnimalPicturesPage.this, animal.getAnimalUUID() +
                        WrapperUtil.picPathDirName, new PictureWrapper(animal.getPictureUUID() + 1,
                        Wrapper.WRAPPER_START_SENTINEL, animal.getPictureUUID(),
                        new PictureInfo(new GregorianCalendar(), byteArray)));
            }
            animal.setPictureUUID(animal.getPictureUUID() + 1);
            AnimalUtil.updateAnimal(AnimalPicturesPage.this, animal);
            animalPicturesList.add(0, new AnimalPictureItem(byteArray, new GregorianCalendar(), animal.getPictureUUID()));
            adapter.notifyItemInserted(0);
        }
    }
}