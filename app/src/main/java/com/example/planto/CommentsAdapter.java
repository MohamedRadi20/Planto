package com.example.planto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentsAdapter extends BaseAdapter {
    private Context mContext;
    private List<Comment> comments;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public CommentsAdapter(Context context, List<Comment> comments) {
        this.mContext = context;
        this.comments = comments;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Comment getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.comment, parent, false);
        }
        Comment comment = comments.get(position);
        TextView comment_username = convertView.findViewById(R.id.comment_username);
        TextView comment_content = convertView.findViewById(R.id.comment_text);
        ImageView comment_profile_image = convertView.findViewById(R.id.comment_profile_image);
        db.collection("users").document(comment.getUser_id()).get().addOnSuccessListener(documentSnapshot -> {
            User commentUser = documentSnapshot.toObject(User.class);
            Picasso.get().load(commentUser.getAvatar_url()).into(comment_profile_image);
            comment_username.setText(commentUser.getUsername());

        });
        comment_content.setText(comment.getContent());
        return convertView;
    }
}
