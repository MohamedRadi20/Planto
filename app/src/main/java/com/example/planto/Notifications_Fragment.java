package com.example.planto;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Notifications_Fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }
    public void onStart (){
        super.onStart();
        ProgressBar progressBar ;
        Button inbox ;

        progressBar = (ProgressBar) getActivity().findViewById(R.id.loadingNoti);
        inbox = getActivity().findViewById(R.id.inbox);

        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

            }
        });




    }
}