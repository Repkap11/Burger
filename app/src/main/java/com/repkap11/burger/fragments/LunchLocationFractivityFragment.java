package com.repkap11.burger.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
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
import com.repkap11.burger.activities.AddLunchLocationFractivity;
import com.repkap11.burger.activities.base.FirebaseAdapterFractivity;
import com.repkap11.burger.models.LunchLocation;

/**
 * Created by paul on 8/8/17.
 */
public class LunchLocationFractivityFragment extends FirebaseAdapterFractivity.FirebaseAdapterFragment {
    private static final String TAG = LunchLocationFractivityFragment.class.getSimpleName();
    public static final int REQUEST_CODE_PICK_LOCATION = 42;
    public static final String STARTING_INTENT_LOCATION_INDEX = "com.repkap11.burger.STARTING_INTENT_LOCATION_INDEX";
    public static final String STARTING_INTENT_WHICH_LUNCH_GROUP = "com.repkap11.burger.STARTING_INTENT_WHICH_LUNCH_GROUP";

    private ListView mListView;
    private FloatingActionButton mFab;

    private String mLunchGroup;

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

    //Using this activity view
    @Override
    protected View createAdapterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fractivity_lunch_locations, container, false);
        mListView = (ListView) rootView.findViewById(R.id.fractivity_lunch_locations_list);
        //mAddLocationButton = (Button) rootView.findViewById(R.id.fractivity_lunch_locations_button_add_user);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.fractivity_lunch_locations_title);
        mFab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddLunchLocationFractivity.class);
                intent.putExtra(AddLunchLocationFractivityFragment.STARTING_INTENT_WHICH_LUNCH_GROUP, mLunchGroup);
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
        return R.layout.fractivity_lunch_locations_list_element;
    }

    //And that view has a holder caching position and subviews
    @Override
    public Holder populateHolder(View convertView) {
        Holder holder = new Holder();
        holder.mName = (TextView) convertView.findViewById(R.id.fractivity_lunch_locations_list_element_text);
        return holder;
    }

    //And each subview is populated with data
    @Override
    public void populateView(View convertView, Object o, int position, String key, Object value) {
        Holder holder = (Holder) o;
        LunchLocation location = (LunchLocation) value;
        holder.mName.setText(location.displayName);
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
        int resultIndex = startingIntent.getIntExtra(STARTING_INTENT_LOCATION_INDEX, -1);

        Intent intent = new Intent();
        intent.putExtra(AboutUserFractivityFragment.RESULT_INTENT_LOCATION_LINK, link);
        intent.putExtra(AboutUserFractivityFragment.RESULT_INTENT_LOCATION_INDEX, resultIndex);

        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    protected boolean onItemLongClicked(View view, Object holderObject, int position, final String key, String link, Object value) {
        LunchLocation location = (LunchLocation) value;
        Holder holder = (Holder) holderObject;
        DialogFragment df = new LongClickDeleteDialogFragment();
        Bundle args = new Bundle();
        args.putString(LongClickDeleteDialogFragment.ARG_TITLE, "Delete Location");
        args.putString(LongClickDeleteDialogFragment.ARG_MESSAGE, "Are you sure you want to delete location \"" + location.displayName + "\"");
        args.putString(LongClickDeleteDialogFragment.ARG_KEY, key);
        df.setArguments(args);
        df.show(getActivity().getSupportFragmentManager(), "MyDF");
        return true;
    }

    public static class Holder {
        public TextView mName;
        public int mIndex;
    }

}
