package com.repkap11.burger.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.repkap11.burger.BurgerApplication;
import com.repkap11.burger.LongClickDeleteDialogFragment;
import com.repkap11.burger.R;
import com.repkap11.burger.activities.UsersFractivity;
import com.repkap11.burger.activities.base.FirebaseAdapterFractivity;
import com.repkap11.burger.models.LunchLocation;
import com.repkap11.burger.models.LunchPreference;

import java.util.Calendar;

/**
 * Created by paul on 8/8/17.
 */
public class LunchLocationsTodayFractivityFragment extends FirebaseAdapterFractivity.FirebaseAdapterFragment {
    private static final String TAG = LunchLocationsTodayFractivityFragment.class.getSimpleName();
    public static final int REQUEST_CODE_PICK_LOCATION = 42;
    public static final String STARTING_INTENT_WHICH_DAY_INDEX = "com.repkap11.burger.STARTING_INTENT_WHICH_DAY_INDEX";
    public static final String STARTING_INTENT_WHICH_LUNCH_GROUP = "com.repkap11.burger.STARTING_INTENT_WHICH_LUNCH_GROUP";

    private ListView mListView;

    private String mLunchGroup;
    private int mDayInt;

    @Override
    protected void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        Intent startingIntent = getActivity().getIntent();
        if (startingIntent == null) {
            getActivity().finish();
        }
        mLunchGroup = BurgerApplication.getUserPerferedLunchGroup(getActivity());
        Log.e(TAG, "create: mLunchGroup:" + mLunchGroup);
        if (mLunchGroup == null) {
            getActivity().finish();
        }
        super.create(savedInstanceState);
    }

    @Override
    protected void onBackIconClick() {

    }

    @Override
    protected boolean getShowBackIcon() {
        return false;
    }

    @Override
    protected void onFabClick() {
    }

    @Override
    protected boolean getShowFab() {
        return false;
    }

    @Override
    public String getBarTitleString(Context context) {
        Calendar calendar = Calendar.getInstance();
        mDayInt = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        //mDayInt = 1;
        Log.e(TAG, "my day:" + mDayInt);
        String dayOfWeek = context.getResources().getStringArray(R.array.fractivity_day_label)[mDayInt];
        String lunch = context.getResources().getString(R.string.fractivity_lunch_day_of_week);
        return lunch + dayOfWeek;
        //return context.getResources().getString(R.string.fractivity_lunch_locations_today_title);
    }

    //Using this activity view
    @Override
    protected View createAdapterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, boolean attachToRoot) {
        View rootView = inflater.inflate(R.layout.fractivity_lunch_locations_today, container, attachToRoot);
        mListView = (ListView) rootView.findViewById(R.id.fractivity_lunch_locations_today_list);
        return rootView;
    }

    @Override
    protected void destroyView() {
        mListView = null;
        super.destroyView();
    }

    //Put this data
    @Override
    protected String adapterReference() {
        return mLunchGroup + "/lunch_locations";
    }

    //With this filter
    @Override
    protected Query getQuery(DatabaseReference databaseRef) {
        return databaseRef.orderByValue();
    }

    //Into list listview
    @Override
    protected AbsListView getListView(View rootView) {
        return mListView;
    }

    //Where each element uses this view
    @Override
    public int getListResource() {
        return R.layout.fractivity_lunch_locations_today_list_element;
    }

    //And that view has a holder caching position and subviews
    @Override
    public Holder populateHolder(View convertView) {
        Holder holder = new Holder();
        holder.mName = (TextView) convertView.findViewById(R.id.fractivity_lunch_locations_today_list_element_text_name);
        holder.mNumPeople = (TextView) convertView.findViewById(R.id.fractivity_lunch_locations_today_list_element_text_people);
        return holder;
    }

    //And each subview is populated with data
    @Override
    public void populateView(View convertView, Object o, int position, String key, Object value) {
        Holder holder = (Holder) o;
        LunchLocation location = (LunchLocation) value;
        LunchPreference todaysPreference = null;
        Log.e(TAG, "Days Int:" + mDayInt);
        switch (mDayInt) {
            case 1:
                todaysPreference = location.lunch_preference_1;
                break;
            case 2:
                todaysPreference = location.lunch_preference_2;
                break;
            case 3:
                todaysPreference = location.lunch_preference_3;
                break;
            case 4:
                todaysPreference = location.lunch_preference_4;
                break;
            case 5:
                todaysPreference = location.lunch_preference_5;
                break;
        }
        String numUsersString;
        if (todaysPreference == null) {
            numUsersString = getResources().getQuantityString(R.plurals.fractivity_lunch_locations_today_users_plural, 0, 0);
        } else {
            numUsersString = getResources().getQuantityString(R.plurals.fractivity_lunch_locations_today_users_plural, todaysPreference.usersSize(), todaysPreference.usersSize());
        }
        holder.mName.setText(location.displayName);
        holder.mNumPeople.setText(numUsersString);
        Log.e(TAG, "Using string:" + todaysPreference + holder.mName.getText().toString());
        holder.mIndex = position;
    }

    @Override
    public Class getAdapterDataClass() {
        return LunchLocation.class;
    }

    @Override
    protected void onItemClicked(View view, Object holderObject, int position, String key, String link, Object value) {
        LunchLocation location = (LunchLocation) value;
        Holder holder = (Holder) holderObject;
        Intent startingIntent = getActivity().getIntent();
        if (startingIntent == null) {
            Log.e(TAG, "Somehow we want to give a result, but don't have a starting intent");
            getActivity().finish();
            return;
        }
        showUsersOfLocation(link, mDayInt, holder.mName.getText().toString() + " on " + getResources().getStringArray(R.array.fractivity_day_label)[mDayInt]);
    }

    private void showUsersOfLocation(String lunchPreferenceKey, int i, String title) {
        if (lunchPreferenceKey == null) {
            return;
        }
        String userKey = BurgerApplication.getUserKey(getActivity());

        DatabaseReference lunchGroupRef = FirebaseDatabase.getInstance().getReference(userKey).getParent().getParent();
        String lunchGroupKey = lunchGroupRef.toString().substring(lunchGroupRef.getRoot().toString().length() + 1);
        String lunchLocationUsersKey = lunchGroupKey + "/lunch_locations/" + lunchPreferenceKey + "/lunch_preference_" + i;
        Log.e(TAG, "Showing subgroup:" + lunchLocationUsersKey + " group:" + lunchGroupKey);
        Intent intent = new Intent(getContext(), UsersFractivity.class);
        intent.putExtra(UsersFractivityFragment.STARTING_INTENT_WHICH_USERS_SUB_GROUP, lunchLocationUsersKey);
        intent.putExtra(UsersFractivityFragment.STARTING_INTENT_WHICH_USERS_GROUP, lunchGroupKey);
        intent.putExtra(UsersFractivityFragment.STARTING_INTENT_TITLE, title);
        intent.putExtra(UsersFractivityFragment.STARTING_INTENT_SHOW_ILL_DRIVE, false);


        startActivityForResult(intent, UsersFractivityFragment.REQUEST_CODE_LIST_USERS);
    }


    @Override
    protected boolean onItemLongClicked(View view, Object holderObject, int position, final String key, String link, Object value) {
        return false;
    }

    public static class Holder {
        public TextView mName;
        public int mIndex;
        public TextView mNumPeople;
    }

}
