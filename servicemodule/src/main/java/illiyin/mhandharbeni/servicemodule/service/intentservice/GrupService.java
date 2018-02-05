package illiyin.mhandharbeni.servicemodule.service.intentservice;

import android.app.IntentService;
import android.content.Intent;

import com.google.firebase.crash.FirebaseCrash;

import illiyin.mhandharbeni.databasemodule.AdapterModel;

/**
 * Created by root on 17/07/17.
 */

public class GrupService extends IntentService {
    AdapterModel adapterModel;
    public GrupService() {
        super("Grup Service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
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
        } catch (Exception e) {
            FirebaseCrash.report(e);
        }
    }
}
