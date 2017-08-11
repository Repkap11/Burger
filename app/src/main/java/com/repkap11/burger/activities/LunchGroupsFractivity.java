package com.repkap11.burger.activities;

import com.repkap11.burger.activities.base.FirebaseAdapterFractivity;
import com.repkap11.burger.fragments.LunchGroupFractivityFragment;
import com.repkap11.burger.models.LunchGroup;


public class LunchGroupsFractivity extends FirebaseAdapterFractivity<LunchGroupFractivityFragment.Holder, LunchGroup> {
    @Override
    protected FirebaseAdapterFragment createFirebaseFragment() {
        return new LunchGroupFractivityFragment();
    }


}
