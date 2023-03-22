package com.example.planto;

public class Users {
    String name, email, password,profileImageUrl="https://firebasestorage.googleapis.com/v0/b/planto-c810d.appspot.com/o/profile%2Fdefault?alt=media&token=1bec04b9-4082-44da-8d4a-bdba7ae81cb1";

    public Users() {
    }

    public Users(String name, String email, String password, String profileImageUrl) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
    }

    public Users(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
