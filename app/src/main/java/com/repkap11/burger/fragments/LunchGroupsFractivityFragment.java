package com.repkap11.burger.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.repkap11.burger.BurgerApplication;
import com.repkap11.burger.R;
import com.repkap11.burger.activities.AboutUserFractivity;
import com.repkap11.burger.activities.AddLunchGroupFractivity;
import com.repkap11.burger.activities.base.FirebaseAdapterFractivity;
import com.repkap11.burger.models.LunchGroup;
import com.repkap11.burger.models.User;

/**
 * Created by paul on 8/8/17.
 */
public class LunchGroupsFractivityFragment extends FirebaseAdapterFractivity.FirebaseAdapterFragment {

    private static final String TAG = LunchGroupsFractivityFragment.class.getSimpleName();

    @Override
    protected void create(Bundle savedInstanceState) {
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
        startActivity(new Intent(getActivity(), AddLunchGroupFractivity.class));
    }

    @Override
    protected boolean getShowFab() {
        return true;
    }

    @Override
    protected int getBarTitleResource() {
        return R.string.fractivity_lunch_groups_title;
    }

    private ListView mListView;
    private FloatingActionButton mFab;

    //Using this activity view
    @Override
    protected View createAdapterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fractivity_lunch_groups, container, false);
        mListView = (ListView) rootView.findViewById(R.id.fractivity_lunch_groups_list);
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
        return "lunch_groups";
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
        return R.layout.fractivity_lunch_groups_list_element;
    }

    //And that view has a holder caching position and subviews
    @Override
    public Holder populateHolder(View convertView) {
        Holder holder = new Holder();
        holder.mName = (TextView) convertView.findViewById(R.id.fractivity_lunch_groups_list_element_text);
        return holder;
    }

    //And each subview is populated with data
    @Override
    public void populateView(View convertView, Object o, int position, String key, Object value) {
        Holder holder = (Holder) o;
        LunchGroup lunchGroup = (LunchGroup) value;
        holder.mName.setText(lunchGroup.displayName);
        holder.mIndex = position;
    }

    @Override
    public Class getAdapterDataClass() {
        return LunchGroup.class;
    }

    @Override
    protected void onItemClicked(View view, Object holderObject, int position, String key, String link, Object value) {
        Intent intent = new Intent(getContext(), AboutUserFractivity.class);
        Log.e(TAG, "Starting with group:" + key);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Log.e(TAG, "Unexpected null user");
            getActivity().finish();
            return;
        }
        BurgerApplication.setUserPerferedLunchGroup(LunchGroupsFractivityFragment.this.getActivity(), key);
        String userKey = BurgerApplication.getUserKey(getActivity());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference uderRef = database.getReference(BurgerApplication.getUserKey(getActivity()));
        uderRef.child(User.getDisplayNameLink()).setValue(user.getDisplayName());
        BurgerApplication.updateDeviceToken(getActivity(), true);

        Log.e(TAG, "Writing user's prefered group:" + key);
        intent.putExtra(AboutUserFractivityFragment.STARTING_INTENT_USER_INITIAL_NAME, user.getDisplayName());
        intent.putExtra(AboutUserFractivityFragment.STARTING_INTENT_USER_KEY, userKey);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
    }

    public static class Holder {
        public TextView mName;
        public int mIndex;
    }
}
