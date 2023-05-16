package com.example.planto;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planto.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Plantune_Activity extends AppCompatActivity implements View.OnClickListener {
    final static String URL = "https://perenual.com/api/species-list?&key=sk-tSG4645bc67775b7f873";
    final static String Edible = "&edible=1";
    final static String Poisonous = "&poisonous=1&6";
    final static String indoor = "&indoor=1";
    final static String pages = "&page=1";

    EditText edPlantName;
    Button btnSubmit;
    Spinner filterSpinner;

    String plantName = null;
    Boolean searchIsOn = false;

    RecyclerView plantRecyclerView;
    PlantAdapter plantAdapter;
    TextView textView_no_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantune);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edPlantName = findViewById(R.id.plant_name_et);
        btnSubmit = findViewById(R.id.submit_btn);
        textView_no_result = findViewById(R.id.no_results_tv);
        filterSpinner = findViewById(R.id.filter_spinner);

        btnSubmit.setOnClickListener(this);

        if (isNetworkAvailable()) {
            try {
                queryData(URL);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }

        // Initialize the RecyclerView and its adapter
        plantRecyclerView = findViewById(R.id.plant_recycler_view);
        plantAdapter = new PlantAdapter();
        plantRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        plantRecyclerView.setAdapter(plantAdapter);

        //TODO make pages for this dammit recycle_view

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String filter = (String) parent.getItemAtPosition(position);
                if(filter.equals("Edible")){
                    try {
                        spinnerChoiceProcessing(URL+Edible);
                        Toast.makeText(getApplicationContext(), filter, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if (filter.equals("Poisonous")){
                    try {
                        spinnerChoiceProcessing(URL+Poisonous);
                        Toast.makeText(getApplicationContext(), filter, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if (filter.equals("Indoor")){
                    try {
                        spinnerChoiceProcessing(URL+indoor);
                        Toast.makeText(getApplicationContext(), filter, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if (filter.equals("Indoor + Edible")){
                    try {
                        spinnerChoiceProcessing(URL+Edible+indoor);
                        Toast.makeText(getApplicationContext(), filter, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if (filter.equals("Indoor + Poisonous")){
                    try {
                        spinnerChoiceProcessing(URL+Poisonous+indoor);
                        Toast.makeText(getApplicationContext(), filter, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    public void spinnerChoiceProcessing(String urlSpinner) throws IOException {

        if (isNetworkAvailable()) {
            try {
                queryData(urlSpinner);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.submit_btn){
            plantName= edPlantName.getText().toString();
            if(!plantName.equals("")){
                searchIsOn = true;
                // Clear the data set of the adapter
                plantAdapter.getData().clear();

                // Notify the adapter that the data set has changed
                plantAdapter.notifyDataSetChanged();

                try {
                    queryData(URL);
                } catch (IOException e) {
                    e.printStackTrace();
                }}

        }
    }

    public void queryData(String urlString) throws IOException {
        URL url = new URL(urlString);
        new DataTask().execute(url);
    }

    public class DataTask extends AsyncTask<URL,Void,String> {
        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String data= null;
            try {
                data = NetworkUtils.getDatafromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            if(searchIsOn){setPlant_search_Data(s);searchIsOn = false;}else{
                setPlantData(s);}
        }

        public void setPlantData(String data){
            JSONObject myObject=null;
            try {
                myObject = new JSONObject(data);
                JSONArray plantArray = myObject.getJSONArray("data");
                List<String> plantIds = new ArrayList<>();
                List<Plant> plantList = new ArrayList<>();
                for (int i=0; i<plantArray.length();i++) {
                    JSONObject plantObject = plantArray.getJSONObject(i);
                    String plantId = plantObject.getString("id");
                    String scientificName = plantObject.getJSONArray("scientific_name").getString(0);

                    String commonName = plantObject.get("common_name").toString();

                    JSONArray otherNameArray = plantObject.getJSONArray("other_name");
                    StringBuilder otherNames = new StringBuilder();
                    for (int j = 0; j < otherNameArray.length(); j++) {
                        otherNames.append(otherNameArray.getString(j));
                        if (j != otherNameArray.length() - 1) {
                            otherNames.append(", ");
                        }
                    }

                    String cycle = plantObject.get("cycle").toString();

                    String watering = plantObject.get("watering").toString();

                    JSONArray sunlightArray = plantObject.getJSONArray("sunlight");
                    StringBuilder sunlight = new StringBuilder();
                    for (int j = 0; j < sunlightArray.length(); j++) {
                        sunlight.append(sunlightArray.getString(j));
                        if (j != sunlightArray.length() - 1) {
                            sunlight.append(", ");
                        }
                    }
                    String sunlightString = sunlight.toString();
                    Log.e("fuck me", sunlightString);

                    JSONArray Other_namesArray = plantObject.getJSONArray("other_name");
                    StringBuilder Other_names = new StringBuilder();
                    for (int j = 0; j < Other_namesArray.length(); j++) {
                        Other_names.append(Other_namesArray.getString(j));
                        if (j != Other_namesArray.length() - 1) {
                            Other_names.append(", ");
                        }
                    }
                    String Other_namesString = Other_names.toString();
                    if(Other_namesString==""){Other_namesString="none";}

                    Log.e("fuck me", Other_namesString);


                    JSONObject defaultImage = plantObject.getJSONObject("default_image");
                    String imageUrl = defaultImage.getString("regular_url");

                    // Add the plant to the list that will be displayed in the RecyclerView
                    plantList.add(new Plant(commonName, scientificName, imageUrl, sunlightString, Other_namesString, cycle, watering));
                    plantIds.add(plantId);

                    // Update the RecyclerView adapter with the new plant data
                    plantAdapter.setData(plantList, plantIds);
                }} catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void setPlant_search_Data(String data) {
            JSONObject myObject=null;
            try {
                myObject = new JSONObject(data);
                JSONArray plantArray = myObject.getJSONArray("data");
                List<String> plantIds = new ArrayList<>();
                List<Plant> plantList = new ArrayList<>();
                for (int i=0; i<plantArray.length();i++) {
                    JSONObject plantObject = plantArray.getJSONObject(i);
                    String plantId = plantObject.getString("id");
                    String plantCommonName = plantObject.get("common_name").toString();
                    Log.d("adApi",plantCommonName);
                    Log.d("TextPlantName","plantName");
                    if (plantCommonName.toLowerCase(Locale.ROOT).contains(plantName.toLowerCase(Locale.ROOT))) {
                        String scientificName = plantObject.getJSONArray("scientific_name").getString(0);

                        String commonName = plantObject.get("common_name").toString();

                        JSONArray otherNameArray = plantObject.getJSONArray("other_name");
                        StringBuilder otherNames = new StringBuilder();
                        for (int j = 0; j < otherNameArray.length(); j++) {
                            otherNames.append(otherNameArray.getString(j));
                            if (j != otherNameArray.length() - 1) {
                                otherNames.append(", ");
                            }
                        }

                        String cycle = plantObject.get("cycle").toString();

                        String watering = plantObject.get("watering").toString();

                        JSONArray sunlightArray = plantObject.getJSONArray("sunlight");
                        StringBuilder sunlight = new StringBuilder();
                        for (int j = 0; j < sunlightArray.length(); j++) {
                            sunlight.append(sunlightArray.getString(j));
                            if (j != sunlightArray.length() - 1) {
                                sunlight.append(", ");
                            }
                        }
                        String sunlightString = sunlight.toString();

                        JSONArray Other_namesArray = plantObject.getJSONArray("other_name");
                        StringBuilder Other_names = new StringBuilder();
                        for (int j = 0; j < Other_namesArray.length(); j++) {
                            Other_names.append(Other_namesArray.getString(j));
                            if (j != Other_namesArray.length() - 1) {
                                Other_names.append(", ");
                            }
                        }
                        String Other_namesString = Other_names.toString();
                        if(Other_namesString==""){Other_namesString="none";}
                        Log.e("fuck me", Other_namesString);


                        JSONObject defaultImage = plantObject.getJSONObject("default_image");
                        String imageUrl = defaultImage.getString("regular_url");

                        // Add the plant to the list that will be displayed in the RecyclerView
                        plantList.add(new Plant(commonName, scientificName, imageUrl, sunlightString, Other_namesString, cycle, watering));
                        plantIds.add(plantId);
                        plantAdapter.setData(plantList, plantIds);
                        plantAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        textView_no_result.setText(plantName + " not found");                    }
                }} catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.feedback:
                Toast.makeText(getApplicationContext(), "Coming soon",
                        Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
        return true;
    }
}