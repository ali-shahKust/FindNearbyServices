package com.Arslan.Majid.Alladin.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Users {

    public static String user_name;
    public  static String user_password;
    public static  String user_phone;

    public Users(String user_name, String user_phone , String user_password) {
        this.user_name = user_name;
        this.user_phone = user_phone;
        this.user_password = user_password;
    }

    public static String getUser_password() {
        return user_password;
    }

    public static void setUser_password(String user_password) {
        Users.user_password = user_password;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public Users(){

    }

}
