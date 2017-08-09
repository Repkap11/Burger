package com.repkap11.burger.activities;

import android.os.Bundle;

import com.repkap11.burger.activities.base.BarMenuFractivity;
import com.repkap11.burger.activities.fragments.AboutUserFractivityFragment;

public class AboutUserFractivity extends BarMenuFractivity {


    @Override
    protected AboutUserFractivityFragment createFragment(Bundle savedInstanceState) {
        return new AboutUserFractivityFragment();
    }

}
