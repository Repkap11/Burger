package com.repkap11.burger.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.repkap11.burger.R;
import com.repkap11.burger.activities.base.Fractivity;
import com.repkap11.burger.models.User;

public class AddUserFractivity extends Fractivity<AddUserFractivity.AddUserFragment> {

    public static final String STARTING_INTENT_WHICH_LUNCH_GROUP = "com.repkap11.burger.STARTING_INTENT_WHICH_LUNCH_GROUP";
    public static final String STARTING_INTENT_EDIT_EXISTING_USER = "com.repkap11.burger.STARTING_INTENT_EDIT_EXISTING_USER";

    private static final String TAG = AddUserFractivity.class.getSimpleName();

    @Override
    protected AddUserFragment createFragment(Bundle savedInstanceState) {
        return new AddUserFragment();
    }

    public static class AddUserFragment extends Fractivity.FractivityFragment {

        private EditText mEditTextCarSize;
        private Button mSaveLocationButtion;
        private EditText mEditTextFirstName;
        private EditText mEditTextLastName;

        private String mLunchGroup;
        private String mExistingUser;
        private boolean mFirstTimeShowing;

        @Override
        protected void create(Bundle savedInstanceState) {
            Intent startingIntent = getActivity().getIntent();
            if (startingIntent == null) {
                Log.e(TAG, "Somehow we want to start, but don't have a starting intent");
                getActivity().finish();
                return;
            }
            mLunchGroup = startingIntent.getStringExtra(STARTING_INTENT_WHICH_LUNCH_GROUP);
            mExistingUser = startingIntent.getStringExtra(STARTING_INTENT_EDIT_EXISTING_USER);
            if (mLunchGroup == null) {
                Log.e(TAG, "Finishing because we don't have a lunch group");
                getActivity().finish();
            }
            mFirstTimeShowing = true;
        }

        @Override
        protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fractivity_add_user, container, false);
            Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
            toolbar.setTitle(R.string.fractivity_add_user_title);
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_clear_black, null);
            toolbar.setNavigationIcon(drawable);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
            mEditTextCarSize = (EditText) rootView.findViewById(R.id.fractivity_add_user_edit_text_car_size);
            mEditTextFirstName = (EditText) rootView.findViewById(R.id.fractivity_add_user_edit_text_first_name);
            mEditTextLastName = (EditText) rootView.findViewById(R.id.fractivity_add_user_edit_text_last_name);


            if (mExistingUser != null && mFirstTimeShowing) {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference user = database.getReference(mExistingUser);

                final DatabaseReference firstNameRef = user.child(User.getFirstNameLink());
                final DatabaseReference lastNameRef = user.child(User.getLastNameLink());
                final DatabaseReference carSizeRef = user.child(User.getCarSizeLink());

                firstNameRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        firstNameRef.removeEventListener(this);
                        if (mEditTextFirstName != null) {
                            mEditTextFirstName.setText(dataSnapshot.getValue(String.class));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                lastNameRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        lastNameRef.removeEventListener(this);
                        if (mEditTextLastName != null) {
                            mEditTextLastName.setText(dataSnapshot.getValue(String.class));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                carSizeRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        carSizeRef.removeEventListener(this);
                        if (mEditTextCarSize != null) {
                            mEditTextCarSize.setText(dataSnapshot.getValue(String.class));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
            mFirstTimeShowing = false;

            mSaveLocationButtion = (Button) rootView.findViewById(R.id.fractivity_add_user_button_save);
            mSaveLocationButtion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference user;
                    if (mExistingUser == null) {
                        DatabaseReference usersRef = database.getReference(mLunchGroup + "/users");
                        user = usersRef.push();
                        user.setValue(new User(mEditTextFirstName.getText().toString(), mEditTextLastName.getText().toString(), mEditTextCarSize.getText().toString()));
                    } else {
                        user = database.getReference(mExistingUser);
                        //user.setValue(new User(mEditTextFirstName.getText().toString(), mEditTextLastName.getText().toString(), mEditTextCarSize.getText().toString()));
                        user.child(User.getFirstNameLink()).setValue(mEditTextFirstName.getText().toString());
                        user.child(User.getLastNameLink()).setValue(mEditTextLastName.getText().toString());
                        user.child(User.getCarSizeLink()).setValue(mEditTextCarSize.getText().toString());
                    }
                    getActivity().finish();
                }
            });
            return rootView;
        }

        @Override
        protected void destroyView() {
            mEditTextCarSize = null;
            mEditTextFirstName = null;
            mEditTextLastName = null;
            mSaveLocationButtion = null;
        }
    }
}
