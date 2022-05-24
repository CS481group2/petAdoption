package com.example.petadoption;

public class Animal {
    private String breed;
    private String image_url;
    private String desc;
    private String petname;
    private String timestamp;
    private String user_id;

    public Animal()
    {

    }

    public Animal(String b, String i, String d, String p, String t, String u)
    {
        breed = b;
        image_url = i;
        desc = d;
        petname = p;
        timestamp = t;
        user_id = u;
    }

    public String getDesc()
    {
        return this.desc;
    }
    public void setBreed(String s)
    {
        desc = s;
    }
    public String getImage_url()
    {
        return this.image_url;
    }
    public void setUrl(String s)
    {
        image_url = s;
    }
}