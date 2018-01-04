package illiyin.mhandharbeni.servicemodule.service.intentservice;

import android.app.IntentService;
import android.content.Intent;

import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONException;

import illiyin.mhandharbeni.databasemodule.AdapterModel;

/**
 * Created by root on 11/14/17.
 */

public class GroupLocationService extends IntentService {
    AdapterModel adapterModel;
    public GroupLocationService() {
        super("Grup Location Service");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        adapterModel = new AdapterModel(getBaseContext());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            adapterModel.syncDestinationGroup();
        } catch (Exception e) {
            FirebaseCrash.report(e);
        }
    }
}
