package illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import illiyin.mhandharbeni.databasemodule.MemberModel;
import illiyin.mhandharbeni.realmlibrary.Crud;
import illiyin.mhandharbeni.visualize.R;

/**
 * Created by root on 10/25/17.
 */

public class MemberList extends Fragment {
    View v;

    private RealmRecyclerView listmember;

    private Crud crud;
    private MemberModel memberModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fetch_modul();
        v = inflater.inflate(R.layout.__navactivity_mainnav_layout_group_listmember, container, false);

        fetch_element();
        fetch_event();
        fetch_adapter();
        return v;
    }

    private void fetch_modul(){
        memberModel = new MemberModel();
        crud = new Crud(getActivity().getApplicationContext(), memberModel);
    }

    private void fetch_element(){
        listmember = v.findViewById(R.id.listmember);
    }

    private void fetch_adapter(){

    }

    private void fetch_event(){

    }
}
