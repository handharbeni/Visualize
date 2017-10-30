package illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import illiyin.mhandharbeni.databasemodule.MemberModel;
import illiyin.mhandharbeni.realmlibrary.Crud;
import illiyin.mhandharbeni.visualize.R;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by root on 10/28/17.
 */

public class MemberAdapter extends RealmBasedRecyclerViewAdapter<MemberModel, MemberAdapter.MyViewHolder> {
    private MemberModel memberModel;
    private Crud crud;

    @Override
    public MemberAdapter.MyViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
        View v = inflater.inflate(R.layout.__navactivity_mainnav_itemlist, viewGroup, false);
        return new MemberAdapter.MyViewHolder((LinearLayout) v);
    }

    @Override
    public void onBindRealmViewHolder(final MemberAdapter.MyViewHolder myViewHolder, final int i) {
        final MemberModel m = realmResults.get(i);
        Glide.with(getContext()).load(m.getImage()).into(myViewHolder.image);
        myViewHolder.title.setText(m.getNama());
        myViewHolder.subtitle.setText(m.getNo_telp());
    }
    public class MyViewHolder extends RealmViewHolder {
        LinearLayout listparent;
        ImageView image;
        TextView title, subtitle;
        public MyViewHolder(LinearLayout container) {
            super(container);
            this.listparent = container.findViewById(R.id.listparent);
            this.image = container.findViewById(R.id.image);
            this.title = container.findViewById(R.id.title);
            this.subtitle = container.findViewById(R.id.subtitle);
        }
    }
    public MemberAdapter(Context context, RealmResults<MemberModel> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate, false);
        memberModel = new MemberModel();
        crud = new Crud(getContext(), memberModel);
    }
}
