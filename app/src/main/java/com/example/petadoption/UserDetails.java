package com.example.petadoption;

public class UserDetails {
    static String name = "";
    static String uid = "";
    static String password = "";
    static String chatWith = "";

    public UserDetails(String name)
    {
        this.name = name;
    }

    public static void setChatWith(String user)
    {
        chatWith = user;
    }
}
