package com.repkap11.burger.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.repkap11.burger.R;
import com.repkap11.burger.activities.base.Fractivity;
import com.repkap11.burger.models.User;

public class AboutUserFractivity extends Fractivity<AboutUserFractivity.AboutUserFragment> {

    public static final String STARTING_INTENT_USER_INITIAL_NAME = "com.repkap11.burger.STARTING_INTENT_USER_INITIAL_NAME";
    public static final String STARTING_INTENT_USER_KEY = "com.repkap11.burger.STARTING_INTENT_USER_KEY";

    @Override
    protected AboutUserFragment createFragment(Bundle savedInstanceState) {
        Intent strtingIntent = getIntent();
        if (strtingIntent == null) {
            finish();
        }
        return new AboutUserFragment();
    }

    public static class AboutUserFragment extends Fractivity.FractivityFragment {
        private TextView mEditTextFullName;
        private TextView mEditTextCarSize;
        private Toolbar mToolbar;

        @Override
        protected void create(Bundle savedInstanceState) {
        }

        @Override
        protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fractivity_about_user, container, false);
            mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
            //toolbar.setTitle(R.string.fractivity_about_user_title);
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_clear_black, null);
            mToolbar.setNavigationIcon(drawable);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
            mEditTextFullName = (TextView) rootView.findViewById(R.id.fractivity_about_user_edit_text_full_name);
            mEditTextCarSize = (TextView) rootView.findViewById(R.id.fractivity_about_user_edit_text_car_size);

            Intent startingIntent = getActivity().getIntent();
            if (startingIntent == null) {
                getActivity().finish();
            }
            String initialName = startingIntent.getStringExtra(STARTING_INTENT_USER_INITIAL_NAME);
            ;
            String userKey = startingIntent.getStringExtra(STARTING_INTENT_USER_KEY);
            if (userKey == null) {
                getActivity().finish();
                return rootView;
            }
            mToolbar.setTitle(initialName);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference locationsRef = database.getReference("users/" + userKey);
            locationsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = null;
                    try {
                        user = dataSnapshot.getValue(User.class);
                    } catch (DatabaseException e) {
                        e.printStackTrace();
                    }
                    if (user == null) {
                        return;
                    }
                    mEditTextFullName.setText(user.firstName + " " + user.lastName);
                    mToolbar.setTitle(user.firstName);
                    mEditTextCarSize.setText(user.carSize);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return rootView;
        }

        @Override
        protected void destroyView() {
            mEditTextCarSize = null;
            mEditTextFullName = null;
        }
    }
}
