package illiyin.mhandharbeni.servicemodule.service.intentservice;

import android.app.IntentService;
import android.content.Intent;

import org.json.JSONException;

import illiyin.mhandharbeni.databasemodule.AdapterModel;
import illiyin.mhandharbeni.servicemodule.service.MainService;

/**
 * Created by root on 10/26/17.
 */

public class ListMemberService extends IntentService {
    public static final String
            ACTION_LOCATION_BROADCAST = MainService.class.getName();
    AdapterModel adapterModel;
    public ListMemberService() {
        super("List Member Service");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        adapterModel = new AdapterModel(getBaseContext());
        sendBroadCast();
        return super.onStartCommand(intent, flags, startId);
    }
    public void sendBroadCast(){
        this.sendBroadcast(new Intent().setAction("SERVICE MENU").putExtra("MODE", "UPDATE CHAT"));
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            adapterModel.syncListMember();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
