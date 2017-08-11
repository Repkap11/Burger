package com.repkap11.burger.activities.base;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.repkap11.burger.R;
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

    public static class TabFractivityFragment extends Fractivity.FractivityFragment {
        @Override
        protected void create(Bundle savedInstanceState) {

        }

        @Override
        protected final View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fractivity_tab, container, false);
            TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.fractivity_tab_tablayout);
            ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.fractivity_tab_viewpager);
            Fractivity.FractivityFragment[] fragments = new FractivityFragment[2];
            fragments[0] = new UsersFractivityFragment();
            fragments[1] = new LunchLocationsFractivityFragment();
            viewPager.setAdapter(new TabFractivity.TabPager(getActivity().getSupportFragmentManager(), fragments));

            return rootView;
        }

        @Override
        protected void destroyView() {

        }
    }

    private static class TabPager extends FragmentStatePagerAdapter {
        private final FractivityFragment[] mFractivityFragments;

        public TabPager(FragmentManager fm, Fractivity.FractivityFragment[] fractivityFragments) {
            super(fm);
            mFractivityFragments = fractivityFragments;
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
            return super.getPageTitle(position);
        }
    }
}
