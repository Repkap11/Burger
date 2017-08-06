package com.repkap11.burger.models;

/**
 * Created by paul on 8/2/17.
 */

public class User {
    public String displayName;
    public String carSize;
    //But it is zero indexed you see, Sunday is day 0.
    public String lunch_preference_1 = "-Kqkv66paMYBnZBAzBHJ";//Lunch and Learn
    public String lunch_preference_2;
    public String lunch_preference_3 = "-KqkuqYJxlROl2jbW5BY";//Bill Gray's
    public String lunch_preference_4;
    public String lunch_preference_5;

    public User() {
    }

    public User(String displayName, String carSize) {
        this();
        this.displayName = displayName;
        this.carSize = carSize;
    }

    public static String getDisplayNameLink() {
        return "displayName";
    }

    public static String getCarSizeLink() {
        return "carSize";
    }
}
