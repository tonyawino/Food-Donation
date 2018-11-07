package com.example.android.fooddonation;

public class User {
    private String firstName;
    private String lastName;
    private String location;
    private String contact;
    private String coordinates;

    public User() {

    }

    public User(String firstName, String lastName, String location, String contact, String coordinates) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.contact = contact;
        this.coordinates = coordinates;
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


    public String getCoordinates() {
        return coordinates;
    }
}
