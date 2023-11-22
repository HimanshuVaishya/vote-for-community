package com.example.voteforcommunity1.model;

public class communitymodel {
    int id;
    String code;
    String name;
    String email;
    String password;

    communitymodel(){
    }
    public communitymodel(int id, String code, String name, String email, String password) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
