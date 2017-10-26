package illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import illiyin.mhandharbeni.databasemodule.ChatModel;
import illiyin.mhandharbeni.realmlibrary.Crud;
import illiyin.mhandharbeni.visualize.R;

/**
 * Created by root on 10/26/17.
 */

public class PlaceList extends Fragment {
    View v;

    private RealmRecyclerView listchat;

    private Crud crud;
    private ChatModel chatModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fetch_modul();
        v = inflater.inflate(R.layout.__navactivity_mainnav_layout_group_listplace, container, false);

        fetch_element();
        fetch_event();
        fetch_adapter();
        return v;
    }

    private void fetch_modul(){
        chatModel = new ChatModel();
        crud = new Crud(getActivity().getApplicationContext(), chatModel);
    }

    private void fetch_element(){
        listchat = v.findViewById(R.id.listchat);
    }

    private void fetch_adapter(){

    }

    private void fetch_event(){

    }
}
