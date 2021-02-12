package com.quantum.myaata.Models;

public class UsersModel {
    String name,user_id,address;

    public UsersModel(String name, String user_id, String address) {
        this.name = name;
        this.user_id = user_id;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
