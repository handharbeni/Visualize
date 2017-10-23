package illiyin.mhandharbeni.visualize.navpackage.mainnav.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import illiyin.mhandharbeni.visualize.navpackage.mainnav.contact.ContactFragment;
import illiyin.mhandharbeni.visualize.navpackage.mainnav.group.GroupFragment;

/**
 * Created by root on 9/5/17.
 */

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return new GroupFragment();
            case 1:
                return new ContactFragment();
        }

        return new GroupFragment();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "Group";
        }
        else if (position == 1)
        {
            title = "Contact";
        }
        return title;
    }
}