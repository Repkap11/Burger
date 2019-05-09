package com.repkap11.burger.activities;

import android.os.Bundle;

import com.repkap11.burger.activities.base.Fractivity;
import com.repkap11.burger.fragments.TabFractivityFragment;

/**
 * Created by paul on 8/10/17.
 */

public class TabFractivity extends Fractivity<TabFractivityFragment> {
    @Override
    protected TabFractivityFragment createFragment(Bundle savedInstanceState) {
        return new TabFractivityFragment();
    }
}
