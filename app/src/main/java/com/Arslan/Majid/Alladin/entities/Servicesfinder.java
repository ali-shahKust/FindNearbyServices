package com.Arslan.Majid.Alladin.entities;

public class Servicesfinder {

    public static String user_name , user_role , user_phone , user_number;

    public Servicesfinder(String user_name, String user_role, String user_phone ,String user_number ) {
        this.user_name = user_name;
        this.user_role = user_role;
        this.user_phone = user_phone;
        this.user_number = user_number;

    }

    public static String getUser_number() {
        return user_number;
    }

    public static void setUser_number(String user_number) {
        Servicesfinder.user_number = user_number;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }
    public Servicesfinder(){
        //Empty Constructor
    }
}
