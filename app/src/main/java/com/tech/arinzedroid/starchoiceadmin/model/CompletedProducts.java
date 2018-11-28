package com.tech.arinzedroid.starchoiceadmin.model;

import java.util.Date;

public class CompletedProducts {

    String id;
    String userId;
    String productId;
    Date dateCreated;
    Date dateUpdated;
    boolean active = true;
    boolean paidFully;
    double amtPaid = 0;
    ProductsModel productModel;
    ClientsModel userModel;

    public CompletedProducts(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isPaidFully() {
        return paidFully;
    }

    public void setPaidFully(boolean paidFully) {
        this.paidFully = paidFully;
    }

    public double getAmtPaid() {
        return amtPaid;
    }

    public void setAmtPaid(double amtPaid) {
        this.amtPaid = amtPaid;
    }

    public ProductsModel getProductModel() {
        return productModel;
    }

    public void setProductModel(ProductsModel productModel) {
        this.productModel = productModel;
    }

    public ClientsModel getUserModel() {
        return userModel;
    }

    public void setUserModel(ClientsModel userModel) {
        this.userModel = userModel;
    }
}
