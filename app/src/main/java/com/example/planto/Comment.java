package com.example.planto;

import com.google.firebase.Timestamp;

public class Comment {
    private String user_id;
    private String content;
    private Timestamp created_at;

    public Comment() {
    }

    public Comment(String user_id, String content) {
        this.user_id = user_id;
        this.content = content;
        this.created_at = Timestamp.now();
    }
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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
}