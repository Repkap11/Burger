package com.repkap11.burger.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.repkap11.burger.BurgerApplication;
import com.repkap11.burger.R;
import com.repkap11.burger.activities.EditUserFractivity;
import com.repkap11.burger.activities.LunchLocationsFractivity;
import com.repkap11.burger.activities.base.BarMenuFractivity;
import com.repkap11.burger.models.LunchLocation;
import com.repkap11.burger.models.User;

/**
 * Created by paul on 8/8/17.
 */
public class AboutUserFractivityFragment extends BarMenuFractivity.BarMenuFractivityFragment {

    private static final String TAG = AboutUserFractivityFragment.class.getSimpleName();
    public static final String STARTING_INTENT_USER_INITIAL_NAME = "com.repkap11.burger.STARTING_INTENT_USER_INITIAL_NAME";
    public static final String STARTING_INTENT_USER_KEY = "com.repkap11.burger.STARTING_INTENT_USER_KEY";

    public static final String RESULT_INTENT_LOCATION_LINK = "com.repkap11.burger.RESULT_INTENT_LOCATION_LINK";
    public static final String RESULT_INTENT_LOCATION_INDEX = "com.repkap11.burger.RESULT_INTENT_LOCATION_INDEX";

    private TextView mTextFullName;
    private TextView mEditTextCarSize;
    private Button mButtonEditUser;
    //private Toolbar mToolbar;
    private TextView mLunchChoiceLabel1;
    private TextView mLunchChoiceLabel2;
    private TextView mLunchChoiceLabel3;
    private TextView mLunchChoiceLabel4;
    private TextView mLunchChoiceLabel5;
    private DatabaseReference mLunchPreference1Ref;
    private DatabaseReference mLunchPreference2Ref;
    private DatabaseReference mLunchPreference3Ref;
    private DatabaseReference mLunchPreference4Ref;
    private DatabaseReference mLunchPreference5Ref;
    private String mLunchPreference1Key = null;
    private String mLunchPreference2Key = null;
    private String mLunchPreference3Key = null;
    private String mLunchPreference4Key = null;
    private String mLunchPreference5Key = null;

    private String mUserKey;

    @Override
    protected void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        Intent startingIntent = getActivity().getIntent();
        if (startingIntent == null) {
            Log.e(TAG, "Somehow we want to start, but don't have a starting intent");
            getActivity().finish();
            return;
        }
        mUserKey = startingIntent.getStringExtra(STARTING_INTENT_USER_KEY);

        if (mUserKey == null) {
            mUserKey = BurgerApplication.getUserKey(getActivity());
            Log.e(TAG, "Using default user because a user key was not spesified");
            //getActivity().finish();
        }
    }

    @Override
    protected void onBackIconClick() {
        getActivity().finish();
    }

    @Override
    protected boolean getShowBackIcon() {
        return true;
    }

    @Override
    protected void onFabClick() {

    }

    @Override
    protected boolean getShowFab() {
        return false;
    }

    @Override
    protected int getBarTitleResource() {
        return R.string.fractivity_about_user_title;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == LunchLocationsFractivityFragment.REQUEST_CODE_PICK_LOCATION && data != null) {
            Log.e(TAG, "Good result");
            Intent startingIntent = getActivity().getIntent();
            if (startingIntent == null) {
                Log.e(TAG, "Exiting result because starting intent is gone");
                getActivity().finish();
            }
            int location_index = data.getIntExtra(RESULT_INTENT_LOCATION_INDEX, -1);
            String location_link = data.getStringExtra(RESULT_INTENT_LOCATION_LINK);
            if (location_index == -1) {
                Log.e(TAG, "Error, location_index is invalid (-1) not adding result location");
                return;
            }

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference userRef = database.getReference(mUserKey + "/lunch_preference_" + location_index);
            userRef.setValue(location_link);
        }
    }

    @Override
    protected View createBarView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fractivity_about_user, container, false);
        mTextFullName = (TextView) rootView.findViewById(R.id.fractivity_about_user_text_full_name);
        mEditTextCarSize = (TextView) rootView.findViewById(R.id.fractivity_about_user_text_car_size);
        mButtonEditUser = (Button) rootView.findViewById(R.id.fractivity_about_user_edit_user);
        mButtonEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditUserFractivity.class);
                intent.putExtra(EditUserFractivityFragment.STARTING_INTENT_EDIT_EXISTING_USER, mUserKey);
                startActivity(intent);
            }
        });


        View rootDay1 = rootView.findViewById(R.id.fractivity_about_user_day_1);
        View rootDay2 = rootView.findViewById(R.id.fractivity_about_user_day_2);
        View rootDay3 = rootView.findViewById(R.id.fractivity_about_user_day_3);
        View rootDay4 = rootView.findViewById(R.id.fractivity_about_user_day_4);
        View rootDay5 = rootView.findViewById(R.id.fractivity_about_user_day_5);

        TextView dayLabel1 = (TextView) rootDay1.findViewById(R.id.fractivity_about_user_day_label);
        TextView dayLabel2 = (TextView) rootDay2.findViewById(R.id.fractivity_about_user_day_label);
        TextView dayLabel3 = (TextView) rootDay3.findViewById(R.id.fractivity_about_user_day_label);
        TextView dayLabel4 = (TextView) rootDay4.findViewById(R.id.fractivity_about_user_day_label);
        TextView dayLabel5 = (TextView) rootDay5.findViewById(R.id.fractivity_about_user_day_label);

        dayLabel1.setText(getResources().getText(R.string.fractivity_about_user_day_label1));
        dayLabel2.setText(getResources().getText(R.string.fractivity_about_user_day_label2));
        dayLabel3.setText(getResources().getText(R.string.fractivity_about_user_day_label3));
        dayLabel4.setText(getResources().getText(R.string.fractivity_about_user_day_label4));
        dayLabel5.setText(getResources().getText(R.string.fractivity_about_user_day_label5));

        mLunchChoiceLabel1 = (TextView) rootDay1.findViewById(R.id.fractivity_about_user_lunch_location_label);
        mLunchChoiceLabel2 = (TextView) rootDay2.findViewById(R.id.fractivity_about_user_lunch_location_label);
        mLunchChoiceLabel3 = (TextView) rootDay3.findViewById(R.id.fractivity_about_user_lunch_location_label);
        mLunchChoiceLabel4 = (TextView) rootDay4.findViewById(R.id.fractivity_about_user_lunch_location_label);
        mLunchChoiceLabel5 = (TextView) rootDay5.findViewById(R.id.fractivity_about_user_lunch_location_label);

        mLunchChoiceLabel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAddLocation(1, mLunchPreference1Key);
            }
        });
        mLunchChoiceLabel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAddLocation(2, mLunchPreference2Key);
            }
        });
        mLunchChoiceLabel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAddLocation(3, mLunchPreference3Key);
            }
        });
        mLunchChoiceLabel4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAddLocation(4, mLunchPreference4Key);
            }
        });
        mLunchChoiceLabel5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAddLocation(5, mLunchPreference5Key);
            }
        });

        Intent startingIntent = getActivity().getIntent();
        if (startingIntent == null) {
            getActivity().finish();
        }
        String initialName = startingIntent.getStringExtra(STARTING_INTENT_USER_INITIAL_NAME);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference userRef = database.getReference(mUserKey);
        userRef.addValueEventListener(new ValueEventListener() {
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
                if (mTextFullName == null) {
                    return;
                }
                mTextFullName.setText(user.displayName);
                mEditTextCarSize.setText(user.carSize);

                if (mLunchPreference1Ref != null) {
                    mLunchPreference1Ref.removeEventListener(mLunchPreference1Listener);
                }
                if (mLunchPreference2Ref != null) {
                    mLunchPreference2Ref.removeEventListener(mLunchPreference2Listener);
                }
                if (mLunchPreference3Ref != null) {
                    mLunchPreference3Ref.removeEventListener(mLunchPreference3Listener);
                }
                if (mLunchPreference4Ref != null) {
                    mLunchPreference4Ref.removeEventListener(mLunchPreference4Listener);
                }
                if (mLunchPreference5Ref != null) {
                    mLunchPreference5Ref.removeEventListener(mLunchPreference5Listener);
                }
                mLunchPreference1Ref = database.getReference(mUserKey).getParent().getParent().child("lunch_locations/" + user.lunch_preference_1);
                mLunchPreference2Ref = database.getReference(mUserKey).getParent().getParent().child("lunch_locations/" + user.lunch_preference_2);
                mLunchPreference3Ref = database.getReference(mUserKey).getParent().getParent().child("lunch_locations/" + user.lunch_preference_3);
                mLunchPreference4Ref = database.getReference(mUserKey).getParent().getParent().child("lunch_locations/" + user.lunch_preference_4);
                mLunchPreference5Ref = database.getReference(mUserKey).getParent().getParent().child("lunch_locations/" + user.lunch_preference_5);
                Log.e(TAG, "Watching pref1 based:" + database.getReference(mUserKey).toString());
                Log.e(TAG, "Watching pref1 ref:" + mLunchPreference1Ref.toString());

                mLunchPreference1Ref.addValueEventListener(mLunchPreference1Listener);
                mLunchPreference2Ref.addValueEventListener(mLunchPreference2Listener);
                mLunchPreference3Ref.addValueEventListener(mLunchPreference3Listener);
                mLunchPreference4Ref.addValueEventListener(mLunchPreference4Listener);
                mLunchPreference5Ref.addValueEventListener(mLunchPreference5Listener);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return rootView;
    }

    private void launchAddLocation(int i, String lunch_pref_key) {
        Intent intent = new Intent(getContext(), LunchLocationsFractivity.class);
        intent.putExtra(LunchLocationsFractivityFragment.STARTING_INTENT_LOCATION_INDEX, i);
        DatabaseReference lunchGroupRef = FirebaseDatabase.getInstance().getReference(mUserKey).getParent().getParent();
        String lunchGroupKey = lunchGroupRef.toString().substring(lunchGroupRef.getRoot().toString().length() + 1);
        intent.putExtra(AddLunchLocationFractivityFragment.STARTING_INTENT_WHICH_LUNCH_GROUP, lunchGroupKey);
        startActivityForResult(intent, LunchLocationsFractivityFragment.REQUEST_CODE_PICK_LOCATION);
    }


    private ValueEventListener mLunchPreference1Listener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            LunchLocation location = null;
            try {
                location = dataSnapshot.getValue(LunchLocation.class);
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
            if (location == null) {
                return;
            }
            mLunchChoiceLabel1.setText(location.displayName);
            mLunchPreference1Key = dataSnapshot.getKey();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    private ValueEventListener mLunchPreference2Listener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            LunchLocation location = null;
            try {
                location = dataSnapshot.getValue(LunchLocation.class);
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
            if (location == null) {
                return;
            }
            mLunchChoiceLabel2.setText(location.displayName);
            mLunchPreference2Key = dataSnapshot.getKey();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    private ValueEventListener mLunchPreference3Listener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            LunchLocation location = null;
            try {
                location = dataSnapshot.getValue(LunchLocation.class);
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
            if (location == null) {
                return;
            }
            mLunchChoiceLabel3.setText(location.displayName);
            mLunchPreference3Key = dataSnapshot.getKey();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    private ValueEventListener mLunchPreference4Listener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            LunchLocation location = null;
            try {
                location = dataSnapshot.getValue(LunchLocation.class);
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
            if (location == null) {
                return;
            }
            mLunchChoiceLabel4.setText(location.displayName);
            mLunchPreference4Key = dataSnapshot.getKey();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    private ValueEventListener mLunchPreference5Listener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            LunchLocation location = null;
            try {
                location = dataSnapshot.getValue(LunchLocation.class);
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
            if (location == null) {
                return;
            }
            mLunchChoiceLabel5.setText(location.displayName);
            mLunchPreference5Key = dataSnapshot.getKey();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    protected void destroyView() {
        if (mLunchPreference1Ref != null) {
            mLunchPreference1Ref.removeEventListener(mLunchPreference1Listener);
        }
        if (mLunchPreference2Ref != null) {
            mLunchPreference2Ref.removeEventListener(mLunchPreference2Listener);
        }
        if (mLunchPreference3Ref != null) {
            mLunchPreference3Ref.removeEventListener(mLunchPreference3Listener);
        }
        if (mLunchPreference4Ref != null) {
            mLunchPreference4Ref.removeEventListener(mLunchPreference4Listener);
        }
        if (mLunchPreference5Ref != null) {
            mLunchPreference5Ref.removeEventListener(mLunchPreference5Listener);
        }
        mEditTextCarSize = null;
        mTextFullName = null;
        mButtonEditUser = null;
        mLunchChoiceLabel1 = null;
        mLunchChoiceLabel2 = null;
        mLunchChoiceLabel3 = null;
        mLunchChoiceLabel4 = null;
        mLunchChoiceLabel5 = null;
    }
}
