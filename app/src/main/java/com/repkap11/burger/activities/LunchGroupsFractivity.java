package com.repkap11.burger.activities;

import com.repkap11.burger.activities.base.FirebaseAdapterFractivity;
import com.repkap11.burger.fragments.LunchGroupsFractivityFragment;
import com.repkap11.burger.models.LunchGroup;


public class LunchGroupsFractivity extends FirebaseAdapterFractivity<LunchGroupsFractivityFragment.Holder, LunchGroup> {
    @Override
    protected FirebaseAdapterFragment createFirebaseFragment() {
        return new LunchGroupsFractivityFragment();
    }


}
