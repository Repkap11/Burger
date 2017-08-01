package com.repkap11.burger;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paul on 2/16/16.
 */
public class FirebaseAdapter extends BaseAdapter {
    private static final String TAG = FirebaseAdapter.class.getSimpleName();
    private final FirebaseAdapterFractivity.FirebaseAdapterFragment mFragment;

    class AdapterData {
        private String key;
        private Object value;

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
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mFragment.getActivity()).inflate(R.layout.fractivity_firebase_adapter_list_element, parent, false);
            holder = new Holder();
            holder.mName = (TextView) convertView.findViewById(R.id.fractivity_firebase_adapter_list_element_text);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        AdapterData data = mData.get(position);
        holder.mName.setText(data.key + ":" + data.value);
        holder.mIndex = position;

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

    public class Holder {
        public TextView mName;
        public int mIndex;
    }
}