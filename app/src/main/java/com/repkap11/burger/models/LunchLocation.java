package com.repkap11.burger.models;

/**
 * Created by paul on 8/2/17.
 */

public class LunchLocation {
    public String displayName;
    public LunchPreference lunch_preference_1;
    public LunchPreference lunch_preference_2;
    public LunchPreference lunch_preference_3;
    public LunchPreference lunch_preference_4;
    public LunchPreference lunch_preference_5;

    public LunchLocation() {
    }

    public LunchLocation(String displayName) {
        this();
        this.displayName = displayName;
    }
}
