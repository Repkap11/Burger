package com.repkap11.burger.models;

/**
 * Created by paul on 8/2/17.
 */

public class User {
    public String firstName;
    public String lastName;
    public String carSize;
    //But it is zero indexed you see, Sunday is day 0.
    public String lunch_preference_1 = "-Kqa1x8QJ_IG9tScBmCW";//Lunch and Learn
    public String lunch_preference_2;
    public String lunch_preference_3 = "-Kq_wkBxe24w3JxHFUqR";//Bill Gray's
    public String lunch_preference_4;
    public String lunch_preference_5;

    public User() {
    }

    public User(String firstName, String lastName, String carSize) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.carSize = carSize;
    }
}
