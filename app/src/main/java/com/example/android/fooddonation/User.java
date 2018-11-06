package com.example.android.fooddonation;

public class User {
    private String firstName;
    private String lastName;
    private String location;
    private String contact;

    public User(){

    }

    public User(String firstName, String lastName, String location, String contact) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.contact = contact;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLocation() {
        return location;
    }

    public String getContact() {
        return contact;
    }


}
