package com.repkap11.burger.activities.base;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.repkap11.burger.R;
import com.repkap11.burger.fragments.AboutUserFractivityFragment;
import com.repkap11.burger.fragments.LunchLocationsFractivityFragment;
import com.repkap11.burger.fragments.UsersFractivityFragment;

/**
 * Created by paul on 8/10/17.
 */

public class TabFractivity extends Fractivity<TabFractivity.TabFractivityFragment> {
    @Override
    protected TabFractivityFragment createFragment(Bundle savedInstanceState) {
        return new TabFractivityFragment();
    }

    public static class TabFractivityFragment extends BarMenuFractivity.BarMenuFractivityFragment {
        @Override
        protected void create(Bundle savedInstanceState) {
            super.create(savedInstanceState);

        }

        @Override
        protected View createBarView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fractivity_tab, container, false);
            TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.fractivity_tab_tablayout);
            ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.fractivity_tab_viewpager);
            BarMenuFractivity.BarMenuFractivityFragment[] fragments = new BarMenuFractivity.BarMenuFractivityFragment[2];
            fragments[0] = new AboutUserFractivityFragment();
            fragments[1] = new LunchLocationsFractivityFragment();
            viewPager.setAdapter(new TabPager(this, getActivity().getSupportFragmentManager(), fragments));
            tabLayout.setupWithViewPager(viewPager, true);

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
        protected int getBarTitleResource() {
            return R.string.app_name;
        }

        @Override
        protected void destroyView() {

            super.destroyView();

        }
    }

    private static class TabPager extends FragmentStatePagerAdapter {
        private final BarMenuFractivity.BarMenuFractivityFragment[] mFractivityFragments;
        private final TabFractivityFragment mFractivityFragment;

        public TabPager(TabFractivityFragment fractivityFragment, FragmentManager fm, BarMenuFractivity.BarMenuFractivityFragment[] fractivityFragments) {
            super(fm);
            mFractivityFragment = fractivityFragment;
            mFractivityFragments = fractivityFragments;
            for (BarMenuFractivity.BarMenuFractivityFragment fragment : mFractivityFragments) {
                fragment.setShowBar(false);
            }
        }

        @Override
        public Fragment getItem(int position) {
            return mFractivityFragments[position];
        }

        @Override
        public int getCount() {
            return mFractivityFragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            BarMenuFractivity.BarMenuFractivityFragment fragment = mFractivityFragments[position];
            return mFractivityFragment.getResources().getText(fragment.getBarTitleResource());
        }
    }
}
