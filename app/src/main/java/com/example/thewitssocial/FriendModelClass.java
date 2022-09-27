package com.example.thewitssocial;

import com.google.firebase.database.PropertyName;

public class FriendModelClass {
    private String Name,Surname;

    public FriendModelClass(String name, String surname) {
        this.Name = name;
        this.Surname = surname;
    }

    public FriendModelClass() {
    }
    @PropertyName("Name")
    public String getName() {
        return Name;
    }
    @PropertyName("Name")
    public void setName(String name) {
        Name = name;
    }
    @PropertyName("Surname")
    public String getSurname() {
        return Surname;
    }
    @PropertyName("Surname")
    public void setSurname(String surname) {
        Surname = surname;
    }
}
