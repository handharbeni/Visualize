package illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import illiyin.mhandharbeni.databasemodule.AdapterModel;
import illiyin.mhandharbeni.databasemodule.MemberModel;
import illiyin.mhandharbeni.realmlibrary.Crud;
import illiyin.mhandharbeni.visualize.R;
import illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.adapter.MemberAdapter;
import illiyin.mhandharbeni.visualize.utils.DeleteItem;
import io.realm.RealmResults;

/**
 * Created by root on 10/25/17.
 */

public class MemberList extends Fragment implements DeleteItem {
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
        MemberAdapter memberAdapter = new MemberAdapter(getActivity().getApplicationContext(), memberResults, true, this);
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
        builder.setNegativeButton("Cancel", (dialog, which) -> {

        });
        builder.setPositiveButton("Yes", (dialog, which) -> {
            try {
                delete(id);
            } catch (Exception e) {
                FirebaseCrash.report(e);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void delete(int id) throws Exception {
        AdapterModel adapterModel = new AdapterModel(getActivity().getApplicationContext());
        MemberModel mm = new MemberModel();
        Crud crudMm = new Crud(getActivity().getApplicationContext(), mm);
        RealmResults results = crudMm.read("id", id);
        if (results.size() > 0){
            MemberModel memberModel = (MemberModel) results.get(0);
            String id_grup = String.valueOf(memberModel.getId_grup());
            String id_user = String.valueOf(memberModel.getId_user());
            String returns = adapterModel.delete_membergrup(id_grup, id_user);
            if (returns.equalsIgnoreCase("Successfully remove member")){
                crudMm.delete("id", id);
            }else{
                Toast.makeText(getActivity().getApplicationContext(), "Cannot Remove Member, Please Try Again", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity().getApplicationContext(), "Member Not Found", Toast.LENGTH_SHORT).show();
        }
    }
}
