package com.repkap11.burger.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.repkap11.burger.BurgerApplication;
import com.repkap11.burger.R;
import com.repkap11.burger.activities.EditUserFractivity;
import com.repkap11.burger.activities.base.FirebaseAdapterFractivity;
import com.repkap11.burger.models.User;

/**
 * Created by paul on 8/8/17.
 */
public class UsersFractivityFragment extends FirebaseAdapterFractivity.FirebaseAdapterFragment {

    public static final String STARTING_INTENT_WHICH_LUNCH_GROUP = "com.repkap11.burger.STARTING_INTENT_WHICH_LUNCH_GROUP";
    private static final String TAG = UsersFractivityFragment.class.getSimpleName();
    private String mLunchGroup;

    @Override
    protected void create(Bundle savedInstanceState) {
        mLunchGroup = BurgerApplication.getUserPerferedLunchGroup(getActivity());
        Log.e(TAG, "create: mLunchGroup:" + mLunchGroup);
        if (mLunchGroup == null) {
            getActivity().finish();
        }
        super.create(savedInstanceState);
    }

    private ListView mListView;
    private FloatingActionButton mFab;

    //Using this activity view
    @Override
    protected View createAdapterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fractivity_users, container, false);
        mListView = (ListView) rootView.findViewById(R.id.fractivity_users_list);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.fractivity_users_title);
        mFab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditUserFractivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    protected void destroyView() {
        mListView = null;
        mFab = null;
        super.destroyView();
    }

    //Put this data
    @Override
    protected String adapterReference() {
        Log.e(TAG, "Getting adapter with group:" + mLunchGroup);
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

    //And each subview is populated with data
    @Override
    public void populateView(View convertView, Object o, int position, String key, Object value) {
        Holder holder = (Holder) o;
        User user = (User) value;
        holder.mName.setText(user.displayName);
        holder.mIndex = position;
    }

    @Override
    public Class getAdapterDataClass() {
        return User.class;
    }

    @Override
    protected void onItemClicked(View view, Object holderObject, int position, String key, String link, Object value) {
        User user = (User) value;
        Holder holder = (Holder) holderObject;
        //Intent intent = new Intent(getActivity(), AboutUserFractivity.class);
        //intent.putExtra(AboutUserFractivity.STARTING_INTENT_USER_INITIAL_NAME, user.firstName);
        //intent.putExtra(AboutUserFractivity.STARTING_INTENT_USER_KEY, key);

        Intent intent = new Intent(getActivity(), EditUserFractivity.class);
        intent.putExtra(EditUserFractivityFragment.STARTING_INTENT_EDIT_EXISTING_USER, key);
        startActivity(intent);
    }

    public static class Holder {
        public TextView mName;
        public int mIndex;
    }
}
