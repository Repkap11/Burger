package com.repkap11.burger.activities;

import com.repkap11.burger.activities.base.FirebaseAdapterFractivity;
import com.repkap11.burger.fragments.LunchLocationFractivityFragment;
import com.repkap11.burger.models.LunchLocation;


public class LunchLocationsFractivity extends FirebaseAdapterFractivity<LunchLocationFractivityFragment.Holder, LunchLocation> {
    @Override
    protected FirebaseAdapterFragment createFirebaseFragment() {
        return new LunchLocationFractivityFragment();
    }

}
