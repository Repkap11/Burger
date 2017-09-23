package com.repkap11.burger.activities;

import com.repkap11.burger.activities.base.FirebaseAdapterFractivity;
import com.repkap11.burger.fragments.LunchLocationsFractivityFragment;
import com.repkap11.burger.fragments.LunchLocationsTodayFractivityFragment;
import com.repkap11.burger.models.LunchLocation;


public class LunchLocationsTodayFractivity extends FirebaseAdapterFractivity<LunchLocationsFractivityFragment.Holder, LunchLocation> {
    @Override
    protected FirebaseAdapterFragment createFirebaseFragment() {
        return new LunchLocationsTodayFractivityFragment();
    }

}
