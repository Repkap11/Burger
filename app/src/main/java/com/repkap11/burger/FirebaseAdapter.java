package com.repkap11.burger;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseException;
import com.repkap11.burger.activities.base.FirebaseAdapterFractivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paul on 2/16/16.
 */
public class FirebaseAdapter<AdapterHolder, AdapterData> extends BaseAdapter {
    private static final String TAG = FirebaseAdapter.class.getSimpleName();
    private final FirebaseAdapterFractivity.FirebaseAdapterFragment<AdapterHolder, AdapterData> mFragment;

    public static class AdapterKeyValue {
        public String key;
        public Object value;

        public AdapterKeyValue(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof AdapterKeyValue)) return false;
            return key.equals(((AdapterKeyValue) o).key);
        }

        @Override
        public int hashCode() {
            return key.hashCode();
        }
    }

    private List<AdapterKeyValue> mData;

    public FirebaseAdapter(FirebaseAdapterFractivity.FirebaseAdapterFragment firebaseAdapterFragment) {
        mFragment = firebaseAdapterFragment;
        mData = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AdapterHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mFragment.getActivity()).inflate(mFragment.getListResource(), parent, false);
            holder = mFragment.populateHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (AdapterHolder) convertView.getTag();
        }
        AdapterKeyValue data = mData.get(position);
        mFragment.populateView(convertView, holder, position, data.key, data.value);

        return convertView;
    }

    public boolean add(DataSnapshot data) {
        Log.e(TAG, "Adding data:" + data.getKey());
        Object value = null;
        try {
            value = data.getValue(mFragment.getAdapterDataClass());
        } catch (DatabaseException e) {
            Log.e(TAG, "Unable to parse data:" + data.toString());
            e.printStackTrace();
        }
        if (value == null) {
            return false;
        }
        boolean result = mData.add(new AdapterKeyValue(data.getKey(), value));
        if (result) {
            notifyDataSetChanged();
        }
        return result;
    }

    public boolean remove(DataSnapshot data) {
        Log.e(TAG, "Removing data:" + data.getKey());
        Object value = null;
        try {
            value = data.getValue(mFragment.getAdapterDataClass());
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        if (value == null) {
            return false;
        }
        boolean result = mData.remove(new AdapterKeyValue(data.getKey(), value));
        if (result) {
            notifyDataSetChanged();
        }
        return result;

    }
}