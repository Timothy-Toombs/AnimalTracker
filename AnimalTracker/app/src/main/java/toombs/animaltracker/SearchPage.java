package toombs.animaltracker;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.Navigator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

import toombs.animaltracker.animals.Animal;
import toombs.animaltracker.animals.AnimalUtil;
import toombs.animaltracker.wrappers.WrapperUtil;
import toombs.animaltracker.wrappers.infoClasses.PictureInfo;

public class SearchPage extends AppCompatActivity {
    private ArrayList<AnimalItem> mAnimalList;
    private ArrayList<AnimalItem> nonArchivedAnimals;
    private ArrayList<AnimalItem> archivedAnimals;

    private RecyclerView mRecyclerView;
    private AnimalAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private boolean showArchived;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("My Animals");
        setSupportActionBar(toolbar);

        invalidateOptionsMenu();

        prepareView();
        showArchived = false;
        inflateView(nonArchivedAnimals);
    }

    private void prepareView() {
        nonArchivedAnimals = new ArrayList<>();
        archivedAnimals = new ArrayList<>();

        LinkedHashSet<String> animalSet = AnimalUtil.loadAnimalSet(SearchPage.this, AnimalUtil.animalSetPath);

        if (animalSet != null) {
            for (String s : animalSet) {
                Animal animal = AnimalUtil.loadAnimal(getApplicationContext(), s);
                if (!animal.isArchived())
                    nonArchivedAnimals.add(new AnimalItem(((PictureInfo) WrapperUtil.loadPictureWrapper(SearchPage.this,
                            animal.getAnimalUUID() + WrapperUtil.picPathDirName, animal.getPictureUUID()).getResource()).getPicture(),
                            animal.getPetName(), animal.getCommonName(), animal.getAnimalUUID()));
                else
                    archivedAnimals.add(new AnimalItem(((PictureInfo) WrapperUtil.loadPictureWrapper(SearchPage.this,
                            animal.getAnimalUUID() + WrapperUtil.picPathDirName, animal.getPictureUUID()).getResource()).getPicture(),
                            animal.getPetName(), animal.getCommonName(), animal.getAnimalUUID()));
            }
        }
    }

    private void inflateView(ArrayList<AnimalItem> animalList) {
        Collections.reverse(animalList);
        mAnimalList = new ArrayList<>(animalList);

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
    protected void onStop() {
        super.onStop();

        //getIntent().putExtra();
    }

    @Override
    protected void onResume() {
        super.onResume();

        showArchived = false;
        prepareView();
        inflateView(nonArchivedAnimals);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_page_options_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.show_archived_animals_option);
        if (!showArchived)
            item.setTitle("Show Archived Animals");
        else
            item.setTitle("Hide Archived Animals");
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_archived_animals_option:
                if (!showArchived) {
                    inflateView(archivedAnimals);
                    showArchived = true;
                }
                else {
                    inflateView(nonArchivedAnimals);
                    showArchived = false;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}