package illiyin.mhandharbeni.visualize.navpackage.mainnav.group;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import illiyin.mhandharbeni.databasemodule.AdapterModel;
import illiyin.mhandharbeni.databasemodule.GrupModel;
import illiyin.mhandharbeni.realmlibrary.Crud;
import illiyin.mhandharbeni.visualize.R;
import illiyin.mhandharbeni.visualize.navpackage.mainnav.group.adapter.GroupAdapter;
import illiyin.mhandharbeni.visualize.utils.DeleteItem;
import illiyin.mhandharbeni.visualize.utils.DividerRecycleView;
import io.realm.RealmResults;

/**
 * Created by root on 10/23/17.
 */

public class GroupFragment extends Fragment implements DeleteItem {
    private View v;
    private RealmRecyclerView list;

    private Crud crud;

    private GroupAdapter groupAdapter;

    private AdapterModel adapterModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.__navactivity_mainnav_layout_group, container, false);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        fetch_modul();
        fetch_element();
        fetch_event();
        fetch_adapter();
    }

    private void fetch_modul(){
        adapterModel = new AdapterModel(getActivity().getApplicationContext());
        GrupModel grupModel = new GrupModel();
        crud = new Crud(getActivity().getApplicationContext(), grupModel);
    }

    private void fetch_element(){
        list = v.findViewById(R.id.list);
    }

    private void fetch_event(){

    }

    private void fetch_adapter(){
        RealmResults grupResults = crud.read("deleted", "N");
        groupAdapter = new GroupAdapter(getActivity().getApplicationContext(), grupResults, true, this);
        DividerRecycleView dividerRecycleView = new DividerRecycleView(getActivity().getApplicationContext(), Color.GRAY, 0.5f);
        list.addItemDecoration(dividerRecycleView);
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

    @Override
    public void onConfirmDelete(int id, String message) {
        showDialog(id, message);
    }
    private void showDialog(final int id, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getWindow().getContext());
        builder.setMessage(message);
        builder.setPositiveButton("YA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    deleteData(id);
                } catch (Exception e) {
                    FirebaseCrash.report(e);
                }
            }
        });
        builder.setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void deleteData(int id) throws Exception {
        String returns = adapterModel.delete_grup(String.valueOf(id));
        if (returns.equalsIgnoreCase("Grup Berhasil Di Hapus.")){
            groupAdapter.notifyDataSetChanged();
            Toast.makeText(getActivity().getApplicationContext(), "The group successfully removed.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getActivity().getApplicationContext(), "The group fail to removed.", Toast.LENGTH_SHORT).show();
        }
    }
}
