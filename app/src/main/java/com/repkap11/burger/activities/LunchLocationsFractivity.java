package com.repkap11.burger.activities;

import android.content.Intent;
import android.widget.TextView;

import com.repkap11.burger.activities.base.FirebaseAdapterFractivity;
import com.repkap11.burger.activities.fragments.LunchLocationFractivityFragment;
import com.repkap11.burger.models.LunchLocation;


public class LunchLocationsFractivity extends FirebaseAdapterFractivity<LunchLocationsFractivity.Holder, LunchLocation> {

    private static final String TAG = LunchLocationsFractivity.class.getSimpleName();

    public static final int REQUEST_CODE_PICK_LOCATION = 42;
    public static final String STARTING_INTENT_LOCATION_INDEX = "com.repkap11.burger.STARTING_INTENT_LOCATION_INDEX";
    public static final String STARTING_INTENT_WHICH_LUNCH_GROUP = "com.repkap11.burger.STARTING_INTENT_WHICH_LUNCH_GROUP";

    private String mResultIndex;

    @Override
    protected FirebaseAdapterFragment createFirebaseFragment() {
        Intent startingIntent = getIntent();
        if (startingIntent == null) {
            finish();
        }
        return new LunchLocationFractivityFragment<Holder>();
    }

    public static class Holder {
        public TextView mName;
        public int mIndex;
    }
}
