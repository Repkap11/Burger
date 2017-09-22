package com.repkap11.burger.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.repkap11.burger.R;
import com.repkap11.burger.activities.EditUserFractivity;
import com.repkap11.burger.activities.base.FirebaseAdapter2Fractivity;
import com.repkap11.burger.models.User;

/**
 * Created by paul on 8/8/17.
 */
public class UsersFractivityFragment extends FirebaseAdapter2Fractivity.FirebaseAdapter2Fragment<UsersFractivityFragment.Holder, User> {

    public static final String STARTING_INTENT_WHICH_USERS_SUB_GROUP = "com.repkap11.burger.STARTING_INTENT_WHICH_USERS_SUB_GROUP";
    public static final String STARTING_INTENT_WHICH_USERS_GROUP = "com.repkap11.burger.STARTING_INTENT_WHICH_USERS_GROUP";
    public static final String STARTING_INTENT_TITLE = "com.repkap11.burger.STARTING_INTENT_TITLE";


    private static final String TAG = UsersFractivityFragment.class.getSimpleName();
    public static final int REQUEST_CODE_LIST_USERS = 1;
    private String mLunchGroup;
    private String mLunchSubGroup;
    private String mTitleString;

    @Override
    protected void create(Bundle savedInstanceState) {

        if (getActivity().getIntent() == null) {
            getActivity().finish();
        }
        mLunchSubGroup = getActivity().getIntent().getStringExtra(STARTING_INTENT_WHICH_USERS_SUB_GROUP);
        mLunchGroup = getActivity().getIntent().getStringExtra(STARTING_INTENT_WHICH_USERS_GROUP);
        mTitleString = getActivity().getIntent().getStringExtra(STARTING_INTENT_TITLE);
        if (mTitleString == null){
            mTitleString = getResources().getString(R.string.fractivity_users_title);
        }


        //mLunchGroup = BurgerApplication.getUserPerferedLunchGroup(getActivity());
        Log.e(TAG, "create: mLunchGroup:" + mLunchGroup);
        if (mLunchGroup == null || mLunchSubGroup == null) {
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
        Intent intent = new Intent(getActivity(), EditUserFractivity.class);
        startActivity(intent);
    }

    @Override
    protected boolean getShowFab() {
        return false;
    }

    @Override
    public String getBarTitleString(Context context) {
        return mTitleString;
    }
    private ListView mListView;

    //Using this activity view
    @Override
    protected View createAdapterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, boolean attachToRoot) {
        View rootView = inflater.inflate(R.layout.fractivity_users, container, attachToRoot);
        mListView = (ListView) rootView.findViewById(R.id.fractivity_users_list);
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
        Log.e(TAG, "Getting adapter with group:" + mLunchSubGroup);
        return mLunchSubGroup + "/users";
    }

    @Override
    protected String adapter2Reference() {
        Log.e(TAG, "Getting adapter2 with group:" + mLunchGroup);
        return mLunchGroup + "/users";
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
        return R.layout.fractivity_users_list_element;
    }

    //And that view has a holder caching position and subviews
    @Override
    public Holder populateHolder(View convertView) {
        Holder holder = new Holder();
        holder.mName = (TextView) convertView.findViewById(R.id.fractivity_users_list_element_text);
        return holder;
    }

    @Override
    protected void populateView2(View convertView, Holder holder, int position, String key, User user) {
        holder.mName.setText(user.displayName);
        holder.mIndex = position;
    }

    @Override
    public Class<User> getAdapter2DataClass() {
        return User.class;
    }

    @Override
    protected void onItem2Clicked(View view, Holder holder, int position, String key, String link, User user) {
        //Intent intent = new Intent(getActivity(), AboutUserFractivity.class);
        //intent.putExtra(AboutUserFractivity.STARTING_INTENT_USER_INITIAL_NAME, user.firstName);
        //intent.putExtra(AboutUserFractivity.STARTING_INTENT_USER_KEY, key);

//        Intent intent = new Intent(getActivity(), EditUserFractivity.class);
//        intent.putExtra(EditUserFractivityFragment.STARTING_INTENT_EDIT_EXISTING_USER, key);
//        startActivity(intent);
    }

    public static class Holder {
        public TextView mName;
        public int mIndex;
    }
}
