package com.tech.arinzedroid.starchoiceadmin.model;

import com.tech.arinzedroid.starchoiceadmin.utils.RandomString;

import org.parceler.Parcel;

import java.util.Date;

@Parcel
public class AgentsModel {
    String id = new RandomString().nextString();
    String firstname;
    String lastname;
    String address;
    int age;
    String phone;
    String username;
    String password;
    Date dateCreated = new Date();
    String kinFulname;
    String kinAddress;
    String kinPhone;

    public AgentsModel(){

    }

    public String getKinFulname() {
        return kinFulname;
    }

    public void setKinFulname(String kinFulname) {
        this.kinFulname = kinFulname;
    }

    public String getKinAddress() {
        return kinAddress;
    }

    public void setKinAddress(String kinAddress) {
        this.kinAddress = kinAddress;
    }

    public String getKinPhone() {
        return kinPhone;
    }

    public void setKinPhone(String kinPhone) {
        this.kinPhone = kinPhone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
