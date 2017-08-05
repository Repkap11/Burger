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

import com.google.firebase.auth.FirebaseAuth;
import com.repkap11.burger.R;
import com.repkap11.burger.activities.SettingsActivity;
import com.repkap11.burger.activities.SignInActivity;
import com.repkap11.burger.activities.SignInFractivity;

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
            Intent intent;
            switch (item.getItemId()) {

                case R.id.action_settings:
                    Toast.makeText(getActivity(), "Settings selected", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getActivity(), SettingsActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.action_sign_out:
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(getActivity(), "Sign Out selected", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getActivity(), SignInFractivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
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
