package com.alzatezabala.eslem.chatdemogcm.android.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.alzatezabala.eslem.chatdemogcm.android.Fragments.ConversationsFragment;
import com.alzatezabala.eslem.chatdemogcm.android.Fragments.UsersFragment;
import com.alzatezabala.eslem.chatdemogcm.android.MainActivity;

/**
 * Created by Eslem on 07/10/2014.
 */
public class TabsPageAdapter extends FragmentPagerAdapter {
    public TabsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                // Top Rated fragment activity
                return (Fragment) new UsersFragment();
            case 1:
                // Games fragment activity
                ConversationsFragment fragment = new ConversationsFragment();
                MainActivity.observerBroadCast=fragment;
                return fragment ;
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
