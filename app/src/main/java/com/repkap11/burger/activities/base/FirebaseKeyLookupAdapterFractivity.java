package com.repkap11.burger.activities.base;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public abstract class FirebaseKeyLookupAdapterFractivity<AdapterHolder, AdapterData> extends Fractivity<FirebaseKeyLookupAdapterFractivity.FirebaseKeyLookupAdapterFragment> {
    private static final String TAG = FirebaseKeyLookupAdapterFractivity.class.getSimpleName();


    //Since Fractivity onCreate needs to not be final (for version string checking) enfore that here
    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected FirebaseKeyLookupAdapterFragment createFragment(Bundle savedInstanceState) {
        //use the bundle to create the fragment
        FirebaseKeyLookupAdapterFragment<AdapterHolder, AdapterData> fragment = createFirebaseFragment();
        return fragment;
    }

    protected abstract FirebaseKeyLookupAdapterFragment createFirebaseFragment();


    public static abstract class FirebaseKeyLookupAdapterFragment<AdapterHolder, AdapterData> extends FirebaseAdapterFractivity.FirebaseAdapterFragment<AdapterHolder, String> {

        protected abstract String adapter2Reference();

        @Override
        public final void populateView(final View convertView, final AdapterHolder adapterHolder, final int position, String key, Object value) {
            final String secondaryKey = adapter2Reference() + "/" + key;
            Log.e(TAG, "Starting 2nd query for:" + secondaryKey);
            FirebaseDatabase.getInstance().getReference(secondaryKey).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot data) {
                    AdapterData value = null;
                    try {
                        value = (AdapterData) data.getValue(getAdapter2DataClass());
                    } catch (DatabaseException e) {
                        Log.e(TAG, "Unable to parse data:" + data.toString());
                        e.printStackTrace();
                    }
                    if (value == null) {
                        return;
                    }
                    populateView2(convertView, adapterHolder, position, secondaryKey, value);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        protected abstract void populateView2(final View convertView, final AdapterHolder adapterHolder, final int position, String key, AdapterData value);


        @Override
        public final Class<String> getAdapterDataClass() {
            return String.class;
        }

        public abstract Class<AdapterData> getAdapter2DataClass();

        @Override
        protected final void onItemClicked(View view, AdapterHolder adapterHolder, int position, String key, String link, String value) {
            //onItem2Clicked(view, adapterHolder, position, key, link, value);
            //TODO support clicks
        }

        protected abstract void onItem2Clicked(View view, AdapterHolder adapterHolder, int position, String key, String link, AdapterData value);

    }
}
