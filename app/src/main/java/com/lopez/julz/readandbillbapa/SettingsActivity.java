package com.lopez.julz.readandbillbapa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.room.Room;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lopez.julz.readandbillbapa.adapters.HomeMenuAdapter;
import com.lopez.julz.readandbillbapa.api.RequestPlaceHolder;
import com.lopez.julz.readandbillbapa.api.RetrofitBuilder;
import com.lopez.julz.readandbillbapa.dao.AppDatabase;
import com.lopez.julz.readandbillbapa.dao.Settings;
import com.lopez.julz.readandbillbapa.helpers.AlertHelpers;
import com.lopez.julz.readandbillbapa.helpers.ObjectHelpers;
import com.lopez.julz.readandbillbapa.objects.BAPA;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {

    public Toolbar toolbar;

    public Spinner serverSelect, townSelect, bapaSelect;
    public List<String> townCodes;

    public FloatingActionButton saveBtn;

    public AppDatabase db;
    public RetrofitBuilder retrofitBuilder;
    private RequestPlaceHolder requestPlaceHolder;
    public Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        db = Room.databaseBuilder(this, AppDatabase.class, ObjectHelpers.dbName()).fallbackToDestructiveMigration().build();

        toolbar = findViewById(R.id.settingsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        serverSelect = findViewById(R.id.serverSelect);
        townSelect = findViewById(R.id.townSelect);
        bapaSelect = findViewById(R.id.bapaSelect);
        saveBtn = findViewById(R.id.saveBtn);
        townCodes = new ArrayList<>();

        new FetchSettings().execute();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SaveSettings().execute(serverSelect.getSelectedItem().toString(), bapaSelect.getSelectedItem() != null ? bapaSelect.getSelectedItem().toString() : "");
                finish();
                Toast.makeText(SettingsActivity.this, "Settings saved!", Toast.LENGTH_SHORT).show();
            }
        });

        serverSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                        new SaveSettings().execute(serverSelect.getSelectedItem().toString(), bapaSelect.getSelectedItem() != null ? bapaSelect.getSelectedItem().toString() : "");
                getBapaList(townCodes.get(townSelect.getSelectedItemPosition()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        townSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getBapaList(townCodes.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void populateSpinners() {
        try {
            // add town codes
            townCodes.add("01");
            townCodes.add("02");
            townCodes.add("03");
            townCodes.add("04");
            townCodes.add("05");
            townCodes.add("06");
            townCodes.add("07");
            townCodes.add("08");
            townCodes.add("09");

            // servers
            List<String> servers = new ArrayList<>();
            servers.add("192.168.10.161");
            servers.add("192.168.100.10");
            servers.add("192.168.100.20");
            servers.add("192.168.100.30");
            servers.add("192.168.100.40");
            servers.add("192.168.100.50");
            servers.add("192.168.100.60");
            servers.add("192.168.100.70");
            servers.add("192.168.100.80");
            servers.add("192.168.100.90");
            servers.add("192.168.100.4");
            servers.add("192.168.100.1");
            servers.add("192.168.110.94");
            servers.add("203.177.135.179:8443");
            servers.add("203.177.135.180:11445");
            servers.add("192.168.0.100");
            servers.add("192.168.5.6");
            servers.add("192.168.0.111");
            ArrayAdapter serversAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, servers.toArray());
            serversAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            serverSelect.setAdapter(serversAdapter);
            if (settings != null) {
                String ip = settings.getDefaultServer();
                if (ip != null) {
                    int spinnerPosition = serversAdapter.getPosition(ip);
                    serverSelect.setSelection(spinnerPosition);
                }
            }

            //towns
            List<String> towns = new ArrayList<>();
            towns.add("Cadiz");
            towns.add("EB Magalona");
            towns.add("Manapla");
            towns.add("Victorias");
            towns.add("San Carlos");
            towns.add("Sagay");
            towns.add("Escalante");
            towns.add("Calatrava");
            towns.add("Toboso");
            ArrayAdapter townsAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, towns.toArray());
            townsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            townSelect.setAdapter(townsAdapter);

        } catch (Exception e) {
            Log.e("ERR_POP_SPINNRS", e.getMessage());
        }
    }

    public void getBapaList(String town) {
        try {
            if (requestPlaceHolder != null) {
                Call<List<BAPA>> bapaListCall = requestPlaceHolder.getBapaList(town);

                bapaListCall.enqueue(new Callback<List<BAPA>>() {
                    @Override
                    public void onResponse(Call<List<BAPA>> call, Response<List<BAPA>> response) {
                        if(response.isSuccessful()) {
                            List<BAPA> bapaList = response.body();
                            List<String> bapas = new ArrayList<>();
                            for (BAPA bapaName : bapaList) {
                                if (bapaName.getOrganizationParentAccount() != null && bapaName.getOrganizationParentAccount().length() > 0) {
                                    bapas.add(bapaName.getOrganizationParentAccount());
                                }
                            }

                            ArrayAdapter bapaAdapter = new ArrayAdapter(SettingsActivity.this, android.R.layout.simple_spinner_item, bapas.toArray());
                            bapaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            bapaSelect.setAdapter(bapaAdapter);
                            if (settings != null) {
                                String bapa = settings.getBAPAName();
                                if (bapa != null) {
                                    int spinnerPosition = bapaAdapter.getPosition(bapa);
                                    bapaSelect.setSelection(spinnerPosition);
                                }
                            }
                        } else {
                            Log.e("ERR_GET_BAPA_LIST", response.raw() + "\n" + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<BAPA>> call, Throwable t) {
                        Log.e("ERR_GET_BAPA_LIST", t.getMessage());
                    }
                });
            }
        }catch (Exception e) {
            Log.e("ERR_GET_BAPA_LIST", e.getMessage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public class SaveSettings extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            try {
                Settings settingsLoc = new Settings(strings[0], "", strings[1]); // 0 = server, 1 = bapa name
                db.settingsDao().insertAll(settingsLoc);
                settings = settingsLoc;
            } catch (Exception e) {
                Log.e("ERR_SV_SETTINGS", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

        }
    }

    public class FetchSettings extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                settings = db.settingsDao().getSettings();
            } catch (Exception e) {
                Log.e("ERR_FETCH_SETTINGS", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            populateSpinners();
            if (settings != null) {
                retrofitBuilder = new RetrofitBuilder(settings.getDefaultServer());
                requestPlaceHolder = retrofitBuilder.getRetrofit().create(RequestPlaceHolder.class);

                getBapaList("01");


            }
        }
    }
}