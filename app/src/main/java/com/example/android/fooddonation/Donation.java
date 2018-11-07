package com.example.android.fooddonation;

public class Donation {
    private boolean donation;
    private boolean fulfilled;
    private String cooked;
    private String food;
    private String amount;
    private String userId;
    private boolean pick;
    private String measure;

    public Donation() {

    }

    public Donation(boolean donation, boolean fulfilled, String cooked, String food, String amount, String userId, boolean pick, String measure) {
        this.donation = donation;
        this.fulfilled = fulfilled;
        this.cooked = cooked;
        this.food = food;
        this.amount = amount;
        this.userId = userId;
        this.pick = pick;
        this.measure = measure;
    }

    public boolean isDonation() {
        return donation;
    }

    public boolean isFulfilled() {
        return fulfilled;
    }

    public String getCooked() {
        return cooked;
    }

    public String getFood() {
        return food;
    }

    public String getAmount() {
        return amount;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isPick() {
        return pick;
    }

    public String getMeasure() {
        return measure;
    }
}
