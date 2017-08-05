package com.repkap11.burger.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.repkap11.burger.activities.base.Fractivity;

/**
 * Created by paul on 8/5/17.
 */

public class SettingsFractivity extends Fractivity {

    @Override
    protected FractivityFragment createFragment(Bundle savedInstanceState) {
        return new SettingsFragment();
    }

    public static class SettingsFragment extends FractivityFragment {

        @Override
        protected void create(Bundle savedInstanceState) {

        }

        @Override
        protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return null;
        }

        @Override
        protected void destroyView() {

        }
    }
}
