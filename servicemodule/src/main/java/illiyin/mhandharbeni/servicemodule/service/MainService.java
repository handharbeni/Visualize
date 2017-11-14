package illiyin.mhandharbeni.servicemodule.service;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;

import java.util.Timer;
import java.util.TimerTask;

import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.TrackerSettings;
import illiyin.mhandharbeni.databasemodule.AdapterModel;
import illiyin.mhandharbeni.servicemodule.service.intentservice.ChatService;
import illiyin.mhandharbeni.servicemodule.service.intentservice.ContactService;
import illiyin.mhandharbeni.servicemodule.service.intentservice.GroupLocationService;
import illiyin.mhandharbeni.servicemodule.service.intentservice.GrupService;
import illiyin.mhandharbeni.servicemodule.service.intentservice.ListMemberLocation;
import illiyin.mhandharbeni.servicemodule.service.intentservice.ListMemberService;

import static android.content.ContentValues.TAG;


/**
 * Created by root on 17/07/17.
 */

public class MainService extends Service {
    public static Boolean serviceRunning = false;
    public static final long NOTIFY_INTERVAL = 1 * 1000;
    private Handler handler = new Handler();
    private Timer timer = null;

    @Override
    public void onCreate() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
        startTrack();
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
    private void startTrack(){
        if (    ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // You need to ask the user to enable the permissions
        } else {
            TrackerSettings settings =
                    new TrackerSettings()
                            .setUseGPS(true)
                            .setUseNetwork(true)
                            .setUsePassive(true)
                            .setTimeBetweenUpdates(1)
                            .setMetersBetweenUpdates(1);
            LocationTracker tracker = new LocationTracker(getBaseContext(), settings) {

                @Override
                public void onLocationFound(Location location) {
                    try {
                        AdapterModel adapterModel = new AdapterModel(getBaseContext());
                        String response = adapterModel.send_location(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onTimeout() {

                }
            };
            tracker.startListening();
        }
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
                    if (!checkIsRunning(ListMemberService.class)){
                        Intent is = new Intent(getBaseContext(), ListMemberService.class);
                        startService(is);
                    }
                    if (!checkIsRunning(ListMemberLocation.class)){
                        Intent is = new Intent(getBaseContext(), ListMemberLocation.class);
                        startService(is);
                    }
                    if (!checkIsRunning(GroupLocationService.class)){
                        Intent is = new Intent(getBaseContext(), GroupLocationService.class);
                        startService(is);
                    }
                }
            });
        }
    }
}
