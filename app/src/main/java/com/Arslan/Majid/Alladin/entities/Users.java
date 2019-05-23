package com.Arslan.Majid.Alladin.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Users implements Serializable {
    @SerializedName("Id")
    private String Id;
    @SerializedName("FullName")
    private String FullName;
    @SerializedName("Username")
    private  String Username;
    @SerializedName("Password")
    private String Password;
    @SerializedName("PhoneNumber")
    private String PhoneNumber;
    @SerializedName("Gender")
    private  String Gender;
    @SerializedName("DOB")
    private  String DOB;
    @SerializedName("Role")
    private  String Role;
    @SerializedName("City")
    private  String City;
    @SerializedName("Longitude")
    private  double Longitude;
    @SerializedName("Latitude")
    private  double Latitude;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(float longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(float latitude) {
        Latitude = latitude;
    }
    //id,name1,Email1,Password1,age1,role1

    public Users(String id, String fullName, String username, String password, String DOB, String role) {
        Id = id;
        FullName = fullName;
        Username = username;
        Password = password;
        this.DOB = DOB;
        Role = role;
    }
}
