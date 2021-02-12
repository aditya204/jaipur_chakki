package com.quantum.myaata.Models;

public class AdminOrderModel {

    private String order_id,time,otp,grandTotal,store_name,store_address,total_commission,store_id;
    private boolean is_paid,is_pickup;

    public AdminOrderModel(String order_id, String time, String otp, String grandTotal, boolean is_paid, boolean is_pickup,String store_address,String store_name,String total_commission,String store_id) {
        this.order_id = order_id;
        this.time = time;
        this.otp = otp;
        this.grandTotal = grandTotal;
        this.is_paid = is_paid;
        this.is_pickup=is_pickup;
        this.store_address=store_address;
        this.store_name=store_name;
        this.total_commission=total_commission;
        this.store_id=store_id;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_address() {
        return store_address;
    }

    public void setStore_address(String store_address) {
        this.store_address = store_address;
    }

    public String getTotal_commission() {
        return total_commission;
    }

    public void setTotal_commission(String total_commission) {
        this.total_commission = total_commission;
    }

    public boolean isIs_pickup() {
        return is_pickup;
    }

    public void setIs_pickup(boolean is_pickup) {
        this.is_pickup = is_pickup;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public boolean isIs_paid() {
        return is_paid;
    }

    public void setIs_paid(boolean is_paid) {
        this.is_paid = is_paid;
    }
}
