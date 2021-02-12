package com.quantum.myaata.Models;

public class ExtraCommissionModel {

    String details,time,commission;

    public ExtraCommissionModel(String details, String time, String commission) {
        this.details = details;
        this.time = time;
        this.commission = commission;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }
}
