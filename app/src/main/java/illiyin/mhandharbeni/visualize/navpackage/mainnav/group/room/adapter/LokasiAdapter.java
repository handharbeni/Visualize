package illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import illiyin.mhandharbeni.databasemodule.GrupLocationModel;
import illiyin.mhandharbeni.visualize.R;
import illiyin.mhandharbeni.visualize.utils.DeleteItem;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by root on 11/18/17.
 */

public class LokasiAdapter extends RealmBasedRecyclerViewAdapter<GrupLocationModel, LokasiAdapter.MyViewHolder> {
    private DeleteItem deleteItem;

    public LokasiAdapter(Context context, RealmResults<GrupLocationModel> realmResults, DeleteItem deleteItem) {
        super(context, realmResults, true, false);
        this.deleteItem = deleteItem;
    }

    @Override
    public LokasiAdapter.MyViewHolder onCreateRealmViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = inflater.inflate(R.layout.__navactivity_mainnav_itemlocation, viewGroup, false);
        return new LokasiAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindRealmViewHolder(@NonNull LokasiAdapter.MyViewHolder myViewHolder, int i) {
        final GrupLocationModel m = realmResults.get(i);
        assert m != null;
        myViewHolder.nama_lokasi.setText(m.getNama_lokasi());
        myViewHolder.prioritas_lokasi.setText(String.valueOf(m.getPrioritas()));
        myViewHolder.iconDelete.setOnClickListener(v -> deleteItem.onConfirmDelete(m.getId(), "Are you sure to delete this Place / Location?"));
    }

    class MyViewHolder extends RealmViewHolder {
        TextView nama_lokasi, prioritas_lokasi, alamat_lokasi;
        ImageView iconDelete;
        CardView cardParent;
        MyViewHolder(View itemView) {
            super(itemView);
            nama_lokasi = itemView.findViewById(R.id.nama_lokasi);
            prioritas_lokasi = itemView.findViewById(R.id.prioritas_lokasi);
            alamat_lokasi = itemView.findViewById(R.id.alamat_lokasi);
            iconDelete = itemView.findViewById(R.id.iconDelete);
            cardParent = itemView.findViewById(R.id.cardParent);
        }
    }
}
