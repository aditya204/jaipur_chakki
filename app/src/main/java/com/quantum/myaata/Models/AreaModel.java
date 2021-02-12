package com.quantum.myaata.Models;

public class AreaModel {
    String name;
    String order;


    public AreaModel(String name, String order) {
        this.name = name;
        this.order = order;
    }


    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
