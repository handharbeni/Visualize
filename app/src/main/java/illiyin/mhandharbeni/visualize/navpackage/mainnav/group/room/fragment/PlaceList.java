package illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import illiyin.mhandharbeni.databasemodule.ChatModel;
import illiyin.mhandharbeni.databasemodule.GrupLocationModel;
import illiyin.mhandharbeni.realmlibrary.Crud;
import illiyin.mhandharbeni.visualize.R;
import illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.adapter.LokasiAdapter;
import illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.adapter.MemberAdapter;
import io.realm.RealmResults;

/**
 * Created by root on 10/26/17.
 */

public class PlaceList extends Fragment {
    View v;

    private RealmRecyclerView listLocationGrup;

    private Crud crud;
    private GrupLocationModel grupLocationModel;
    private Integer id;

    private LokasiAdapter lokasiAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fetch_modul();
        fetch_extras();
        v = inflater.inflate(R.layout.__navactivity_mainnav_layout_group_listplace, container, false);

        fetch_element();
        fetch_event();
        fetch_adapter();
        return v;
    }

    private void fetch_modul(){
        grupLocationModel = new GrupLocationModel();
        crud = new Crud(getActivity().getApplicationContext(), grupLocationModel);
    }

    private void fetch_element(){
        listLocationGrup = v.findViewById(R.id.listplace);
    }
    private void fetch_extras(){
        Bundle args = getArguments();
        id = args.getInt("id", 0);
    }

    private void fetch_adapter(){
        RealmResults memberResults = crud.read("id_grup", id);
        lokasiAdapter = new LokasiAdapter(getActivity().getApplicationContext(), memberResults);
        listLocationGrup.setAdapter(lokasiAdapter);

    }

    private void fetch_event(){

    }
}
