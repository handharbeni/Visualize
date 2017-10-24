package illiyin.mhandharbeni.visualize.navpackage.mainnav.group.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import illiyin.mhandharbeni.databasemodule.GrupModel;
import illiyin.mhandharbeni.realmlibrary.Crud;
import illiyin.mhandharbeni.sessionlibrary.Session;
import illiyin.mhandharbeni.sessionlibrary.SessionListener;
import illiyin.mhandharbeni.visualize.R;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by root on 10/24/17.
 */

public class GroupAdapter extends RealmBasedRecyclerViewAdapter<GrupModel, GroupAdapter.MyViewHolder> implements SessionListener {
    private Session session;
    private GrupModel grupModel;
    private Crud crud;
    @Override
    public GroupAdapter.MyViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
        View v = inflater.inflate(R.layout.__navactivity_mainnav_itemlist, viewGroup, false);
        return new GroupAdapter.MyViewHolder((LinearLayout) v);
    }

    @Override
    public void onBindRealmViewHolder(final GroupAdapter.MyViewHolder myViewHolder, final int i) {
        final GrupModel m = realmResults.get(i);
//        Glide.with(getContext()).load(m.get)
        myViewHolder.title.setText(m.getNama_grup());
        myViewHolder.subtitle.setVisibility(View.GONE);
    }

    @Override
    public void sessionChange() {

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
    public GroupAdapter(Context context, RealmResults<GrupModel> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate, false);
        session = new Session(getContext(), this);
        grupModel = new GrupModel();
        crud = new Crud(getContext(), grupModel);
    }
}
