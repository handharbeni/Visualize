package illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import illiyin.mhandharbeni.databasemodule.ChatModel;
import illiyin.mhandharbeni.databasemodule.MemberModel;
import illiyin.mhandharbeni.realmlibrary.Crud;
import illiyin.mhandharbeni.visualize.R;
import illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.adapter.ChatAdapter;
import illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.adapter.MemberAdapter;
import io.realm.RealmResults;
import io.realm.Sort;

import static android.content.ContentValues.TAG;

/**
 * Created by root on 10/26/17.
 */

public class ChatList extends Fragment {
    View v;

    private RealmRecyclerView listchat;

    private Crud crud;
    private ChatModel chatModel;
    private Integer id;

    private ChatAdapter chatAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fetch_modul();
        fetch_extras();
        v = inflater.inflate(R.layout.__navactivity_mainnav_layout_group_listchat, container, false);

        fetch_element();
        fetch_event();
        fetch_adapter();
        return v;
    }
    private void fetch_extras(){
        Bundle args = getArguments();
        id = args.getInt("id", 0);
    }

    private void fetch_modul(){
        chatModel = new ChatModel();
        crud = new Crud(getActivity().getApplicationContext(), chatModel);
    }

    private void fetch_element(){
        listchat = v.findViewById(R.id.listchat);
    }

    private void fetch_adapter(){
        RealmResults memberResults = crud.read("id_grup", id);
        Log.d(TAG, "fetch_adapter: Result Size "+String.valueOf(memberResults.size()));
//        RealmResults memberResults = crud.readSorted("id_grup", id, "id", Sort.DESCENDING);
        chatAdapter = new ChatAdapter(getActivity().getApplicationContext(), memberResults, true);
        listchat.setAdapter(chatAdapter);
    }

    private void fetch_event(){

    }
}
