package com.example.planto;

import com.google.firebase.Timestamp;

public class React {
    private String userId;
    private String reactType;
    private Timestamp createdAt;

    public React() {
    }

    public React(String userId, String reactType) {
        this.userId = userId;
        this.reactType = reactType;
        this.createdAt = Timestamp.now();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReactType() {
        return reactType;
    }

    public void setReactType(String reactType) {
        this.reactType = reactType;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
