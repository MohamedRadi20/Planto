package com.example.planto;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class PlantAdapter extends RecyclerView.Adapter<PlantViewHolder> {
    private List<Plant> plantList = new ArrayList<>();
    private List<String> plantIds = new ArrayList<>();


    public void setData(List<Plant> plantList, List<String> plantIds) {
        this.plantList = plantList;
        this.plantIds = plantIds;
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
    public void onBindViewHolder(@NonNull PlantViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Plant plant = plantList.get(position);
        holder.plantCommonName.setText(plant.getCommonName());
        holder.plantScientificName.setText(plant.getScientificName());
        holder.sunlight.setText(plant.sunlight());
        holder.other_name.setText(plant.other_name());
        Glide.with(holder.itemView.getContext()).load(plant.getImageUrl()).into(holder.plantImage);
        holder.cycle.setText(plant.cycle());
        holder.watering.setText(plant.watering());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String plantId = plantIds.get(position);
                Toast.makeText(v.getContext(), "Plant ID: " + plantId, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(holder.itemView.getContext(), Plant_Details.class);
                intent.putExtra("plantId", plantId);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return plantList.size();
    }
}
