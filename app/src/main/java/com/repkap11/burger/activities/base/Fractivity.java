package com.repkap11.burger.activities.base;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.repkap11.burger.R;

public abstract class Fractivity<FragType extends Fractivity.FractivityFragment> extends AppCompatActivity {

    private static final String INSTANCE_STATE_FRAGMENT = "INSTANCE_STATE_FRAGMENT";
    private static final String TAG = Fractivity.class.getSimpleName();
    protected FragType mFragment;

    //You really should't overwide this
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fractivity);
        if (savedInstanceState == null) {
            mFragment = createFragment(savedInstanceState);
        } else {
            mFragment = (FragType) getSupportFragmentManager().getFragment(savedInstanceState, INSTANCE_STATE_FRAGMENT);
        }
        if (mFragment == null) {
            Log.e(TAG, "Fragment Null");
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fractivity_view, mFragment, INSTANCE_STATE_FRAGMENT).commitAllowingStateLoss();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getSupportFragmentManager().putFragment(outState, INSTANCE_STATE_FRAGMENT, mFragment);
        super.onSaveInstanceState(outState);
    }

    protected abstract FragType createFragment(Bundle savedInstanceState);

    public static abstract class FractivityFragment extends Fragment {
        public FractivityFragment() {
            super();
            setRetainInstance(true);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            create(savedInstanceState);
        }

        protected abstract void create(Bundle savedInstanceState);

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return createView(inflater, container, savedInstanceState);
        }

        @Override
        public void onDestroyView() {
            destroyView();
            super.onDestroyView();
        }

        protected abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

        protected abstract void destroyView();

        protected void requestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
            mFragment.requestPermissionResult(requestCode, permissions, grantResults);
        } else {
            Log.e(TAG, "Permissions not granted");
        }
    }


}
