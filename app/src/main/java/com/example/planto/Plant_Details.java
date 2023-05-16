package com.example.planto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.planto.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Plant_Details extends AppCompatActivity {

    TextView idTv, commonNameTv, scientificNameTv, otherNameTv, familyTv, originTv, typeTv, dimensionTv, cycleTv, attractsTv,
            propagationTv, hardinessTv, hardinessLocationTv, wateringTv, sunlightTv, maintenanceTv, careGuidesTv, soilTv,
            growthRateTv, droughtTolerantTv, saltTolerantTv, thornyTv, invasiveTv, tropicalTv, indoorTv, careLevelTv,
            pestSusceptibilityTv, pestSusceptibilityApiTv, flowersTv, floweringSeasonTv, flowerColorTv, conesTv, fruitsTv,
            edibleFruitTv, edibleFruitTasteProfileTv, fruitNutritionalValueTv, fruitColorTv, harvestSeasonTv, harvestMethodTv,
            leafTv, leafColorTv, edibleLeafTv, edibleLeafTasteProfileTv, leafNutritionalValueTv, cuisineTv, cuisineListTv,
            medicinalTv, medicinalUseTv, medicinalMethodTv, poisonousToHumansTv, poisonEffectsToHumansTv,
            poisonToHumansCureTv, poisonousToPetsTv, poisonEffectsToPetsTv, poisonToPetsCureTv, rareTv, rareLevelTv,
            endangeredTv, endangeredLevelTv, descriptionTv, problemTv;
    ImageView defaultImageIv;

    private String ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        idTv = findViewById(R.id.id_tv);
        commonNameTv = findViewById(R.id.common_name_tv);
        scientificNameTv = findViewById(R.id.scientific_name_tv);
        otherNameTv = findViewById(R.id.other_name_tv);
        familyTv = findViewById(R.id.family_tv);
        originTv = findViewById(R.id.origin_tv);
        typeTv = findViewById(R.id.type_tv);
        dimensionTv = findViewById(R.id.dimension_tv);
        cycleTv = findViewById(R.id.cycle_tv);
        attractsTv = findViewById(R.id.attracts_tv);
        propagationTv = findViewById(R.id.propagation_tv);
        hardinessTv = findViewById(R.id.hardiness_tv);
        hardinessLocationTv = findViewById(R.id.hardiness_location_tv);
        wateringTv = findViewById(R.id.watering_tv);
        sunlightTv = findViewById(R.id.sunlight_tv);
        maintenanceTv = findViewById(R.id.maintenance_tv);
        careGuidesTv = findViewById(R.id.care_guides_tv);
        soilTv = findViewById(R.id.soil_tv);
        growthRateTv = findViewById(R.id.growth_rate_tv);
        droughtTolerantTv = findViewById(R.id.drought_tolerant_tv);
        saltTolerantTv = findViewById(R.id.salt_tolerant_tv);
        thornyTv = findViewById(R.id.thorny_tv);
        invasiveTv = findViewById(R.id.invasive_tv);
        tropicalTv = findViewById(R.id.tropical_tv);
        indoorTv = findViewById(R.id.indoor_tv);
        careLevelTv = findViewById(R.id.care_level_tv);
        pestSusceptibilityTv = findViewById(R.id.pest_susceptibility_tv);
        pestSusceptibilityApiTv = findViewById(R.id.pest_susceptibility_api_tv);
        flowersTv = findViewById(R.id.flowers_tv);
        floweringSeasonTv = findViewById(R.id.flowering_season_tv);
        flowerColorTv = findViewById(R.id.flower_color_tv);
        conesTv = findViewById(R.id.cones_tv);
        fruitsTv = findViewById(R.id.fruits_tv);
        edibleFruitTv = findViewById(R.id.edible_fruit_tv);
        edibleFruitTasteProfileTv = findViewById(R.id.edible_fruit_taste_profile_tv);
        fruitNutritionalValueTv = findViewById(R.id.fruit_nutritional_value_tv);
        fruitColorTv = findViewById(R.id.fruit_color_tv);
        harvestSeasonTv = findViewById(R.id.harvest_season_tv);
        harvestMethodTv = findViewById(R.id.harvest_method_tv);
        leafTv = findViewById(R.id.leaf_tv);
        leafColorTv = findViewById(R.id.leaf_color_tv);
        edibleLeafTv = findViewById(R.id.edible_leaf_tv);
        edibleLeafTasteProfileTv = findViewById(R.id.edible_leaf_taste_profile_tv);
        leafNutritionalValueTv = findViewById(R.id.leaf_nutritional_value_tv);
        cuisineTv = findViewById(R.id.cuisine_tv);
        cuisineListTv = findViewById(R.id.cuisine_list_tv);
        medicinalTv = findViewById(R.id.medicinal_tv);
        medicinalUseTv = findViewById(R.id.medicinal_use_tv);
        medicinalMethodTv = findViewById(R.id.medicinal_method_tv);
        poisonousToHumansTv = findViewById(R.id.poisonous_to_humans_tv);
        poisonEffectsToHumansTv = findViewById(R.id.poison_effects_to_humans_tv);
        poisonToHumansCureTv = findViewById(R.id.poison_to_humans_cure_tv);
        poisonousToPetsTv = findViewById(R.id.poisonous_to_pets_tv);
        poisonEffectsToPetsTv = findViewById(R.id.poison_effects_to_pets_tv);
        poisonToPetsCureTv = findViewById(R.id.poison_to_pets_cure_tv);
        rareTv = findViewById(R.id.rare_tv);
        rareLevelTv = findViewById(R.id.rare_level_tv);
        endangeredTv = findViewById(R.id.endangered_tv);
        endangeredLevelTv = findViewById(R.id.endangered_level_tv);
        descriptionTv = findViewById(R.id.description_tv);
        problemTv = findViewById(R.id.problem_tv);

        defaultImageIv = findViewById(R.id.default_image_iv);


        Intent intent = getIntent();
        if (intent != null) {
            ID = intent.getStringExtra("plantId");
        }
        if (isNetworkAvailable()) {
            try {
                Toast.makeText(getApplicationContext(), ID, Toast.LENGTH_LONG).show();
                queryData("https://perenual.com/api/species/details/" + ID + "?key=sk-tSG4645bc67775b7f873");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void queryData(String urlString) throws IOException {
        URL url = new URL(urlString);
        new DataTask().execute(url);
    }

    public class DataTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String data = null;
            try {
                data = NetworkUtils.getDatafromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            setPlantData(s);
        }

        public void setPlantData(String data) {
            try {
                JSONObject myObject = new JSONObject(data);

                JSONObject defaultImageObj = myObject.getJSONObject("default_image");
                String imageUrl = defaultImageObj.getString("regular_url");

                // Use an image loading library like Picasso or Glide to load the image into the ImageView
                Glide.with(getApplicationContext())
                        .load(imageUrl)
                        .into(defaultImageIv);

                String id = String.valueOf(myObject.getInt("id"));
                idTv.setText(id);

                String commonName = myObject.getString("common_name");
                commonNameTv.setText(commonName);

                JSONArray scientificNamesArray = myObject.getJSONArray("scientific_name");
                String scientificNames = "";
                if (scientificNamesArray.length() > 0) {
                    for (int i = 0; i < scientificNamesArray.length(); i++) {
                        String scientificName = scientificNamesArray.getString(i);
                        scientificNames += scientificName + ", ";
                    }
                    if (scientificNames.endsWith(", ")) {
                        scientificNames = scientificNames.substring(0, scientificNames.length() - 2);
                    }
                } else {
                    scientificNames = "N/A";
                }
                scientificNameTv.setText(scientificNames);

                JSONArray otherNamesArray = myObject.getJSONArray("other_name");
                String otherNames = "";
                if (otherNamesArray.length() > 0) {
                    for (int i = 0; i < otherNamesArray.length(); i++) {
                        String otherName = otherNamesArray.getString(i);
                        otherNames += otherName + ", ";
                    }
                    if (otherNames.endsWith(", ")) {
                        otherNames = otherNames.substring(0, otherNames.length() - 2);
                    }
                } else {
                    otherNames = "N/A";
                }
                otherNameTv.setText(otherNames);

                String family = myObject.optString("family", "N/A");
                familyTv.setText(family);

                String origins = "";
                JSONArray originArray = myObject.getJSONArray("origin");
                if (originArray.length() > 0) {
                    for (int i = 0; i < originArray.length(); i++) {
                        String origin = originArray.getString(i);
                        origins += origin + ", ";
                    }
                    if (origins.endsWith(", ")) {
                        origins = origins.substring(0, origins.length() - 2);
                    }
                } else {
                    origins = "N/A";
                }
                originTv.setText(origins);

                String type = myObject.getString("type");
                typeTv.setText(type);

                String dimension = myObject.getString("dimension");
                dimensionTv.setText(dimension);

                String cycle = myObject.getString("cycle");
                cycleTv.setText(cycle);

// Attracts
                JSONArray attractsArray = myObject.getJSONArray("attracts");
                String attracts = "";
                if (attractsArray.length() > 0) {
                    for (int i = 0; i < attractsArray.length(); i++) {
                        attracts += attractsArray.getString(i) + ", ";
                    }
                    attracts = attracts.substring(0, attracts.length() - 2);
                } else {
                    attracts = "N/A";
                }
                attractsTv.setText(attracts);

// Propagation
                JSONArray propagationArray = myObject.getJSONArray("propagation");
                String propagation = "";
                for (int i = 0; i < propagationArray.length(); i++) {
                    propagation += propagationArray.getString(i) + ", ";
                }
                if (!propagation.isEmpty()) {
                    propagation = propagation.substring(0, propagation.length() - 2);
                }
                propagationTv.setText(propagation);
// Hardiness
                JSONObject hardinessObject = myObject.getJSONObject("hardiness");
                String minHardiness = hardinessObject.getString("min");
                String maxHardiness = hardinessObject.getString("max");
                String hardiness = "Min: " + minHardiness + " - Max: " + maxHardiness;
                hardinessTv.setText(hardiness);

// Hardiness Location
                String hardinessLocation = myObject.getJSONObject("hardiness_location").getString("full_url");
                hardinessLocationTv.setText(hardinessLocation);

// Watering
                String watering = myObject.getString("watering");
                wateringTv.setText(watering);

// Sunlight
                JSONArray sunlightArray = myObject.getJSONArray("sunlight");
                String sunlight = "";
                for (int i = 0; i < sunlightArray.length(); i++) {
                    sunlight += sunlightArray.getString(i) + ", ";
                }
                if (!sunlight.isEmpty()) {
                    sunlight = sunlight.substring(0, sunlight.length() - 2);
                }
                sunlightTv.setText(sunlight);
// Maintenance
                String maintenance = myObject.optString("maintenance", "N/A");
                maintenanceTv.setText(maintenance);

// Care Guides
                String careGuides = myObject.getString("care-guides");
                careGuidesTv.setText(careGuides);

// Soil
                JSONArray soilArray = myObject.getJSONArray("soil");
                String soil = "";
                for (int i = 0; i < soilArray.length(); i++) {
                    soil += soilArray.getString(i) + ", ";
                }
                if (!soil.isEmpty()) {
                    soil = soil.substring(0, soil.length() - 2);
                }
                soilTv.setText(soil);

// Growth Rate
                String growthRate = myObject.getString("growth_rate");
                growthRateTv.setText(growthRate);

// Drought Tolerant
                boolean droughtTolerant = myObject.getBoolean("drought_tolerant");
                droughtTolerantTv.setText(String.valueOf(droughtTolerant));

// Salt Tolerant
                boolean saltTolerant = myObject.getBoolean("salt_tolerant");
                saltTolerantTv.setText(String.valueOf(saltTolerant));

// Thorny
                boolean thorny = myObject.getBoolean("thorny");
                thornyTv.setText(String.valueOf(thorny));

// Invasive
                boolean invasive = myObject.getBoolean("invasive");
                invasiveTv.setText(String.valueOf(invasive));

// Tropical
                boolean tropical = myObject.getBoolean("tropical");
                tropicalTv.setText(String.valueOf(tropical));

// Indoor
                boolean indoor = myObject.getBoolean("indoor");
                indoorTv.setText(String.valueOf(indoor));

// Care Level
                String careLevel = myObject.getString("care_level");
                careLevelTv.setText(careLevel);

// Pest Susceptibility
                String pestSusceptibility = myObject.optString("pest_susceptibility", "N/A");
                pestSusceptibilityTv.setText(pestSusceptibility);

// Pest Susceptibility API
                String pestSusceptibilityApi = myObject.optString("pest_susceptibility_api", "N/A");
                pestSusceptibilityApiTv.setText(pestSusceptibilityApi);
                // Flowers
                boolean flowers = myObject.getBoolean("flowers");
                flowersTv.setText(String.valueOf(flowers));

// Flowering Season
                String floweringSeason = myObject.optString("flowering_season", "N/A");
                floweringSeasonTv.setText(floweringSeason);

// Flower Color
                String flowerColor = myObject.optString("flower_color", "N/A");
                flowerColorTv.setText(flowerColor);

// Cones
                boolean cones = myObject.getBoolean("cones");
                conesTv.setText(String.valueOf(cones));

// Fruits
                boolean fruits = myObject.getBoolean("fruits");
                fruitsTv.setText(String.valueOf(fruits));

// Edible Fruit
                boolean edibleFruit = myObject.getBoolean("edible_fruit");
                edibleFruitTv.setText(String.valueOf(edibleFruit));

// Edible Fruit Taste Profile
                String edibleFruitTasteProfile = myObject.optString("edible_fruit_taste_profile", "N/A");
                edibleFruitTasteProfileTv.setText(edibleFruitTasteProfile);

// Fruit Nutritional Value
                String fruitNutritionalValue = myObject.optString("fruit_nutritional_value", "N/A");
                fruitNutritionalValueTv.setText(fruitNutritionalValue);

// Fruit Color
                JSONArray fruitColorArray = myObject.getJSONArray("fruit_color");
                String fruitColor = "";
                if (fruitColorArray.length() > 0) {
                    for (int i = 0; i < fruitColorArray.length(); i++) {
                        fruitColor += fruitColorArray.getString(i) + ", ";
                    }
                    fruitColor = fruitColor.substring(0, fruitColor.length() - 2);
                } else {
                    fruitColor = "N/A";
                }
                fruitColorTv.setText(fruitColor);

// Harvest Season
                String harvestSeason = myObject.optString("harvest_season", "N/A");
                harvestSeasonTv.setText(harvestSeason);

// Harvest Method
                String harvestMethod = myObject.optString("harvest_method", "N/A");
                harvestMethodTv.setText(harvestMethod);

// Leaf
                boolean leaf = myObject.getBoolean("leaf");
                leafTv.setText(String.valueOf(leaf));

// Leaf Color
                JSONArray leafColorArray = myObject.getJSONArray("leaf_color");
                String leafColor = "";
                if (leafColorArray.length() > 0) {
                    for (int i = 0; i < leafColorArray.length(); i++) {
                        leafColor += leafColorArray.getString(i) + ", ";
                    }
                    leafColor = leafColor.substring(0, leafColor.length() - 2);
                } else {
                    leafColor = "N/A";
                }
                leafColorTv.setText(leafColor);

// Edible Leaf
                boolean edibleLeaf = myObject.getBoolean("edible_leaf");
                edibleLeafTv.setText(String.valueOf(edibleLeaf));

// Edible Leaf Taste Profile
                String edibleLeafTasteProfile = myObject.optString("edible_leaf_taste_profile", "N/A");
                edibleLeafTasteProfileTv.setText(edibleLeafTasteProfile);

// Leaf Nutritional Value
                String leafNutritionalValue = myObject.optString("leaf_nutritional_value", "N/A");
                leafNutritionalValueTv.setText(leafNutritionalValue);

// Cuisine
                boolean cuisine = myObject.getBoolean("cuisine");
                cuisineTv.setText(String.valueOf(cuisine));

// Cuisine List
                String cuisineList = myObject.optString("cuisine_list", "N/A");
                cuisineListTv.setText(cuisineList);

// Medicinal
                boolean medicinal = myObject.getBoolean("medicinal");
                medicinalTv.setText(String.valueOf(medicinal));

// Medicinal Use
                String medicinalUse = myObject.optString("medicinal_use", "N/A");
                medicinalUseTv.setText(medicinalUse);

// Medicinal Method
                String medicinalMethod = myObject.optString("medicinal_method", "N/A");
                medicinalMethodTv.setText(medicinalMethod);


                boolean poisonousToHumans = myObject.getBoolean("poisonous_to_humans");
                poisonousToHumansTv.setText(String.valueOf(poisonousToHumans));

// Description
                String description = myObject.optString("description", "N/A");
                TextView descriptionTv = findViewById(R.id.description_tv);
                descriptionTv.setText(description);

// Problem
                String problem = myObject.optString("problem", "N/A");
                TextView problemTv = findViewById(R.id.problem_tv);
                problemTv.setText(problem);


            } catch (JSONException ex) {
                ex.printStackTrace();
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
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }
}