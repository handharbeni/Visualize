package illiyin.mhandharbeni.servicemodule.service.intentservice;

import android.app.IntentService;
import android.content.Intent;

import com.google.firebase.crash.FirebaseCrash;

import illiyin.mhandharbeni.databasemodule.AdapterModel;

/**
 * Created by root on 9/21/17.
 */

public class ContactService extends IntentService {
    AdapterModel adapterModel;
    public ContactService() {
        super("Contact Service");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        adapterModel = new AdapterModel(getBaseContext());
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            adapterModel.syncContact();
        } catch (Exception e) {
            FirebaseCrash.report(e);
        }
    }
}
