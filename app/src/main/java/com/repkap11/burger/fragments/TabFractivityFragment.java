package com.repkap11.burger.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.repkap11.burger.R;
import com.repkap11.burger.activities.base.BarMenuFractivity;

/**
 * Created by paul on 8/11/17.
 */
public class TabFractivityFragment extends BarMenuFractivity.BarMenuFractivityFragment {
    private static final String TAG = TabFractivityFragment.class.getSimpleName();

    @Override
    protected void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);

    }

    @Override
    protected View createBarView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, boolean attachToRoot) {
        BarMenuFractivity.BarMenuFractivityFragment[] fragments = new BarMenuFractivity.BarMenuFractivityFragment[2];
        fragments[0] = new AboutUserFractivityFragment();
        fragments[1] = new LunchLocationsTodayFractivityFragment();

        View rootView = inflater.inflate(R.layout.fractivity_tab, container, attachToRoot);
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.fractivity_tab_tablayout);
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.fractivity_tab_viewpager);
        TabPager tabPager = new TabPager(this, getChildFragmentManager(), fragments);

        viewPager.setAdapter(tabPager);
        tabLayout.setupWithViewPager(viewPager, true);
        for (BarMenuFractivity.BarMenuFractivityFragment fragment : fragments) {
            Log.w(TAG, "createBarView: removing bar...");
            fragment.setShowBar(false);
        }

        return rootView;
    }

    @Override
    protected void onBackIconClick() {

    }

    @Override
    protected boolean getShowBackIcon() {
        return false;
    }

    @Override
    protected void onFabClick() {

    }

    @Override
    protected boolean getShowFab() {
        return false;
    }

    @Override
    public String getBarTitleString(Context context) {
        return context.getResources().getString(R.string.app_name);
    }

    @Override
    protected void destroyView() {
        super.destroyView();

    }

    private static class TabPager extends FragmentPagerAdapter {
        private final BarMenuFractivity.BarMenuFractivityFragment[] mFractivityFragments;
        private final TabFractivityFragment mFractivityFragment;

        public TabPager(TabFractivityFragment fractivityFragment, FragmentManager fm, BarMenuFractivity.BarMenuFractivityFragment[] fractivityFragments) {
            super(fm);
            mFractivityFragment = fractivityFragment;
            mFractivityFragments = fractivityFragments;
        }

        @Override
        public Fragment getItem(int position) {
            Log.w(TAG, "getItem: returning item with getShowBar:" + mFractivityFragments[position].getShowBar());
            return mFractivityFragments[position];
        }

        @Override
        public int getCount() {
            return mFractivityFragments.length;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            BarMenuFractivity.BarMenuFractivityFragment fragment = mFractivityFragments[position];
            return fragment.getBarTitleString(mFractivityFragment.getActivity());
        }
    }
}
