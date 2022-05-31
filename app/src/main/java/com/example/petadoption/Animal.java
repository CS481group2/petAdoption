package com.example.petadoption;

public class Animal {
    private String breed;
    private String image_url;
    private String desc;
    private String petname;
    private String timestamp;
    private String user_id;
    private String location;

    public Animal()
    {

    }

    public Animal(String br, String img, String des, String pname, String time, String user, String loc)
    {
        breed = br;
        image_url = img;
        desc = des;
        petname = pname;
        timestamp = time;
        user_id = user;
        location= loc;
    }

    public String getUser_id()
    {
        return this.user_id;
    }

    public String getDesc()
    {
        return this.desc;
    }

    public String getPetname(){
        return this.petname;
    }
    public String getBreed(){
        return  this.breed;
    }

    public String getLocation(){
        return  this.location;
    }
    public String getImage_url()
    {
        return this.image_url;
    }
    /*
    public String getTimestamp(){
        return this.timestamp;
    }


    public String getBreed() {
        return breed;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getLocation() {
        return location;
    }

     */
    /*
    public void setBreed(String s)
    {

        desc = s;
    }

    public void setUrl(String s)
    {

        image_url = s;
    }
    */
}