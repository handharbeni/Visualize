package illiyin.mhandharbeni.servicemodule.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

import illiyin.mhandharbeni.servicemodule.service.intentservice.ChatService;
import illiyin.mhandharbeni.servicemodule.service.intentservice.ContactService;
import illiyin.mhandharbeni.servicemodule.service.intentservice.GrupService;

/**
 * Created by root on 17/07/17.
 */

public class MainService extends Service {
    public static Boolean serviceRunning = false;
    public static final long NOTIFY_INTERVAL = 2 * 1000;
    private Handler handler = new Handler();
    private Timer timer = null;

    @Override
    public void onCreate() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceRunning = true;
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        serviceRunning = false;
        super.onDestroy();
    }
    private boolean checkIsRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    class TimeDisplayTimerTask extends TimerTask {
        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (!checkIsRunning(ChatService.class)){
                        Intent is = new Intent(getBaseContext(), ChatService.class);
                        startService(is);
                    }
                    if (!checkIsRunning(ContactService.class)){
                        Intent is = new Intent(getBaseContext(), ContactService.class);
                        startService(is);
                    }
                    if (!checkIsRunning(GrupService.class)){
                        Intent is = new Intent(getBaseContext(), GrupService.class);
                        startService(is);
                    }
                }
            });
        }
    }
}
