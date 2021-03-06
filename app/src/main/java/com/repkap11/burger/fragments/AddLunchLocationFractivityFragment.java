package com.repkap11.burger.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.repkap11.burger.R;
import com.repkap11.burger.activities.base.Fractivity;
import com.repkap11.burger.models.LunchLocation;

/**
 * Created by paul on 8/8/17.
 */
public class AddLunchLocationFractivityFragment extends Fractivity.FractivityFragment {
    private static final String TAG = AddLunchLocationFractivityFragment.class.getSimpleName();
    public static final String STARTING_INTENT_WHICH_LUNCH_GROUP = "com.repkap11.burger.STARTING_INTENT_WHICH_LUNCH_GROUP";
    private EditText mEditTextName;
    private Button mSaveLocationButtion;

    private String mLunchGroup;

    @Override
    protected void create(Bundle savedInstanceState) {

    }

    @Override
    protected void saveState(Bundle outState) {

    }

    @Override
    protected void restoreState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Intent startingIntent = getActivity().getIntent();
        if (startingIntent == null) {
            Log.e(TAG, "Somehow we want to start, but don't have a starting intent");
            getActivity().finish();
            return null;
        }
        mLunchGroup = startingIntent.getStringExtra(STARTING_INTENT_WHICH_LUNCH_GROUP);
        //Log.e(TAG, "create: mLunchGroup:" + mLunchGroup);
        if (mLunchGroup == null) {
            getActivity().finish();
        }
        View rootView = inflater.inflate(R.layout.fractivity_add_lunch_location, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.fractivity_bar_menu_app_bar_layout);
        toolbar.setTitle(R.string.fractivity_add_lunch_location_title);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_clear_black, null);
        toolbar.setNavigationIcon(drawable);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        mEditTextName = (EditText) rootView.findViewById(R.id.fractivity_add_lunch_location_edit_text_name);
        mSaveLocationButtion = (Button) rootView.findViewById(R.id.fractivity_add_lunch_location_button_save);
        mSaveLocationButtion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                //Log.e(TAG, "Adding lunch location to:" + mLunchGroup + "/lunch_locations");
                DatabaseReference locationsRef = database.getReference(mLunchGroup + "/lunch_locations");
                DatabaseReference newLocation = locationsRef.push();
                //Log.e(TAG, "Created new ref lunch location:" + newLocation.toString());
                newLocation.setValue(new LunchLocation(mEditTextName.getText().toString()));

                getActivity().finish();
            }
        });
        return rootView;
    }

    @Override
    protected void destroyView() {
        mEditTextName = null;
        mSaveLocationButtion = null;
    }
}
