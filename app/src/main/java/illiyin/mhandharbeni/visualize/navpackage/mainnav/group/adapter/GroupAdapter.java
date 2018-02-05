package illiyin.mhandharbeni.visualize.navpackage.mainnav.group.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import illiyin.mhandharbeni.databasemodule.AdapterModel;
import illiyin.mhandharbeni.databasemodule.GrupModel;
import illiyin.mhandharbeni.sessionlibrary.SessionListener;
import illiyin.mhandharbeni.visualize.R;
import illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.DetailGroup;
import illiyin.mhandharbeni.visualize.utils.DeleteItem;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by root on 10/24/17.
 */

public class GroupAdapter extends RealmBasedRecyclerViewAdapter<GrupModel, GroupAdapter.MyViewHolder> implements SessionListener {
    private AdapterModel adapterModel;
    private DeleteItem deleteItem;

    @Override
    public GroupAdapter.MyViewHolder onCreateRealmViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = inflater.inflate(R.layout.__navactivity_mainnav_itemlist, viewGroup, false);
        return new GroupAdapter.MyViewHolder((ConstraintLayout) v);
    }

    @Override
    public void onBindRealmViewHolder(@NonNull final GroupAdapter.MyViewHolder myViewHolder, final int i) {
        final GrupModel m = realmResults.get(i);
        assert m != null;
        myViewHolder.title.setText(m.getNama_grup());
        myViewHolder.title.setOnClickListener(view -> {
            if (m.getConfirmation().equalsIgnoreCase("Y")){
                gotoDetail(m.getId());
            }else{
                deleteItem.onConfirmGrup(m.getId(), "Yakin masuk grup?");
            }
        });
        Glide.with(getContext()).load(R.drawable.icon_group).into(myViewHolder.image);
        myViewHolder.subtitle.setText(m.getNama_user());
        myViewHolder.listparent.setOnClickListener(view -> {
            if (m.getConfirmation().equalsIgnoreCase("Y")){
                gotoDetail(m.getId());
            }else{
                deleteItem.onConfirmGrup(m.getId(), "Yakin masuk grup?");
            }
        });
        myViewHolder.iconDelete.setOnClickListener(v -> deleteItem.onConfirmDelete(m.getId(), "Are sure want to remove Group?"));
    }
    private void gotoDetail(Integer id){
        try {
            Bundle b = new Bundle();
            b.putInt("id", id);

            Intent i = new Intent(getContext(), DetailGroup.class);

            i.putExtras(b);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            getContext().startActivity(i);
//            adapterModel.konfirmasi_masuk_grup(String.valueOf(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void sessionChange() {

    }
    class MyViewHolder extends RealmViewHolder {
        ConstraintLayout listparent;
        ImageView image;
        TextView title, subtitle;
        ImageView iconDelete;
        MyViewHolder(ConstraintLayout container) {
            super(container);
            this.listparent = container.findViewById(R.id.listparent);
            this.image = container.findViewById(R.id.image);
            this.title = container.findViewById(R.id.title);
            this.subtitle = container.findViewById(R.id.subtitle);
            this.iconDelete = container.findViewById(R.id.iconDelete);
        }
    }
    public GroupAdapter(Context context, RealmResults<GrupModel> realmResults, boolean automaticUpdate, DeleteItem deleteItem) {
        super(context, realmResults, automaticUpdate, false);
        this.deleteItem = deleteItem;
        this.adapterModel = new AdapterModel(getContext());
    }
}
