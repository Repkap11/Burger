package com.repkap11.burger.activities.base;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.messaging.FirebaseMessaging;
import com.repkap11.burger.FirebaseAdapter;


public abstract class FirebaseAdapterFractivity<AdapterHolder, AdapterData> extends Fractivity<FirebaseAdapterFractivity.FirebaseAdapterFragment> {
    private static final String TAG = FirebaseAdapterFractivity.class.getSimpleName();


    //Since Fractivity onCreate needs to not be final (for version string checking) enfore that here
    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected FirebaseAdapterFragment createFragment(Bundle savedInstanceState) {
        //use the bundle to create the fragment
        FirebaseAdapterFragment<AdapterHolder, AdapterData> fragment = createFirebaseFragment();
        return fragment;
    }

    protected abstract FirebaseAdapterFragment createFirebaseFragment();


    public static abstract class FirebaseAdapterFragment<AdapterHolder, AdapterData> extends BarMenuFractivity.BarMenuFragment implements AdapterView.OnItemClickListener {
        FirebaseAdapter mAdapter;

        protected abstract String adapterReference();

        @Override
        protected void create(Bundle savedInstanceState) {
            super.create(savedInstanceState);
            mAdapter = new FirebaseAdapter<AdapterHolder, AdapterData>(this);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final FirebaseMessaging messaging = FirebaseMessaging.getInstance();
            messaging.subscribeToTopic("lunch");

            String reference = adapterReference();
            Log.e(TAG, "Starting Adapter with key:" + reference);
            final DatabaseReference databaseRef = database.getReference(reference);
            Query refQuery = getQuery(databaseRef);
            refQuery.addChildEventListener(new ChildEventListener() {

                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    mAdapter.add(dataSnapshot);
                }

                // This function is called each time a child item is removed.
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    mAdapter.remove(dataSnapshot);
                }

                // The following functions are also required in ChildEventListener implementations.
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                }

                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("TAG:", "Failed to read value.", error.toException());
                }
            });
        }

        protected Query getQuery(DatabaseReference databaseRef) {
            return databaseRef.orderByKey();
        }


        private AbsListView mListView;

        @Override
        protected View createBarView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = createAdapterView(inflater, container, savedInstanceState);
            mListView = getListView(rootView);
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(this);
            return rootView;
        }

        protected abstract AbsListView getListView(View rootView);

        protected abstract View createAdapterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

        @Override
        protected void destroyView() {
            mListView = null;
        }

        public abstract int getListResource();

        public abstract AdapterHolder populateHolder(View convertView);

        public abstract void populateView(View convertView, AdapterHolder holder, int position, String key, Object value);

        public abstract Class<AdapterData> getAdapterDataClass();

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            FirebaseAdapter.AdapterKeyValue keyValuePair = (FirebaseAdapter.AdapterKeyValue) mAdapter.getItem(position);
            AdapterHolder holder = (AdapterHolder) view.getTag();
            onItemClicked(view, holder, position, adapterReference() + "/" + keyValuePair.key, keyValuePair.key, (AdapterData) keyValuePair.value);
        }

        protected abstract void onItemClicked(View view, AdapterHolder holder, int position, String key, String link, AdapterData value);

    }
}
