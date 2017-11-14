package illiyin.mhandharbeni.servicemodule.service.intentservice;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;

import illiyin.mhandharbeni.databasemodule.AdapterModel;
import illiyin.mhandharbeni.servicemodule.service.MainService;

import static android.content.ContentValues.TAG;

/**
 * Created by root on 17/07/17.
 */

public class GrupService extends IntentService {
    AdapterModel adapterModel;
    public GrupService() {
        super("Grup Service");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        adapterModel = new AdapterModel(getBaseContext());
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            adapterModel.syncGrup();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
