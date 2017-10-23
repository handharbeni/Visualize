package illiyin.mhandharbeni.visualize.navpackage.mainnav;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.tablayouthelper.TabLayoutHelper;

import illiyin.mhandharbeni.databasemodule.AdapterModel;
import illiyin.mhandharbeni.sessionlibrary.Session;
import illiyin.mhandharbeni.sessionlibrary.SessionListener;
import illiyin.mhandharbeni.visualize.R;
import illiyin.mhandharbeni.visualize.navpackage.mainnav.adapter.TabsPagerAdapter;

/**
 * Created by root on 10/23/17.
 */

public class MainNav extends Fragment implements TabLayout.OnTabSelectedListener {
    private View v;

    private Session session;
    private AdapterModel adapterModel;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabLayoutHelper mTabLayoutHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.__navactivity_mainnav, container, false);
        fetch_element();
        fetch_module();
        fetch_view();
        return v;
    }

    private void fetch_module(){
        session = new Session(getActivity().getApplicationContext(), new SessionListener() {
            @Override
            public void sessionChange() {

            }
        });
        adapterModel = new AdapterModel(getActivity().getApplicationContext());
    }

    private void fetch_element(){
        tabLayout = v.findViewById(R.id.tabLayout);
        viewPager = v.findViewById(R.id.pager);
    }

    public void fetch_view(){
        viewPager = v.findViewById(R.id.pager);
        tabLayout = v.findViewById(R.id.tabLayout);
        viewPager.setAdapter(buildAdapter());
        mTabLayoutHelper = new TabLayoutHelper(tabLayout, viewPager);
        mTabLayoutHelper.setAutoAdjustTabModeEnabled(true);
        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.blueCustom));
//        tabLayout.setTabTextColors(getResources().getColor(R.color.greyCustom), getResources().getColor(R.color.blueCustom));
        tabLayout.addOnTabSelectedListener(this);
    }

    private void fetch_event(){

    }
    private PagerAdapter buildAdapter(){
        return new TabsPagerAdapter(getChildFragmentManager());
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
