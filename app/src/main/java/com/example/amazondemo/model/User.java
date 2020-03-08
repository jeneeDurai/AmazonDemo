package com.example.amazondemo.model;

public class User {
    private String name;
    private String email;
    private String password;
    private String image;
    private String address;

    public User(){

    }

    public User(String name, String email, String password, String image, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.image = image;
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
}
