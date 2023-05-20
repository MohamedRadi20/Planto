package com.example.planto;

import com.google.firebase.Timestamp;
import com.google.firebase.database.Exclude;

public class Comment {
    private String user_id;
    private String comment_id;
    private String content;
    private Timestamp created_at;

    public Comment() {
        // Default constructor required for Firebase
    }

    public Comment(String user_id, String content) {
        this.user_id = user_id;
        this.content = content;
        this.created_at = Timestamp.now();
        this.comment_id = generateCommentId();
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    @Exclude // Exclude the comment_id field from Firebase serialization
    private String generateCommentId() {
        return String.valueOf(created_at.getSeconds()); // Use seconds as the ID
    }
}
