package illiyin.mhandharbeni.visualize.navpackage.mainnav.contact.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import illiyin.mhandharbeni.databasemodule.ContactModel;
import illiyin.mhandharbeni.sessionlibrary.SessionListener;
import illiyin.mhandharbeni.visualize.R;
import illiyin.mhandharbeni.visualize.utils.DeleteItem;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by root on 10/24/17.
 */

public class ContactAdapter extends RealmBasedRecyclerViewAdapter<ContactModel, ContactAdapter.MyViewHolder> implements SessionListener {
    private DeleteItem deleteItem;

    @Override
    public ContactAdapter.MyViewHolder onCreateRealmViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        v = inflater.inflate(R.layout.__navactivity_mainnav_itemlist, viewGroup, false);
        return new ContactAdapter.MyViewHolder((ConstraintLayout) v);
    }

    @Override
    public void onBindRealmViewHolder(@NonNull final ContactAdapter.MyViewHolder myViewHolder, final int i) {
        final ContactModel m = realmResults.get(i);

        assert m != null;
        Glide.with(getContext())
                .load(m.getImage())
                .into(myViewHolder.image);
        myViewHolder.title.setText(m.getNama());
        myViewHolder.subtitle.setText(m.getNo_telp());
        myViewHolder.iconDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem.onConfirmDelete(m.getId(), "Yakin akan Hapus Contact?");
            }
        });
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
    public ContactAdapter(Context context, RealmResults<ContactModel> realmResults, boolean automaticUpdate, DeleteItem deleteItem) {
        super(context, realmResults, automaticUpdate, false);
        this.deleteItem = deleteItem;
    }
}

