package com.example.voteforcommunity1.model;

public class candidatemodel {
    int id;
    String communitycode;
    String code;
    String name;
    String email;
    String image;
    String password;
    int canvote;

    public candidatemodel(){

    }

    public candidatemodel(int id, String communitycode, String code, String name, String email, String image, String password, int canvote) {
        this.id = id;
        this.communitycode = communitycode;
        this.code = code;
        this.name = name;
        this.email = email;
        this.image = image;
        this.password = password;
        this.canvote = canvote;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommunitycode() {
        return communitycode;
    }

    public void setCommunitycode(String communitycode) {
        this.communitycode = communitycode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCanvote() {
        return canvote;
    }

    public void setCanvote(int canvote) {
        this.canvote = canvote;
    }
}
