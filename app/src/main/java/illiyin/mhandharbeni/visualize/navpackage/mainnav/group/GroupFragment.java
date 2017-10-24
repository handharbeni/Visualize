package illiyin.mhandharbeni.visualize.navpackage.mainnav.group;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import illiyin.mhandharbeni.databasemodule.GrupModel;
import illiyin.mhandharbeni.realmlibrary.Crud;
import illiyin.mhandharbeni.visualize.R;
import illiyin.mhandharbeni.visualize.navpackage.mainnav.group.adapter.GroupAdapter;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by root on 10/23/17.
 */

public class GroupFragment extends Fragment {
    private View v;
    private RealmRecyclerView list;

    private Crud crud;
    private GrupModel grupModel;

    private GroupAdapter groupAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fetch_modul();

        v = inflater.inflate(R.layout.__navactivity_mainnav_layout_group, container, false);

        fetch_element();
        fetch_event();
        fetch_adapter();
        return v;
    }

    private void fetch_modul(){
        grupModel = new GrupModel();
        crud = new Crud(getActivity().getApplicationContext(), grupModel);
    }

    private void fetch_element(){
        list = v.findViewById(R.id.list);
    }

    private void fetch_event(){

    }

    private void fetch_adapter(){
        RealmResults grupResults = crud.readSorted("id", Sort.DESCENDING);
        groupAdapter = new GroupAdapter(getActivity().getApplicationContext(), grupResults, true);
        list.setAdapter(groupAdapter);
    }

    @Override
    public void onPause() {
        crud.closeRealm();
        super.onPause();
    }

    @Override
    public void onStop() {
        crud.closeRealm();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        crud.closeRealm();
        super.onDestroy();
    }
}
