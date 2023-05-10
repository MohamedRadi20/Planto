package com.example.planto;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post {

    private String userId;
    private String postId;
    private String content;
    private String imageUrl;
    private List<React> likes ;
    private List<React> dislikes;
    private List<Comment> comments;
    private Timestamp createdAt;

    public Post() {
    }

    public Post(String user_id, String content, String image_url,List<React> likes, List<React> dislikes, List<Comment> comments) {
        this.userId = user_id;
        this.content = content;
        this.imageUrl = image_url;
        this.likes = likes;
        this.dislikes = dislikes;
        this.comments = comments;
        this.createdAt = Timestamp.now();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<React> getLikes() {
        return likes;
    }

    public void setLikes(List<React> likes) {
        this.likes = likes;
    }

    public List<React> getDislikes() {
        return dislikes;
    }

    public void setDislikes(List<React> dislikes) {
        this.dislikes = dislikes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
