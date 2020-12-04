package toombs.animaltracker;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import toombs.animaltracker.animals.Animal;
import toombs.animaltracker.animals.AnimalUtil;
import toombs.animaltracker.wrappers.WrapperUtil;
import toombs.animaltracker.wrappers.infoClasses.PictureInfo;

public class SearchPage extends AppCompatActivity {
    private ArrayList<AnimalItem> mAnimalList;

    private RecyclerView mRecyclerView;
    private AnimalAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("My Animals");
        setSupportActionBar(toolbar);

        inflateView();
    }

    private void inflateView() {
        mAnimalList = new ArrayList<>();

        HashSet<String> animalSet = AnimalUtil.loadAnimalSet(SearchPage.this, AnimalUtil.animalSetPath);

        if (animalSet != null) {
            for (String s : animalSet) {
                Animal animal = AnimalUtil.loadAnimal(getApplicationContext(), s);
                mAnimalList.add(new AnimalItem(((PictureInfo) WrapperUtil.loadPictureWrapper(SearchPage.this,
                        animal.getAnimalUUID() + WrapperUtil.picPathDirName, animal.getPictureUUID()).getResource()).getPicture(),
                        animal.getPetName(), animal.getCommonName(), animal.getAnimalUUID()));
            }
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new AnimalAdapter(mAnimalList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new AnimalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(SearchPage.this, AnimalPage.class);
                intent.putExtra("ANIMAL_UUID", mAnimalList.get(position).getAnimalUUID());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        inflateView();
    }
}