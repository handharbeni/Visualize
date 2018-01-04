package illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import illiyin.mhandharbeni.databasemodule.MemberModel;
import illiyin.mhandharbeni.visualize.R;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by root on 10/28/17.
 */

public class MemberAdapter extends RealmBasedRecyclerViewAdapter<MemberModel, MemberAdapter.MyViewHolder> {

    @Override
    public MemberAdapter.MyViewHolder onCreateRealmViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = inflater.inflate(R.layout.__navactivity_mainnav_itemlist, viewGroup, false);
        return new MemberAdapter.MyViewHolder((ConstraintLayout) v);
    }

    @Override
    public void onBindRealmViewHolder(@NonNull final MemberAdapter.MyViewHolder myViewHolder, final int i) {
        final MemberModel m = realmResults.get(i);
        assert m != null;
        Glide.with(getContext()).load(m.getImage()).into(myViewHolder.image);
        myViewHolder.title.setText(m.getNama());
        myViewHolder.subtitle.setText(m.getNo_telp());
        myViewHolder.iconDelete.setVisibility(View.GONE);
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
    public MemberAdapter(Context context, RealmResults<MemberModel> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate, false);
    }
}
