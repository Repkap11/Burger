package com.repkap11.burger.activities;

import android.content.Intent;
import android.widget.TextView;

import com.repkap11.burger.activities.base.FirebaseAdapterFractivity;
import com.repkap11.burger.activities.fragments.LunchLocationFractivityFragment;
import com.repkap11.burger.models.LunchLocation;


public class LunchLocationsFractivity extends FirebaseAdapterFractivity<LunchLocationFractivityFragment.Holder, LunchLocation> {
    @Override
    protected FirebaseAdapterFragment createFirebaseFragment() {
        return new LunchLocationFractivityFragment<LunchLocationFractivityFragment.Holder>();
    }

}
