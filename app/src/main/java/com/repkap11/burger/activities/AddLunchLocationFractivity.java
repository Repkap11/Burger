package com.repkap11.burger.activities;

import android.os.Bundle;

import com.repkap11.burger.activities.base.Fractivity;
import com.repkap11.burger.activities.fragments.AddLunchLocationsFractivityFragment;

public class AddLunchLocationFractivity extends Fractivity<AddLunchLocationsFractivityFragment> {


    @Override
    protected AddLunchLocationsFractivityFragment createFragment(Bundle savedInstanceState) {
        return new AddLunchLocationsFractivityFragment();
    }

}
