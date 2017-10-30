package illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import illiyin.mhandharbeni.databasemodule.ChatModel;
import illiyin.mhandharbeni.databasemodule.MemberModel;
import illiyin.mhandharbeni.realmlibrary.Crud;
import illiyin.mhandharbeni.sessionlibrary.Session;
import illiyin.mhandharbeni.sessionlibrary.SessionListener;
import illiyin.mhandharbeni.visualize.R;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by root on 10/29/17.
 */

public class ChatAdapter extends RealmBasedRecyclerViewAdapter<ChatModel, ChatAdapter.MyViewHolder> {
    private ChatModel chatModel;
    private Crud crud;
    private Session session;


    @Override
    public ChatAdapter.MyViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
        View v = inflater.inflate(R.layout.__navactivity_mainnav_itemchat, viewGroup, false);
        return new ChatAdapter.MyViewHolder((LinearLayout) v);
    }

    @Override
    public void onBindRealmViewHolder(final ChatAdapter.MyViewHolder myViewHolder, final int i) {
        final ChatModel m = realmResults.get(i);
        String nama = session.getCustomParams("NAMA", "nothing");
//        if (m.getNama_user().equalsIgnoreCase(nama)){
//            /*layoutme*/
//            myViewHolder.layoutfrom.setVisibility(View.GONE);
//        }else{
//            /*layoutfrom*/
//            myViewHolder.layoutme.setVisibility(View.GONE);
//        }
        myViewHolder.contentmessage.setText(m.getText());
        Glide.with(getContext()).load(m.getImage_user()).into(myViewHolder.imagemessage);

    }
    public class MyViewHolder extends RealmViewHolder {
        LinearLayout layoutfrom, layoutme;
        ImageView imagemessage;
        TextView contentmessage;
        public MyViewHolder(LinearLayout container) {
            super(container);
            this.layoutfrom= container.findViewById(R.id.layoutfrom);
            this.layoutme = container.findViewById(R.id.layoutme);
            this.imagemessage = container.findViewById(R.id.imagemessage);
            this.contentmessage = container.findViewById(R.id.contentmessage);
        }
    }
    public ChatAdapter(Context context, RealmResults<ChatModel> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate, false);
        chatModel = new ChatModel();
        crud = new Crud(getContext(), chatModel);
        session = new Session(getContext(), new SessionListener() {
            @Override
            public void sessionChange() {

            }
        });
    }
}
