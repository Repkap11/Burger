package com.repkap11.burger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
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
import com.repkap11.burger.models.User;


public class UsersFractivity extends FirebaseAdapterFractivity<UsersFractivity.Holder, User> {

    @Override
    protected FirebaseAdapterFragment createFirebaseFragment() {
        return new LunchLocationFragment<UsersFractivity.Holder>();
    }

    public static class LunchLocationFragment<AdapterHolder> extends FirebaseAdapterFragment {


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
                    startActivity(new Intent(getActivity(), AddUserFractivity.class));
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
            return "users";
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
        public void populateView(View convertView, Object o, int position, String key, Object value) {
            Holder holder = (Holder) o;
            User user = (User) value;
            holder.mName.setText(key + ":" + user.firstName + " " + user.lastName + " with a " + user.carSize + " seat car.");
            holder.mIndex = position;
        }

        @Override
        public Class getAdapterDataClass() {
            return User.class;
        }
    }

    public static class Holder {
        public TextView mName;
        public int mIndex;
    }
}
