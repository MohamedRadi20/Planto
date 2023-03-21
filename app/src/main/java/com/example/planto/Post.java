package com.example.planto;

import java.util.List;

public class Post {
    private String imageUrl;
    private String text;
    private String user;
    private int upvotes;
    private int downvotes;
    private List<String> comments;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String imageUrl, String text, String user, int upvotes, int downvotes, List<String> comments) {
        this.imageUrl = imageUrl;
        this.text = text;
        this.user = user;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.comments = comments;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }
}
