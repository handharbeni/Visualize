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
    AdapterModel adapterModel;
    public ListMemberService() {
        super("List Member Service");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        adapterModel = new AdapterModel(getBaseContext());
        return super.onStartCommand(intent, flags, startId);
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
