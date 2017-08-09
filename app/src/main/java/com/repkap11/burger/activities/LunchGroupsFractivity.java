package com.repkap11.burger.activities;

import android.widget.TextView;

import com.repkap11.burger.activities.base.FirebaseAdapterFractivity;
import com.repkap11.burger.activities.fragments.LunchGroupFractivityFragment;
import com.repkap11.burger.models.LunchGroup;


public class LunchGroupsFractivity extends FirebaseAdapterFractivity<LunchGroupFractivityFragment.Holder, LunchGroup> {

    @Override
    protected FirebaseAdapterFragment createFirebaseFragment() {
        return new LunchGroupFractivityFragment<LunchGroupFractivityFragment.Holder>();
    }


}
