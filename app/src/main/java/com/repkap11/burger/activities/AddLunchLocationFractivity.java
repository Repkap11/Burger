package com.repkap11.burger.activities;

import android.os.Bundle;

import com.repkap11.burger.activities.base.Fractivity;
import com.repkap11.burger.activities.fragments.AddLunchLocationFractivityFragment;

public class AddLunchLocationFractivity extends Fractivity<AddLunchLocationFractivityFragment> {
    @Override
    protected AddLunchLocationFractivityFragment createFragment(Bundle savedInstanceState) {
        return new AddLunchLocationFractivityFragment();
    }

}
