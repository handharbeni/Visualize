package illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import illiyin.mhandharbeni.databasemodule.AdapterModel;
import illiyin.mhandharbeni.visualize.R;
import illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.fragment.ChatList;
import illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.fragment.MapsList;
import illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.fragment.MemberList;

public class DetailGroup extends AppCompatActivity {
    private BottomNavigationView navigation;

    private FrameLayout detailmainframe;

    private AdapterModel adapterModel;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_member:
                    changeFragment(new MemberList());
                    return true;
                case R.id.navigation_maps:
                    changeFragment(new MapsList());
                    return true;
                case R.id.navigation_chat:
                    changeFragment(new ChatList());
                    return true;
                case R.id.navigation_lokasi:
                    changeFragment(new ChatList());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetch_modul();

        setContentView(R.layout.__navactivity_mainnav_layout_detailgroup);
        fetch_element();
        fetch_event();
        fetch_startup();
    }

    private void fetch_modul(){
        adapterModel = new AdapterModel(getApplicationContext());

    }

    private void fetch_element(){
        detailmainframe = (FrameLayout) findViewById(R.id.detailmainframe);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void fetch_event(){

    }

    private void fetch_startup(){
        View view = navigation.findViewById(R.id.navigation_member);
        view.performClick();
    }

    private void changeFragment(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.detailmainframe, fragment);
        ft.commit();
    }

}
