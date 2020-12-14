package toombs.animaltracker;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.Navigator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.LinkedHashSet;

import toombs.animaltracker.animals.Animal;
import toombs.animaltracker.animals.AnimalUtil;
import toombs.animaltracker.wrappers.LogInfoWrapper;
import toombs.animaltracker.wrappers.Wrapper;
import toombs.animaltracker.wrappers.WrapperUtil;
import toombs.animaltracker.wrappers.infoClasses.LogInfo;
import toombs.animaltracker.wrappers.infoClasses.PictureInfo;

public class SearchPage extends AppCompatActivity {
    private ArrayList<AnimalItem> mAnimalList;
    private ArrayList<AnimalItem> nonArchivedAnimals;
    private ArrayList<AnimalItem> archivedAnimals;

    private RecyclerView mRecyclerView;
    private AnimalAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private boolean showArchived;
    private boolean searchedAnimal;

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
        searchedAnimal = false;
        inflateView(nonArchivedAnimals);
    }

    private void prepareView() {
        nonArchivedAnimals = new ArrayList<>();
        archivedAnimals = new ArrayList<>();

        LinkedHashSet<String> animalSet = AnimalUtil.loadAnimalSet(SearchPage.this, AnimalUtil.animalSetPath);

        if (animalSet != null) {
            for (String s : animalSet) {
                Animal animal = AnimalUtil.loadAnimal(getApplicationContext(), s);
                if (!animal.isArchived()) {
                    nonArchivedAnimals.add(new AnimalItem(((PictureInfo) WrapperUtil.loadPictureWrapper(SearchPage.this,
                            animal.getAnimalUUID() + WrapperUtil.picPathDirName, animal.getPictureUUID()).getResource()).getPicture(),
                            animal.getPetName(), animal.getCommonName(), animal.getAnimalUUID()));
                } else {
                    archivedAnimals.add(new AnimalItem(((PictureInfo) WrapperUtil.loadPictureWrapper(SearchPage.this,
                            animal.getAnimalUUID() + WrapperUtil.picPathDirName, animal.getPictureUUID()).getResource()).getPicture(),
                            animal.getPetName(), animal.getCommonName(), animal.getAnimalUUID()));
                }
            }
        }
        Collections.reverse(nonArchivedAnimals);
        Collections.reverse(archivedAnimals);
    }

    private void inflateView(ArrayList<AnimalItem> animalList) {
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
        searchedAnimal = false;
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
        MenuItem showArchivedItem = menu.findItem(R.id.show_archived_animals_option);
        MenuItem searchedItem = menu.findItem(R.id.search_animal_option);
        if (!showArchived)
            showArchivedItem.setTitle("Show Archived Animals");
        else
            showArchivedItem.setTitle("Hide Archived Animals");
        if (!searchedAnimal)
            searchedItem.setTitle("Search Animals");
        else
            searchedItem.setTitle("Clear Search");
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_archived_animals_option:
                if (!showArchived) {
                    inflateView(archivedAnimals);
                    showArchived = true;
                } else {
                    inflateView(nonArchivedAnimals);
                    showArchived = false;
                }
                searchedAnimal = false;
                return true;
            case R.id.search_animal_option:
                if (!searchedAnimal)
                    searchAnimals();
                else {
                    if (!showArchived)
                        inflateView(nonArchivedAnimals);
                    else
                        inflateView(archivedAnimals);
                    searchedAnimal = false;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void searchAnimals() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(SearchPage.this);
        builder.setTitle("Search Animal");
        builder.setPositiveButton("Search", null);
        builder.setNegativeButton("Cancel", null);

        final EditText input = new EditText(SearchPage.this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(layoutParams);
        builder.setView(input);


        builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
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
                String search = input.getText().toString();
                if (search.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter a valid search",
                            Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<AnimalItem> searchResult = new ArrayList<>();
                    if (!showArchived) {
                        if (!nonArchivedAnimals.isEmpty()) {
                            for (AnimalItem i : nonArchivedAnimals) {
                                if (i.getAnimalPetName().toLowerCase().contains(search.toLowerCase())) {
                                    searchResult.add(i);
                                }
                            }
                        }
                    } else {
                        if (!archivedAnimals.isEmpty()) {
                            for (AnimalItem i : archivedAnimals) {
                                if (i.getAnimalPetName().toLowerCase().contains(search.toLowerCase())) {
                                    searchResult.add(i);
                                }
                            }
                        }
                    }
                    searchedAnimal = true;
                    inflateView(searchResult);
                    dialog.dismiss();
                }
            }
        });
    }
}