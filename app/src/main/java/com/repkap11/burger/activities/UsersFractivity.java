package com.repkap11.burger.activities;

import com.repkap11.burger.activities.base.FirebaseAdapterFractivity;
import com.repkap11.burger.fragments.UsersFractivityFragment;
import com.repkap11.burger.models.User;


public class UsersFractivity extends FirebaseAdapterFractivity<UsersFractivityFragment.Holder, User> {
    @Override
    protected FirebaseAdapterFragment createFirebaseFragment() {
        return new UsersFractivityFragment();
    }
}
