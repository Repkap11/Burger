package com.repkap11.burger;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;


public class LunchLocationsActivity extends FirebaseAdapterFractivity<LunchLocationsActivity.Holder> {

    @Override
    protected FirebaseAdapterFragment createFirebaseFragment() {
        return new LunchLocationFragment<LunchLocationsActivity.Holder>();
    }

    public static class LunchLocationFragment<AdapterHolder> extends FirebaseAdapterFractivity.FirebaseAdapterFragment {

        private ListView mListView;
        private Button mAddLocationButton;

        //Using this activity view
        @Override
        protected View createAdapterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fractivity_lunch_locations, container, false);
            mListView = (ListView) rootView.findViewById(R.id.fractivity_lunch_locations_list);
            //mAddLocationButton = (Button) rootView.findViewById(R.id.fractivity_lunch_locations_button_add_user);
            Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
            toolbar.setTitle(R.string.activity_lunch_locations_title);
            return rootView;
        }

        @Override
        protected void destroyView() {
            mListView = null;
            mAddLocationButton = null;
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
            return R.layout.fractivity_firebase_adapter_list_element;
        }


        //And that view has a holder caching position and subviews
        @Override
        public Holder populateHolder(View convertView) {
            Holder holder = new Holder();
            holder.mName = (TextView) convertView.findViewById(R.id.fractivity_firebase_adapter_list_element_text);
            return holder;
        }

        //And each subview is populated with data
        @Override
        public void populateView(View convertView, Object o, int position, FirebaseAdapter.AdapterData data) {
            Holder holder = (Holder) o;
            holder.mName.setText(data.key + ":" + data.value);
            holder.mIndex = position;
        }
    }

    public static class Holder {
        public TextView mName;
        public int mIndex;
    }
}
