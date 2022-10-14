package com.example.thewitssocial;

import com.google.firebase.database.PropertyName;

public class User {

    public String Name,Surname,Email,PhoneNo,Password;

    public User(){

    }
    public User(String Name,String Surname,String Email,String Password,String PhoneNo){
        this.Name = Name;
        this.Surname = Surname;
        this.Email =Email;
        this.Password = Password;
        this.PhoneNo = PhoneNo;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public String getEmail() {
        return Email;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public void setEmail(String email) {
        Email = email;
    }

    @PropertyName("Name")
    public String getName() {
        return Name;
    }
    @PropertyName("Surname")
    public String getSurname() {
        return Surname;
    }
}
