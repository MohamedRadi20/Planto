package com.example.planto;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private List<Post> itemList;

    public MyAdapter(List<Post> itemList) {
        this.itemList = itemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Post post = itemList.get(position);
        //holder.imageView.setImageResource(R.drawable.user);
        Picasso.get().load(post.getImageUrl()).into( holder.imageView);
        holder.textView.setText(post.getText());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

