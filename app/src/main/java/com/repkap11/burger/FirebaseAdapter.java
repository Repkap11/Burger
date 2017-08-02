package com.repkap11.burger;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paul on 2/16/16.
 */
public class FirebaseAdapter<AdapterHolder> extends BaseAdapter {
    private static final String TAG = FirebaseAdapter.class.getSimpleName();
    private final FirebaseAdapterFractivity.FirebaseAdapterFragment<AdapterHolder> mFragment;

    public static class AdapterData {
        public String key;
        public Object value;

        public AdapterData(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof AdapterData)) return false;
            return key.equals(((AdapterData) o).key);
        }

        @Override
        public int hashCode() {
            return key.hashCode();
        }
    }

    private List<AdapterData> mData;

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
        return null;
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
        AdapterData data = mData.get(position);
        mFragment.populateView(convertView, holder, position, data);

        return convertView;
    }

    public boolean add(DataSnapshot data) {
        Log.e(TAG, "Adding data:" + data.getKey());
        boolean result = mData.add(new AdapterData(data.getKey(), data.getValue()));
        if (result) {
            notifyDataSetChanged();
        }
        return result;
    }

    public boolean remove(DataSnapshot data) {
        Log.e(TAG, "Removing data:" + data.getKey());
        boolean result = mData.remove(new AdapterData(data.getKey(), data.getValue()));
        if (result) {
            notifyDataSetChanged();
        }
        return result;

    }
}