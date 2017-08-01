package com.repkap11.burger;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;


public class FirebaseAdapterFractivity extends Fractivity<FirebaseAdapterFractivity.FirebaseAdapterFragment> {

    @Override
    protected FirebaseAdapterFragment createFragment(Bundle savedInstanceState) {
        //use the bundle to create the fragment
        return new FirebaseAdapterFragment();
    }

    public static final class FirebaseAdapterFragment extends Fractivity.FractivityFragment {
        FirebaseAdapter mAdapter;

        @Override
        protected void create(Bundle savedInstanceState) {
            mAdapter = new FirebaseAdapter(this);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final FirebaseMessaging messaging = FirebaseMessaging.getInstance();
            messaging.subscribeToTopic("lunch");

            final DatabaseReference databaseRef = database.getReference("todoItems");
            databaseRef.addChildEventListener(new ChildEventListener() {

                // This function is called once for each child that exists
                // when the listener is added. Then it is called
                // each time a new child is added.
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

        AbsListView mListView;

        @Override
        protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fractivity_firebase_adapter, container, false);
            mListView = (AbsListView) rootView.findViewById(R.id.fractivity_adaptertest_list);
            mListView.setAdapter(mAdapter);
            return rootView;
        }

        @Override
        protected void destroyView() {
            mListView = null;
        }
    }
}
