package toombs.animaltracker;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchPage extends AppCompatActivity {
    private ArrayList<AnimalItem> mAnimalList;

    private RecyclerView mRecyclerView;
    private AnimalAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        Bundle extras = getIntent().getExtras();
        byte[] byteArray = extras.getByteArray("PICTURE");

        mAnimalList = new ArrayList<>();
        mAnimalList.add(new AnimalItem(byteArray, "Line 1", "Line 2"));
        mAnimalList.add(new AnimalItem(byteArray, "Line 3", "Line 4"));
        mAnimalList.add(new AnimalItem(byteArray, "Line 5", "Line 6"));
        mAnimalList.add(new AnimalItem(byteArray, "Line 1", "Line 2"));
        mAnimalList.add(new AnimalItem(byteArray, "Line 3", "Line 4"));
        mAnimalList.add(new AnimalItem(byteArray, "Line 5", "Line 6"));
        mAnimalList.add(new AnimalItem(byteArray, "Line 1", "Line 2"));
        mAnimalList.add(new AnimalItem(byteArray, "Line 3", "Line 4"));
        mAnimalList.add(new AnimalItem(byteArray, "Line 5", "Line 6"));
        mAnimalList.add(new AnimalItem(byteArray, "Line 1", "Line 2"));
        mAnimalList.add(new AnimalItem(byteArray, "Line 3", "Line 4"));
        mAnimalList.add(new AnimalItem(byteArray, "Line 5", "Line 6"));

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new AnimalAdapter(mAnimalList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new AnimalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Bundle extras = new Bundle();
                extras.putByteArray("ANIMAL_PIC", mAnimalList.get(position).getImageResource());
                extras.putString("PET_NAME", mAnimalList.get(position).getText1());
                extras.putString("COMMON_NAME", mAnimalList.get(position).getText2());
                Intent intent = new Intent(SearchPage.this, AnimalPage.class);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
    }
}