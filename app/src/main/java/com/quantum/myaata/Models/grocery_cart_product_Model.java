package com.quantum.myaata.Models;

public class grocery_cart_product_Model {

    private String name,description,price,product_id,itemcount,commision;
    String image;
    private long in_stock;
    private  int inde;



    public grocery_cart_product_Model(String name, String description, String price, String product_id, String itemcount, String image, long in_stock, int inde,String commision) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.product_id = product_id;
        this.itemcount=itemcount;
        this.image = image;
        this.in_stock=in_stock;
        this.inde=inde;
        this.commision=commision;
    }

    public String getCommision() {
        return commision;
    }

    public void setCommision(String commision) {
        this.commision = commision;
    }

    public int getInde() {
        return inde;
    }

    public long getIn_stock() {
        return in_stock;
    }

    public void setIn_stock(long in_stock) {
        this.in_stock = in_stock;
    }

    public String getItemcount() {
        return itemcount;
    }

    public void setItemcount(String itemcount) {
        this.itemcount = itemcount;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }



    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
