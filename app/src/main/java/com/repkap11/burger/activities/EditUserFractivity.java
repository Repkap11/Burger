package com.repkap11.burger.activities;

import android.os.Bundle;

import com.repkap11.burger.activities.base.Fractivity;
import com.repkap11.burger.fragments.EditUserFractivityFragment;

public class EditUserFractivity extends Fractivity<EditUserFractivityFragment> {
    @Override
    protected EditUserFractivityFragment createFragment(Bundle savedInstanceState) {
        return new EditUserFractivityFragment();
    }

}
