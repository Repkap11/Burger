package com.repkap11.burger.models;

/**
 * Created by paul on 8/2/17.
 */

public class User {
    public String displayName;
    public Long carSizeNum;
    public Long appVersion;

    //But it is zero indexed you see, Sunday is day 0.
    public String lunch_preference_1;
    public String lunch_preference_2;
    public String lunch_preference_3;
    public String lunch_preference_4;
    public String lunch_preference_5;

    public User() {
    }

    public User(String displayName, Long carSize) {
        this();
        this.displayName = displayName;
        this.carSizeNum = carSize;
    }

    public static String getDisplayNameLink() {
        return "displayName";
    }

    public static String getCarSizeLink() {
        return "carSizeNum";
    }
    public static String getAppVersionLink() {
        return "appVersion";
    }
}
