package com.repkap11.burger.activities.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.repkap11.burger.R;
import com.repkap11.burger.activities.SettingsFractivity;

/**
 * Created by paul on 8/5/17.
 */

public abstract class BarMenuFractivity extends Fractivity<Fractivity.FractivityFragment> {

    public abstract static class BarMenuFragment extends Fractivity.FractivityFragment {
        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_main, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_settings:
                    Toast.makeText(getActivity(), "Settings selected", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), SettingsFractivity.class);
                    startActivity(intent);
                    return true;
                default:
                    Toast.makeText(getActivity(), "Other selected", Toast.LENGTH_SHORT).show();
                    break;

            }
            return false;
        }

        protected final View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = createBarView(inflater, container, savedInstanceState);
            setHasOptionsMenu(true);
            Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
            if (toolbar != null) {
                ((Fractivity) getActivity()).setSupportActionBar(toolbar);
                ((Fractivity) getActivity()).getSupportActionBar();
            }
            return rootView;
        }

        protected abstract View createBarView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);


    }

}
