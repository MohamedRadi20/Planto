package com.example.planto;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 *
 */
public class Profile_Fragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);

    }
    public void onStart (){
        super.onStart();
        TextView name ;
        ImageView logout;
        LinearLayout logout_profile ;

        logout_profile = getActivity().findViewById(R.id.logout_profile);
        logout_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), Login_Activity.class);
                startActivity(intent);
                getActivity().finish();

            }});

        SharedPreferences sharedPreferences ;
        name = (TextView) getActivity().findViewById(R.id.name);
        //  email = (TextView) findViewById(R.id.email);
        logout = (ImageView) getActivity().findViewById(R.id.logout);

        name.setText("Straw Hats");

        sharedPreferences = getActivity().getSharedPreferences("shared",MODE_PRIVATE);
        //name.setText(sharedPreferences.getString("name",""));
        //  email.setText(sharedPreferences.getString("email",""));

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                String url ="http://192.168.1.4/login-registration-android/logout.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("damn", response);
                                if (response.equals("success")) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("logged", "");
                                    editor.putString("name", "");
                                    editor.putString("email", "");
                                    editor.putString("apiKey", "");
                                    editor.apply();
                                    Toast.makeText(getActivity().getApplicationContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity().getApplicationContext(), Login_Activity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                                else{
                                    Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                }
                            }

                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error_) {
                        error_.printStackTrace();
                    }
                }){
                    protected Map<String, String> getParams(){
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("email", sharedPreferences.getString("email",""));
                        paramV.put("apiKey", sharedPreferences.getString("apiKey",""));
                        return paramV;
                    }
                };
                queue.add(stringRequest);

            }
        });

    }
}