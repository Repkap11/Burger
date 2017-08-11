package com.repkap11.burger.activities.base;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.repkap11.burger.BurgerApplication;
import com.repkap11.burger.R;
import com.repkap11.burger.UpdateAppTask;
import com.repkap11.burger.activities.SettingsActivity;
import com.repkap11.burger.activities.SignInFractivity;

/**
 * Created by paul on 8/5/17.
 */

public abstract class BarMenuFractivity extends Fractivity<Fractivity.FractivityFragment> {

    private static final String TAG = BarMenuFractivity.class.getSimpleName();

    public abstract static class BarMenuFractivityFragment extends Fractivity.FractivityFragment implements GoogleApiClient.OnConnectionFailedListener {
        private static final String TAG = BarMenuFractivity.class.getSimpleName();
        private static final int REQUEST_CODE_ASK_FOR_WRITE_EXPERNAL_PERMISSION = 44;
        private GoogleApiClient mGoogleAPIClient;
        private boolean mShowBar = true;

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_main, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_settings:
                    //Toast.makeText(getActivity(), "Settings selected", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), SettingsActivity.class);
                    startActivity(intent);
                    return true;

                case R.id.action_update:
                    startUpdateAppProcedure();
                    break;
                case R.id.action_sign_out:
                    BurgerApplication.updateDeviceToken(getActivity(), false);
                    BurgerApplication.setUserPerferedLunchGroup(getActivity(), null);
                    FirebaseAuth.getInstance().signOut();
                    Auth.GoogleSignInApi.signOut(mGoogleAPIClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            Intent intent = new Intent(getActivity(), SignInFractivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                    //Toast.makeText(getActivity(), "Sign Out selected", Toast.LENGTH_SHORT).show();

                    break;
                default:
                    Toast.makeText(getActivity(), "Other selected", Toast.LENGTH_SHORT).show();
                    break;

            }
            return false;
        }

        private void startUpdateAppProcedure() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_FOR_WRITE_EXPERNAL_PERMISSION);
                    return;
                }
            }
            continueUpdateAppWithPermissions();
        }

        @Override
        public void requestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
            continueUpdateAppWithPermissions();

        }

        private void continueUpdateAppWithPermissions() {
            new UpdateAppTask(getActivity().getApplicationContext(), true).execute();
        }

        @Override
        @CallSuper
        protected void create(Bundle savedInstanceState) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            mGoogleAPIClient = new GoogleApiClient.Builder(
                    getActivity())
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
            mGoogleAPIClient.connect();
        }

        protected final View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fractivity_bar_menu, container, false);
            View childView;
            if (mShowBar) {
                childView = createBarView(inflater, rootView, savedInstanceState);
            } else {
                childView = createBarView(inflater, container, savedInstanceState);
                return childView;
            }
            rootView.addView(childView);
            setHasOptionsMenu(true);
            Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.fractivity_bar_menu_toolbar);
            if (mShowBar) {
                toolbar.setTitle(getBarTitleResource());
                ((Fractivity) getActivity()).setSupportActionBar(toolbar);
                boolean showBackIcon = getShowBackIcon();
                if (showBackIcon) {
                    Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_clear_black, null);
                    toolbar.setNavigationIcon(drawable);
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onBackIconClick();
                        }
                    });
                }
            } else {
                toolbar.setVisibility(View.GONE);
            }
            FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
            boolean showFab = getShowFab();
            if (showFab) {
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onFabClick();
                    }
                });
            } else {
                fab.setVisibility(View.GONE);
            }
            return rootView;
        }

        protected abstract void onBackIconClick();

        protected abstract boolean getShowBackIcon();

        protected abstract void onFabClick();

        protected abstract boolean getShowFab();

        protected abstract int getBarTitleResource();

        @Override
        protected void destroyView() {

        }

        protected abstract View createBarView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);


        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        }

        public void setShowBar(boolean showBar) {
            mShowBar = showBar;
        }
    }
}
