package com.example.planto;

import com.google.firebase.Timestamp;

public class User {
    private String username;
    private String email;
    private String avatar_url = "https://firebasestorage.googleapis.com/v0/b/planto-c810d.appspot.com/o/profile%2Fdefault?alt=media&token=1bec04b9-4082-44da-8d4a-bdba7ae81cb1";
    private Timestamp created_at;

    public User() {
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.created_at = Timestamp.now();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
}
