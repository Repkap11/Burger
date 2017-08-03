package com.repkap11.burger.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.repkap11.burger.R;
import com.repkap11.burger.activities.base.Fractivity;
import com.repkap11.burger.models.User;

public class AboutUserFractivity extends Fractivity<AboutUserFractivity.AboutUserFragment> {

    @Override
    protected AboutUserFragment createFragment(Bundle savedInstanceState) {
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


            String userKey = "-Kq__ik09zLOUVFLkc2z";
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference locationsRef = database.getReference("users/" + userKey);
            locationsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
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
