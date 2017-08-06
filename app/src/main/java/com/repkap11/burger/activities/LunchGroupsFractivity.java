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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.repkap11.burger.BurgerApplication;
import com.repkap11.burger.R;
import com.repkap11.burger.activities.base.FirebaseAdapterFractivity;
import com.repkap11.burger.models.LunchGroup;


public class LunchGroupsFractivity extends FirebaseAdapterFractivity<LunchGroupsFractivity.Holder, LunchGroup> {
    private static final String TAG = LunchGroupsFractivity.class.getSimpleName();

    @Override
    protected FirebaseAdapterFragment createFirebaseFragment() {
        return new LunchGroupFragment<Holder>();
    }

    public static class LunchGroupFragment<AdapterHolder> extends FirebaseAdapterFragment {

        @Override
        protected void create(Bundle savedInstanceState) {
            super.create(savedInstanceState);
        }

        private ListView mListView;
        private FloatingActionButton mFab;

        //Using this activity view
        @Override
        protected View createAdapterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fractivity_lunch_groups, container, false);
            mListView = (ListView) rootView.findViewById(R.id.fractivity_lunch_groups_list);
            Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
            toolbar.setTitle(R.string.fractivity_lunch_groups_title);
            mFab = (FloatingActionButton) rootView.findViewById(R.id.fab);
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), AddLunchGroupFractivity.class));
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
            Intent intent = new Intent(getContext(), UsersFractivity.class);
            Log.e(TAG, "Starting with group:" + key);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                Log.e(TAG, "Unexpected null user");
                getActivity().finish();
                return;
            }
            BurgerApplication.writeUserPerferedGroup(LunchGroupFragment.this.getActivity(), key);
            Log.e(TAG, "Writing user's prefered group:" + key);
            startActivity(intent);
        }
    }

    public static class Holder {
        public TextView mName;
        public int mIndex;
    }
}
