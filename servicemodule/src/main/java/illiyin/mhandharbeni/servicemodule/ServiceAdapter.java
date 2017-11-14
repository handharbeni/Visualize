package illiyin.mhandharbeni.servicemodule;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import illiyin.mhandharbeni.servicemodule.service.MainService;

import static android.content.ContentValues.TAG;

/**
 * Created by root on 17/07/17.
 */

public class ServiceAdapter {
    Context context;

    public ServiceAdapter(Context context) {
        this.context = context;
    }

    public void startService(){
        if (!MainService.serviceRunning){
            context.startService(new Intent(context, MainService.class));
        }
    }
}
