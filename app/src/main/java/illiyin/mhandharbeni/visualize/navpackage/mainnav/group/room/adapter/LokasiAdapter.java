package illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.adapter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import illiyin.mhandharbeni.databasemodule.GrupLocationModel;
import illiyin.mhandharbeni.realmlibrary.Crud;
import illiyin.mhandharbeni.sessionlibrary.Session;
import illiyin.mhandharbeni.sessionlibrary.SessionListener;
import illiyin.mhandharbeni.visualize.R;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by root on 11/18/17.
 */

public class LokasiAdapter extends RealmBasedRecyclerViewAdapter<GrupLocationModel, LokasiAdapter.MyViewHolder> {
    private GrupLocationModel grupLocationModel;
    private Crud crud;
    private Session session;

    public LokasiAdapter(Context context, RealmResults<GrupLocationModel> realmResults) {
        super(context, realmResults, true, false);
        grupLocationModel = new GrupLocationModel();
        crud = new Crud(getContext(), grupLocationModel);
        session = new Session(getContext(), new SessionListener() {
            @Override
            public void sessionChange() {

            }
        });

    }

    @Override
    public LokasiAdapter.MyViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
        View v = inflater.inflate(R.layout.__navactivity_mainnav_itemlocation, viewGroup, false);
        return new LokasiAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindRealmViewHolder(LokasiAdapter.MyViewHolder myViewHolder, int i) {
        final GrupLocationModel m = realmResults.get(i);
        myViewHolder.nama_lokasi.setText(m.getNama_lokasi());
        myViewHolder.prioritas_lokasi.setText(String.valueOf(m.getPrioritas()));
//        String address = "Tidak Ditemukan";
//        try {
//            address = getAddress(Double.valueOf(m.getLatitude()), Double.valueOf(m.getLongitude()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        myViewHolder.alamat_lokasi.setText(address);
    }

    public class MyViewHolder extends RealmViewHolder {
        TextView nama_lokasi, prioritas_lokasi, alamat_lokasi;
        public MyViewHolder(View itemView) {
            super(itemView);
            nama_lokasi = itemView.findViewById(R.id.nama_lokasi);
            prioritas_lokasi = itemView.findViewById(R.id.prioritas_lokasi);
            alamat_lokasi = itemView.findViewById(R.id.alamat_lokasi);
        }
    }
    private String getAddress(Double latitude, Double longitude) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getContext(), Locale.getDefault());

        addresses = geocoder.getFromLocation(latitude, longitude, 1);
        if (addresses.size() >0){
            return addresses.get(0).getAddressLine(0);
        }else{
            return "Tidak Ditemukan";
        }
    }
}
