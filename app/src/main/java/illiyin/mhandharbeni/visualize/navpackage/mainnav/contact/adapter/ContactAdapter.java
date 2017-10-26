package illiyin.mhandharbeni.visualize.navpackage.mainnav.contact.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import illiyin.mhandharbeni.databasemodule.ContactModel;
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

public class ContactAdapter extends RealmBasedRecyclerViewAdapter<ContactModel, ContactAdapter.MyViewHolder> implements SessionListener {
    private Session session;
    private ContactModel contactModel;
    private Crud crud;
    @Override
    public ContactAdapter.MyViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
        View v = inflater.inflate(R.layout.__navactivity_mainnav_itemlist, viewGroup, false);
        return new ContactAdapter.MyViewHolder((LinearLayout) v);
    }

    @Override
    public void onBindRealmViewHolder(final ContactAdapter.MyViewHolder myViewHolder, final int i) {
        final ContactModel m = realmResults.get(i);

        Glide.with(getContext())
                .load(m.getImage())
                .into(myViewHolder.image);
        myViewHolder.title.setText(m.getNama());
        myViewHolder.subtitle.setVisibility(View.INVISIBLE);
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
    public ContactAdapter(Context context, RealmResults<ContactModel> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate, false);
        session = new Session(getContext(), this);
        contactModel = new ContactModel();
        crud = new Crud(getContext(), contactModel);
    }
}

