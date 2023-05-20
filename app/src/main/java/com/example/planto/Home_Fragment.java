package com.example.planto;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import pl.droidsonroids.gif.GifImageView;

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

    public void onStart() {
        super.onStart();
        TextView name;
        CardView card_view_1, card_view_2, card_view_3, card_view_4;
        GifImageView home_sun;
        Animation left, right, down;

        card_view_1 = getActivity().findViewById(R.id.card_view_1);
        card_view_2 = getActivity().findViewById(R.id.card_view_2);
        card_view_3 = getActivity().findViewById(R.id.card_view_3);
        card_view_4 = getActivity().findViewById(R.id.card_view_4);

        down = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.down);
        left = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.left);
        right = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.right);

        card_view_1.setAnimation(left);
        card_view_2.setAnimation(right);
        card_view_3.setAnimation(left);
        card_view_4.setAnimation(right);

        name = (TextView) getActivity().findViewById(R.id.name);
        name.setText("Straw Hats");

    }
}