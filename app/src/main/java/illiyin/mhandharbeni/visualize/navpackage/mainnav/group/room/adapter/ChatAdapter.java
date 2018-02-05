package illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import illiyin.mhandharbeni.realmlibrary.Crud;
import illiyin.mhandharbeni.sessionlibrary.Session;
import illiyin.mhandharbeni.sessionlibrary.SessionListener;
import illiyin.mhandharbeni.databasemodule.ChatModel;
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
    public ChatAdapter.MyViewHolder onCreateRealmViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = inflater.inflate(illiyin.mhandharbeni.visualize.R.layout.__navactivity_mainnav_itemchat, viewGroup, false);
        return new ChatAdapter.MyViewHolder((LinearLayout) v);
    }

    @Override
    public void onBindRealmViewHolder(@NonNull final ChatAdapter.MyViewHolder myViewHolder, final int i) {
        final ChatModel m = realmResults.get(i);
        String nama = session.getCustomParams("NAMA", "nothing");
        assert m != null;
        if (m.getNama_user().equalsIgnoreCase(nama)){
            /*layoutme*/
            myViewHolder.layoutfrom.setVisibility(View.GONE);
            myViewHolder.contentmessageleft.setText(m.getText());
            Glide.with(getContext()).load(m.getImage_user()).into(myViewHolder.imagemessageleft);
        }else{
            /*layoutfrom*/
            myViewHolder.layoutme.setVisibility(View.GONE);
            myViewHolder.contentmessageright.setText(m.getText());
            Glide.with(getContext()).load(m.getImage_user()).into(myViewHolder.imagemessageright);
        }

    }
    class MyViewHolder extends RealmViewHolder {
        LinearLayout layoutfrom, layoutme;
        ImageView imagemessageleft, imagemessageright;
        TextView contentmessageleft, contentmessageright;
        MyViewHolder(LinearLayout container) {
            super(container);
            this.layoutfrom= container.findViewById(illiyin.mhandharbeni.visualize.R.id.layoutfrom);
            this.layoutme = container.findViewById(illiyin.mhandharbeni.visualize.R.id.layoutme);
            this.imagemessageleft = container.findViewById(illiyin.mhandharbeni.visualize.R.id.imagemessageleft);
            this.imagemessageright = container.findViewById(illiyin.mhandharbeni.visualize.R.id.imagemessageright);
            this.contentmessageleft = container.findViewById(illiyin.mhandharbeni.visualize.R.id.contentmessageleft);
            this.contentmessageright = container.findViewById(illiyin.mhandharbeni.visualize.R.id.contentmessageright);
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
