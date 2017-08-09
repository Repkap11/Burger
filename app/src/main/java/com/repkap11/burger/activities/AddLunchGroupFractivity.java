package com.repkap11.burger.activities;

import android.os.Bundle;

import com.repkap11.burger.activities.base.Fractivity;
import com.repkap11.burger.activities.fragments.AddLunchGroupFractivityFragment;

public class AddLunchGroupFractivity extends Fractivity<Fractivity.FractivityFragment> {
    @Override
    protected AddLunchGroupFractivityFragment createFragment(Bundle savedInstanceState) {
        return new AddLunchGroupFractivityFragment();
    }

}
