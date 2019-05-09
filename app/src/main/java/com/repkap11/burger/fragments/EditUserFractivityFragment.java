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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.repkap11.burger.BurgerApplication;
import com.repkap11.burger.R;
import com.repkap11.burger.activities.base.Fractivity;
import com.repkap11.burger.models.User;

/**
 * Created by paul on 8/8/17.
 */
public class EditUserFractivityFragment extends Fractivity.FractivityFragment {

    public static final String STARTING_INTENT_EDIT_EXISTING_USER = "com.repkap11.burger.STARTING_INTENT_EDIT_EXISTING_USER";
    private static final String TAG = EditUserFractivityFragment.class.getSimpleName();

    private EditText mEditTextCarSize;
    private Button mSaveLocationButtion;
    private EditText mEditTextDisplayName;

    private String mLunchGroup;
    private String mExistingUser;
    private boolean mFirstTimeShowing;

    @Override
    protected void create(Bundle savedInstanceState) {
        mFirstTimeShowing = true;
    }

    @Override
    protected void saveState(Bundle outState) {
        outState.putBoolean("mFirstTimeShowing", mFirstTimeShowing);
    }

    @Override
    protected void restoreState(@NonNull Bundle savedInstanceState) {
        mFirstTimeShowing = savedInstanceState.getBoolean ("mFirstTimeShowing");
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Intent startingIntent = getActivity().getIntent();
        if (startingIntent == null) {
            Log.e(TAG, "Somehow we want to start, but don't have a starting intent");
            getActivity().finish();
            return null;
        }
        mLunchGroup = BurgerApplication.getUserPerferedLunchGroup(getActivity());
        mExistingUser = startingIntent.getStringExtra(STARTING_INTENT_EDIT_EXISTING_USER);
        if (mLunchGroup == null) {
            Log.e(TAG, "Finishing because we don't have a lunch group");
            getActivity().finish();
        }

        View rootView = inflater.inflate(R.layout.fractivity_edit_user, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.fractivity_bar_menu_app_bar_layout);
        toolbar.setTitle(R.string.fractivity_edit_user_title);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_clear_black, null);
        toolbar.setNavigationIcon(drawable);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        mEditTextCarSize = (EditText) rootView.findViewById(R.id.fractivity_edit_user_edit_text_car_size);
        mEditTextDisplayName = (EditText) rootView.findViewById(R.id.fractivity_edit_user_edit_text_display_name);

        mEditTextDisplayName.requestFocus();

        if (mExistingUser != null && mFirstTimeShowing) {
            mEditTextCarSize.requestFocus();
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference user = database.getReference(mExistingUser);

            final DatabaseReference firstNameRef = user.child(User.getDisplayNameLink());
            final DatabaseReference carSizeRef = user.child(User.getCarSizeLink());

            firstNameRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    firstNameRef.removeEventListener(this);
                    if (mEditTextDisplayName != null) {
                        mEditTextDisplayName.setText(dataSnapshot.getValue(String.class));
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
                        Long carSize = dataSnapshot.getValue(Long.class);
                        String carSizeString;
                        if (carSize == null) {
                            carSizeString = "";
                        } else {
                            carSizeString = carSize.toString();
                        }
                        mEditTextCarSize.setText(carSizeString);
                        if (carSize != null) {
                            mEditTextCarSize.setSelection(carSizeString.length());
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
        mFirstTimeShowing = false;

        mSaveLocationButtion = (Button) rootView.findViewById(R.id.fractivity_edit_user_button_save);
        mSaveLocationButtion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference user;
                if (mExistingUser == null) {
                    DatabaseReference usersRef = database.getReference(mLunchGroup + "/users");
                    user = usersRef.push();
                    Long carSize;
                    try {
                        carSize = Long.parseLong(mEditTextCarSize.getText().toString());
                    } catch (NumberFormatException e) {
                        carSize = 0L;
                    }
                    user.setValue(new User(mEditTextDisplayName.getText().toString(), carSize));
                } else {
                    user = database.getReference(mExistingUser);
                    user.child(User.getDisplayNameLink()).setValue(mEditTextDisplayName.getText().toString());
                    String carSizeString = mEditTextCarSize.getText().toString();
                    Long carSize;
                    try {
                        carSize = Long.parseLong(carSizeString);
                    } catch (NumberFormatException e) {
                        carSize = 0L;
                    }
                    user.child(User.getCarSizeLink()).setValue(carSize);
                }
                getActivity().finish();
            }
        });
        return rootView;
    }

    @Override
    protected void destroyView() {
        mEditTextCarSize = null;
        mEditTextDisplayName = null;
        mSaveLocationButtion = null;
    }
}
