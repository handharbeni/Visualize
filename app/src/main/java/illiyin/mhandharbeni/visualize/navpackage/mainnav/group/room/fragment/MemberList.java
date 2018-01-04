package illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import illiyin.mhandharbeni.databasemodule.MemberModel;
import illiyin.mhandharbeni.realmlibrary.Crud;
import illiyin.mhandharbeni.visualize.R;
import illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.adapter.MemberAdapter;
import io.realm.RealmResults;

/**
 * Created by root on 10/25/17.
 */

public class MemberList extends Fragment {
    View v;

    private RealmRecyclerView listmember;

    private Crud crud;

    private Integer id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.__navactivity_mainnav_layout_group_listmember, container, false);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        fetch_modul();
        fetch_extras();
        fetch_element();
        fetch_event();
        fetch_adapter();
        fetch_data();
    }

    private void fetch_extras(){
        Bundle args = getArguments();
        id = args.getInt("id", 0);
    }

    private void fetch_modul(){
        MemberModel memberModel = new MemberModel();
        crud = new Crud(getActivity().getApplicationContext(), memberModel);
    }

    private void fetch_element(){
        listmember = v.findViewById(R.id.listmember);
    }

    private void fetch_adapter(){
        RealmResults memberResults = crud.read("id_grup", id);
        MemberAdapter memberAdapter = new MemberAdapter(getActivity().getApplicationContext(), memberResults, true);
        listmember.setAdapter(memberAdapter);
    }

    private void fetch_data(){
        RealmResults memberResults = crud.read();
        if(memberResults.size() > 0){
            for (int i=0;i<memberResults.size();i++){
                MemberModel mm = (MemberModel) memberResults.get(i);
            }
        }
    }

    private void fetch_event(){

    }
}
