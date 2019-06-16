package org.henryschmale.milespergallontracker;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DashboardFragment();
            case 1:
                return new HistoryTableFragment();
            case 2:
                return new GraphFragment();
        }
        return new DashboardFragment();
    }

    @Override
    public int getCount() {
        return 3;
    }
}
