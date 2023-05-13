package com.example.planto;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PlantViewHolder extends RecyclerView.ViewHolder {
    ImageView plantImage;
    TextView plantCommonName;
    TextView plantScientificName;
    TextView cycle;
    TextView watering;
    TextView sunlight;
    TextView other_name;



    public PlantViewHolder(@NonNull View itemView) {
        super(itemView);
        plantImage = itemView.findViewById(R.id.plant_image);
        plantCommonName = itemView.findViewById(R.id.plant_common_name);
        plantScientificName = itemView.findViewById(R.id.plant_scientific_name);
        sunlight = itemView.findViewById(R.id.sunlight);
        other_name = itemView.findViewById(R.id.other_name);
        cycle = itemView.findViewById(R.id.cycle);
        watering = itemView.findViewById(R.id.watering);

    }
}