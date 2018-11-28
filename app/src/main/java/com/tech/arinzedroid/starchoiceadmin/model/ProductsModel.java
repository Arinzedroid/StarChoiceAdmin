package com.tech.arinzedroid.starchoiceadmin.model;

import com.tech.arinzedroid.starchoiceadmin.utils.RandomString;

import org.parceler.Parcel;

import java.util.Date;

@Parcel
public class ProductsModel {
    String productName;
    String desc;
    String id = new RandomString().nextString();
    boolean isActive;
    double price;
    Date dateCreated = new Date();

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
