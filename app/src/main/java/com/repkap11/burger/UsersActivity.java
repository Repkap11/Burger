package com.repkap11.burger;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;


public class UsersActivity extends FirebaseAdapterFractivity<UsersActivity.Holder> {

    @Override
    protected FirebaseAdapterFragment createFirebaseFragment() {
        return new LunchLocationFragment<UsersActivity.Holder>();
    }

    public static class LunchLocationFragment<AdapterHolder> extends FirebaseAdapterFragment {


        //Using this activity view
        @Override
        protected View createAdapterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fractivity_firebase_adapter, container, false);
        }

        //Put this data
        @Override
        protected String adapterReference() {
            return "users";
        }

        //Into list listview
        @Override
        protected AbsListView getListView(View rootView) {
            return (AbsListView) rootView.findViewById(R.id.fractivity_adaptertest_list);
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
