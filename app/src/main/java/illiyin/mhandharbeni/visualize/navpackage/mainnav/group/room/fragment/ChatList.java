package illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import illiyin.mhandharbeni.realmlibrary.Crud;
import illiyin.mhandharbeni.databasemodule.ChatModel;
import illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.adapter.ChatAdapter;
import io.realm.RealmResults;

/**
 * Created by root on 10/26/17.
 */

public class ChatList extends Fragment {
    View v;

    private RealmRecyclerView listchat;

    private Crud crud;
    private Integer id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(illiyin.mhandharbeni.visualize.R.layout.__navactivity_mainnav_layout_group_listchat, container, false);
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
    }

    private void fetch_extras(){
        Bundle args = getArguments();
        id = args.getInt("id", 0);
    }

    private void fetch_modul(){
        ChatModel chatModel = new ChatModel();
        crud = new Crud(getActivity().getApplicationContext(), chatModel);
    }

    private void fetch_element(){
        listchat = v.findViewById(illiyin.mhandharbeni.visualize.R.id.listchat);
    }

    private void fetch_adapter(){
        RealmResults memberResults = crud.read("id_grup", id);
        ChatAdapter chatAdapter = new ChatAdapter(getActivity().getApplicationContext(), memberResults, true);
        listchat.setAdapter(chatAdapter);
    }

    private void fetch_event(){

    }
}
