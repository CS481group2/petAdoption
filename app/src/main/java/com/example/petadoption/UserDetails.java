package com.example.petadoption;

import java.io.Serializable;

public class UserDetails {
    static String name = "";
    static String uid = "";
    static String password = "";
    static String chatWith = "";
    static boolean currUser = false;

    public UserDetails(String name, String uid)
    {
        this.name = name;
        this.uid = uid;
    }

    public static String getUid()
    {
        return uid;
    }

    public static void setChatWith(String user)
    {
        chatWith = user;
    }

    public static String getChatWith()
    {
        return chatWith;
    }

    public static void setCurrUser()
    {
        currUser = true;
    }

    public static boolean getCurrentUser()
    {
        return currUser;
    }

    public static String getName()
    {
        return name;
    }
}
