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

import com.google.firebase.crash.FirebaseCrash;
import com.h6ah4i.android.tablayouthelper.TabLayoutHelper;

import net.frederico.showtipsview.ShowTipsBuilder;
import net.frederico.showtipsview.ShowTipsView;
import net.frederico.showtipsview.ShowTipsViewInterface;

import illiyin.mhandharbeni.databasemodule.AdapterModel;
import illiyin.mhandharbeni.sessionlibrary.Session;
import illiyin.mhandharbeni.sessionlibrary.SessionListener;
import illiyin.mhandharbeni.visualize.R;
import illiyin.mhandharbeni.visualize.navpackage.mainnav.adapter.TabsPagerAdapter;

/**
 * Created by root on 10/23/17.
 */

public class MainNav extends Fragment implements TabLayout.OnTabSelectedListener {
    private static Integer tooltipTabGroup = 122;
    private static Integer tooltipTabContac = 123;
    private View v;

    private Session session;
    private AdapterModel adapterModel;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabLayoutHelper mTabLayoutHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.__navactivity_mainnav, container, false);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        fetch_element();
        fetch_module();
        fetch_view();
        try {
            showTooltipTabGroup();
        }catch (Exception e){
            FirebaseCrash.report(e);
        }
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
    private void showTooltipTabGroup(){
        View mainTab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(0);
        ShowTipsView showtips = new ShowTipsBuilder(getActivity())
                .setTarget(mainTab)
                .setTitle("Menu of Group")
                .setDescription("You can see the groups available on this page including your own group.")
                .setDelay(1000)
                .displayOneTime(tooltipTabGroup)
                .build();

        showtips.show(getActivity());
        showtips.setCallback(new ShowTipsViewInterface() {
            @Override
            public void gotItClicked() {
                try {
                    showToolTipTabContact();
                }catch (Exception e){
                    FirebaseCrash.report(e);
                }

            }
        });
    }
    private void showToolTipTabContact(){
        View mainTab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(1);
        ShowTipsView showtips = new ShowTipsBuilder(getActivity())
                .setTarget(mainTab)
                .setTitle("Menu Kontak")
                .setDescription("Daftar Kontak yang telah anda tambahkan")
                .setDelay(1000)
                .displayOneTime(tooltipTabContac)
                .build();
        showtips.show(getActivity());
    }
}
