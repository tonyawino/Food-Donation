package com.example.android.fooddonation;

public class Item {
    private String food;
    private String userId;
    private String id;

    public Item(String food, String userId, String id) {
        this.food = food;
        this.userId = userId;
        this.id = id;
    }

    public Item() {
        this.food = "";
        this.userId = "";
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return food + " " + userId;
    }
}
