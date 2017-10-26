package illiyin.mhandharbeni.visualize.navpackage.mainnav.contact;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import illiyin.mhandharbeni.databasemodule.ContactModel;
import illiyin.mhandharbeni.realmlibrary.Crud;
import illiyin.mhandharbeni.visualize.R;
import illiyin.mhandharbeni.visualize.navpackage.mainnav.contact.adapter.ContactAdapter;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by root on 10/23/17.
 */

public class ContactFragment extends Fragment {
    private View v;
    private RealmRecyclerView list;

    private Crud crud;
    private ContactModel contactModel;

    private ContactAdapter contactAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fetch_modul();

        v = inflater.inflate(R.layout.__navactivity_mainnav_layout_contact, container, false);

        fetch_element();
        fetch_event();
        fetch_adapter();
        return v;
    }

    private void fetch_modul(){
        contactModel = new ContactModel();
        crud = new Crud(getActivity().getApplicationContext(), contactModel);
    }

    private void fetch_element(){
        list = v.findViewById(R.id.list);
    }

    private void fetch_event(){

    }

    private void fetch_adapter(){
        RealmResults grupResults = crud.readSorted("id", Sort.DESCENDING);
        contactAdapter = new ContactAdapter(getActivity().getApplicationContext(), grupResults, true);
        list.setAdapter(contactAdapter);
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
