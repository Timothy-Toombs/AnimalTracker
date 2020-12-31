package toombs.animaltracker;


import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import toombs.animaltracker.animals.Animal;
import toombs.animaltracker.animals.AnimalUtil;
import toombs.animaltracker.wrappers.LogInfoWrapper;
import toombs.animaltracker.wrappers.PictureWrapper;
import toombs.animaltracker.wrappers.Wrapper;
import toombs.animaltracker.wrappers.WrapperUtil;
import toombs.animaltracker.wrappers.infoClasses.LogInfo;

public class AnimalLogsPage extends AppCompatActivity {
    private ArrayList<AnimalLogItem> animalLogsList;

    private RecyclerView recyclerView;
    private AnimalLogsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private String animalUUID;
    private long logUID;
    private Animal animal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_logs_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Logs");
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        animalUUID = intent.getStringExtra("ANIMAL_UUID");
        animal = AnimalUtil.loadAnimal(AnimalLogsPage.this, animalUUID);

        animalLogsList = new ArrayList<>();

        LogInfoWrapper wrapper = WrapperUtil.loadLogInfoWrapper(AnimalLogsPage.this,
                animalUUID + WrapperUtil.logPathDirName, animal.getLogInfoUUID());
        if (wrapper != null) {
            logUID = animal.getLogInfoUUID();
            while (logUID != -1) {
                animalLogsList.add(new AnimalLogItem(((LogInfo) wrapper.getResource()).getLogMsg(),
                        DateUtil.setCurrentInfoDate(), logUID));
                logUID = wrapper.getNextID();
                wrapper = WrapperUtil.loadLogInfoWrapper(AnimalLogsPage.this,
                        animalUUID + WrapperUtil.logPathDirName, logUID);
            }
        }

        inflateView();
    }

    private void inflateView() {
        recyclerView = findViewById(R.id.animal_logs_page_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new AnimalLogsAdapter(animalLogsList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new AnimalLogsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                logOptions(position, animalLogsList.get(position).getLogWrapperUID());
            }
        });
    }

    private void logOptions(final int position, final long logWrapperUID) {
        final CharSequence[] options = {"Edit Log", "Delete Log", "Cancel"};

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AnimalLogsPage.this);

        dialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals("Edit Log")) {

                } else if (options[which].equals("Delete Log")) {
                    deleteLog(position, logWrapperUID);
                } else if (options[which].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        dialogBuilder.show();
    }

    private void deleteLog(int position, long logWrapperUID) {
        animalLogsList.remove(position);
        adapter.notifyItemRemoved(position);
        if (logWrapperUID == animal.getLogInfoUUID()) {
            LogInfoWrapper wrapper = WrapperUtil.loadLogInfoWrapper(AnimalLogsPage.this,
                    animalUUID + WrapperUtil.logPathDirName, logWrapperUID);
            animal.setLogInfoUUID(wrapper.getNextID());
            AnimalUtil.updateAnimal(AnimalLogsPage.this, animal);
        }
        WrapperUtil.removeLogInfo(AnimalLogsPage.this, animalUUID +
                WrapperUtil.logPathDirName, logWrapperUID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_animal_wrapper, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        addAnimalLog();
        return super.onOptionsItemSelected(item);
    }

    private void addAnimalLog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(AnimalLogsPage.this);
        builder.setTitle("Log");
        builder.setMessage("Enter Log Message");
        builder.setPositiveButton("Save", null);
        builder.setNegativeButton("Cancel", null);

        final EditText input = new EditText(AnimalLogsPage.this);
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
                        WrapperUtil.insertLogInfo(AnimalLogsPage.this, animal.getAnimalUUID() +
                                WrapperUtil.logPathDirName, new LogInfoWrapper(animal.getLogInfoUUID() + 1,
                                Wrapper.WRAPPER_START_SENTINEL, Wrapper.WRAPPER_END_SENTINEL,
                                new LogInfo(DateUtil.setCurrentInfoDate(), log)));
                    } else {
                        WrapperUtil.insertLogInfo(AnimalLogsPage.this, animal.getAnimalUUID() +
                                WrapperUtil.logPathDirName, new LogInfoWrapper(animal.getLogInfoUUID() + 1,
                                Wrapper.WRAPPER_START_SENTINEL, animal.getLogInfoUUID(),
                                new LogInfo(DateUtil.setCurrentInfoDate(), log)));
                    }
                    animal.setLogInfoUUID(animal.getLogInfoUUID() + 1);
                    AnimalUtil.updateAnimal(AnimalLogsPage.this, animal);
                    animalLogsList.add(0, new AnimalLogItem(log, DateUtil.setCurrentInfoDate(), animal.getLogInfoUUID()));
                    dialog.dismiss();
                    adapter.notifyItemInserted(0);
                }
            }
        });
    }
}