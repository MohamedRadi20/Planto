package com.example.planto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomSpinnerAdapter extends ArrayAdapter<CharSequence> {
    private Context mContext;
    private CharSequence[] mItems;

    public CustomSpinnerAdapter(Context context, int resource, CharSequence[] items) {
        super(context, resource, items);
        mContext = context;
        mItems = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.custom_spinner_item, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.fruits_tv);
        ImageView imageView = convertView.findViewById(R.id.fruits_im);


        switch (position) {
            case 0:
                imageView.setImageResource(R.drawable.fruits_icon);
                textView.setText(mItems[position]);
                break;
            case 1:
                imageView.setImageResource(R.drawable.ornamentals_icon);
                textView.setText(mItems[position]);
                break;
            case 2:
                imageView.setImageResource(R.drawable.flowers_icon);
                textView.setText(mItems[position]);
                break;
            case 3:
                imageView.setImageResource(R.drawable.pests_icon);
                textView.setText(mItems[position]);
                break;
            case 4:
                imageView.setImageResource(R.drawable.rotten_icon);
                textView.setText(mItems[position]);
                break;
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}