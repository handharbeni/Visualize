package illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import illiyin.mhandharbeni.databasemodule.AdapterModel;
import illiyin.mhandharbeni.databasemodule.GrupLocationModel;
import illiyin.mhandharbeni.realmlibrary.Crud;
import illiyin.mhandharbeni.visualize.R;
import illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.adapter.LokasiAdapter;
import illiyin.mhandharbeni.visualize.utils.DeleteItem;
import io.realm.RealmResults;

/**
 * Created by root on 10/26/17.
 */

public class PlaceList extends Fragment implements DeleteItem{
    View v;

    private RealmRecyclerView listLocationGrup;

    private Crud crud;
    private Integer id;

    private LokasiAdapter lokasiAdapter;
    private AdapterModel adapterModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.__navactivity_mainnav_layout_group_listplace, container, false);
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

    private void fetch_modul(){
        adapterModel = new AdapterModel(getActivity().getApplicationContext());
        GrupLocationModel grupLocationModel = new GrupLocationModel();
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
        lokasiAdapter = new LokasiAdapter(getActivity().getApplicationContext(), memberResults, this);
        listLocationGrup.setAdapter(lokasiAdapter);
    }

    private void fetch_event(){

    }

    @Override
    public void onConfirmDelete(int id, String message) {
        showDialog(id, message);
    }

    @Override
    public void onConfirmGrup(int id, String message) {

    }

    private void showDialog(final int id, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getWindow().getContext());
        builder.setMessage(message);
        builder.setPositiveButton("Yes", (dialog, which) -> {
            try {
                deleteData(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        builder.setNegativeButton("No", (dialog, which) -> {

        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void deleteData(int id) throws Exception {
        String returns = adapterModel.delete_destinasi_grup(String.valueOf(id));
        if (returns.equalsIgnoreCase("Destinasi Grup Berhasil Di Hapus.")){
            crud.delete("id", id);
            lokasiAdapter.notifyDataSetChanged();
            Toast.makeText(getActivity().getApplicationContext(), "The destination of group successfully removed.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getActivity().getApplicationContext(), "The destination of group fail to removed.", Toast.LENGTH_SHORT).show();
        }
    }
}
