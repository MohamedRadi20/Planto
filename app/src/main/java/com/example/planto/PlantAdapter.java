package com.example.planto;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class PlantAdapter extends RecyclerView.Adapter<PlantViewHolder> {
    private List<Plant> plantList = new ArrayList<>();

    public void setData(List<Plant> plantList) {
        this.plantList = plantList;
        notifyDataSetChanged();
    }

    public List<Plant> getData() {
        return plantList;
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plant_list_item, parent, false);
        return new PlantViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        Plant plant = plantList.get(position);
        holder.plantCommonName.setText(plant.getCommonName());
        holder.plantScientificName.setText(plant.getScientificName());
        holder.sunlight.setText(plant.sunlight());
        holder.other_name.setText(plant.other_name());
        Glide.with(holder.itemView.getContext()).load(plant.getImageUrl()).into(holder.plantImage);
        holder.cycle.setText(plant.cycle());
        holder.watering.setText(plant.watering());

    }

    @Override
    public int getItemCount() {
        return plantList.size();
    }
}
