package illiyin.mhandharbeni.servicemodule.service.intentservice;

import android.app.IntentService;
import android.content.Intent;

import org.json.JSONException;

import illiyin.mhandharbeni.databasemodule.AdapterModel;

/**
 * Created by root on 10/26/17.
 */

public class ListMemberLocation extends IntentService {
    AdapterModel adapterModel;
    public ListMemberLocation() {
        super("List Member Location Service");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        adapterModel = new AdapterModel(getBaseContext());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            adapterModel.syncListMemberLocation();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}