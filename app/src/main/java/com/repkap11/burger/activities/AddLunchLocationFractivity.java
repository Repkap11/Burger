package com.repkap11.burger.activities;

import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

public class AddLunchLocationFractivity extends Fractivity<AddLunchLocationFractivity.AddLunchLocationsFragment> {

    @Override
    protected AddLunchLocationFractivity.AddLunchLocationsFragment createFragment(Bundle savedInstanceState) {
        return new AddLunchLocationsFragment();
    }

    public static class AddLunchLocationsFragment extends Fractivity.FractivityFragment {
        private EditText mEditTextName;
        private Button mSaveLocationButtion;

        @Override
        protected void create(Bundle savedInstanceState) {

        }

        @Override
        protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fractivity_add_lunch_locations, container, false);
            Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
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
                    DatabaseReference locationsRef = database.getReference("lunch_locations");
                    DatabaseReference newLocation = locationsRef.push();
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
}
