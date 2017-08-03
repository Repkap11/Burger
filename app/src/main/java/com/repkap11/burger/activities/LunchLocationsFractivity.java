package com.repkap11.burger.activities;

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
import com.repkap11.burger.FirebaseAdapter;
import com.repkap11.burger.R;
import com.repkap11.burger.activities.base.FirebaseAdapterFractivity;
import com.repkap11.burger.models.LunchLocation;


public class LunchLocationsFractivity extends FirebaseAdapterFractivity<LunchLocationsFractivity.Holder, LunchLocation> {

    private static final String TAG = LunchLocationsFractivity.class.getSimpleName();

    public static final int REQUEST_CODE_PICK_LOCATION = 42;
    public static final String STARTING_INTENT_LOCATION_INDEX = "com.repkap11.burger.STARTING_INTENT_LOCATION_INDEX";
    private String mResultIndex;

    @Override
    protected FirebaseAdapterFragment createFirebaseFragment() {
        Intent startingIntent = getIntent();
        if (startingIntent == null) {
            finish();
        }
        return new LunchLocationFragment<LunchLocationsFractivity.Holder>();
    }

    public static class LunchLocationFragment<AdapterHolder> extends FirebaseAdapterFractivity.FirebaseAdapterFragment {
        private ListView mListView;
        private FloatingActionButton mFab;

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
                    startActivity(new Intent(getActivity(), AddLunchLocationFractivity.class));
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
            return "lunch_locations";
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
        protected void onItemClicked(View view, Object o, int position, String key, Object value) {
            LunchLocation location = (LunchLocation) value;
            Holder holder = (Holder) o;
            Intent startingIntent = getActivity().getIntent();
            if (startingIntent == null) {
                Log.e(TAG, "Somehow we want to give a result, but don't have a starting intent");
                getActivity().finish();
                return;
            }
            int resultIndex = startingIntent.getIntExtra(STARTING_INTENT_LOCATION_INDEX, -1);

            Intent intent = new Intent();
            intent.putExtra(AboutUserFractivity.RESULT_INTENT_LOCATION_KEY, key);
            intent.putExtra(AboutUserFractivity.RESULT_INTENT_LOCATION_INDEX, resultIndex);

            getActivity().setResult(RESULT_OK, intent);
            getActivity().finish();
        }

    }

    public static class Holder {
        public TextView mName;
        public int mIndex;
    }
}
