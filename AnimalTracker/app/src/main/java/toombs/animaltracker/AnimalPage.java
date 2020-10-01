package toombs.animaltracker;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
        Bundle extras = intent.getExtras();
        byte[] mAnimalPic = extras.getByteArray("ANIMAL_PIC");
        String mPetName = extras.getString("PET_NAME");
        String mCommonName = extras.getString("COMMON_NAME");

        Bitmap bitmap = BitmapFactory.decodeByteArray(mAnimalPic, 0, mAnimalPic.length);

        animalPic.setImageBitmap(bitmap);
        petName.setText(mPetName);
        commonName.setText(mCommonName);
    }
}