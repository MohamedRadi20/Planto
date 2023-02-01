package com.example.planto;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Home_Fragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);

    }
    public void onStart (){
        super.onStart();
        TextView name;
        ImageView plant_disease_service , identify_plant_service , separate_seeds_service , grating_compatibility_service;
        Animation left , right ;


        plant_disease_service = getActivity().findViewById(R.id.plant_disease_service);
        identify_plant_service = getActivity().findViewById(R.id.identify_plant_service);
        separate_seeds_service = getActivity().findViewById(R.id.separate_seeds_service);
        grating_compatibility_service = getActivity().findViewById(R.id.grating_compatibility_service);

        left = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.left);
        right = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.right);

        plant_disease_service.setAnimation(left);
        identify_plant_service.setAnimation(right);
        separate_seeds_service.setAnimation(left);
        grating_compatibility_service.setAnimation(right);

        name = (TextView) getActivity().findViewById(R.id.name);
        name.setText("Straw Hats");

    }
}