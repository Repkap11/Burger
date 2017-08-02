package com.repkap11.burger;

import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import static android.os.Build.VERSION.SDK_INT;

public class AddLunchLocationsFractivity extends Fractivity<AddLunchLocationsFractivity.AddLunchLocationsFragment> {

    @Override
    protected AddLunchLocationsFractivity.AddLunchLocationsFragment createFragment(Bundle savedInstanceState) {
        return new AddLunchLocationsFragment();
    }

    public static class AddLunchLocationsFragment extends Fractivity.FractivityFragment {
        String mLocationName = null;

        @Override
        protected void create(Bundle savedInstanceState) {

        }

        @Override
        protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fractivity_add_lunch_locations, container, false);
            Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
            toolbar.setTitle(R.string.fractivity_add_lunch_locations_title);
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_clear_black, null);
            toolbar.setNavigationIcon(drawable);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
            return rootView;
        }

        @Override
        protected void destroyView() {

        }
    }
}
