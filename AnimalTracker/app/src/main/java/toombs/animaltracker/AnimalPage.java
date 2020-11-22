package toombs.animaltracker;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import toombs.animaltracker.animals.Animal;
import toombs.animaltracker.animals.AnimalUtil;
import toombs.animaltracker.wrappers.WrapperUtil;
import toombs.animaltracker.wrappers.infoClasses.PictureInfo;

public class AnimalPage extends AppCompatActivity {
    private ImageView animalPic;
    private TextView petName, commonName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_page);

        animalPic = findViewById(R.id.animalPage_pic);
        petName =  findViewById(R.id.animalPage_petName);
        commonName = findViewById(R.id.animalPage_commonName);

        Intent intent = getIntent();
        String animalUUID = intent.getStringExtra("ANIMAL_UUID");

        Animal animal = AnimalUtil.loadAnimal(getApplicationContext(), animalUUID);
        byte[] mAnimalPic = ((PictureInfo) WrapperUtil.loadPictureWrapper(getApplicationContext(), animal.getPetName() +
                WrapperUtil.picPathDirName, animal.getPictureUUID()).getResource()).getPicture();
        String mPetName = animal.getPetName();
        String mCommonName = animal.getCommonName();

        Bitmap bitmap = BitmapFactory.decodeByteArray(mAnimalPic, 0, mAnimalPic.length);

        animalPic.setImageBitmap(bitmap);
        petName.setText(mPetName);
        commonName.setText(mCommonName);
    }
}