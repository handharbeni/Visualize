package illiyin.mhandharbeni.servicemodule.service;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONException;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.TrackerSettings;
import illiyin.mhandharbeni.databasemodule.AdapterModel;
import illiyin.mhandharbeni.servicemodule.service.intentservice.ChatService;
import illiyin.mhandharbeni.servicemodule.service.intentservice.ContactService;
import illiyin.mhandharbeni.servicemodule.service.intentservice.GroupLocationService;
import illiyin.mhandharbeni.servicemodule.service.intentservice.GrupService;
import illiyin.mhandharbeni.servicemodule.service.intentservice.ListMemberLocation;
import illiyin.mhandharbeni.servicemodule.service.intentservice.ListMemberService;
import illiyin.mhandharbeni.sessionlibrary.Session;
import illiyin.mhandharbeni.sessionlibrary.SessionListener;
import rx.Scheduler;
import rx.functions.Action0;
import rx.schedulers.Schedulers;


/**
 * Created by root on 17/07/17.
 */

public class MainService extends Service {
    private static int PERIODICALLY_CALL = 2 * 1000;
    private static int DELAY_CALL = 500;


    public static Boolean serviceRunning = false;
    private Handler handler = new Handler();

    @Override
    public void onCreate() {
        syncChat();
        syncContact();
        syncGrup();
        syncGrupLokasi();
        syncListMember();
        syncListMemberLokasi();
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

    private void justTrack() {
        TrackerSettings settings =
                new TrackerSettings()
                        .setUseGPS(true)
                        .setUseNetwork(false)
                        .setUsePassive(false)
                        .setTimeBetweenUpdates(100)
                        .setMetersBetweenUpdates(5);
        LocationTracker tracker = new LocationTracker(getBaseContext(), settings) {

            @Override
            public void onLocationFound(Location location) {
                try {
                    AdapterModel adapterModel = new AdapterModel(getBaseContext());
                    adapterModel.send_location(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                } catch (Exception e) {
                    FirebaseCrash.report(e);
                }
            }

            @Override
            public void onTimeout() {

            }
        };

        if (!tracker.isListening()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }else{
                    tracker.startListening();
                }
            }
        }
    }
    private void startTrack() {
        int off = 0;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                off = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
            }
            if (off == 0) {
                Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(onGPS);
            } else {
                TrackerSettings settings =
                        new TrackerSettings()
                                .setUseGPS(true)
                                .setUseNetwork(true)
                                .setUsePassive(false)
                                .setTimeBetweenUpdates(100)
                                .setMetersBetweenUpdates(5);
                LocationTracker tracker = new LocationTracker(getBaseContext(), settings) {

                    @Override
                    public void onLocationFound(@NonNull Location location) {
                        try {
                            AdapterModel adapterModel = new AdapterModel(getBaseContext());
                            adapterModel.send_location(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                        } catch (Exception e) {
                            FirebaseCrash.report(e);
                        }
                    }

                    @Override
                    public void onTimeout() {

                    }
                };
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    }
                }
                tracker.startListening();
            }
        } catch (Exception e) {
            FirebaseCrash.report(e);
        }
    }
    @Override
    public void onDestroy() {
        serviceRunning = false;
        super.onDestroy();
    }
    private boolean checkIsRunning(Class<?> serviceClass) {
        try {
            ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            assert manager != null;
            for (ActivityManager.RunningServiceInfo service : manager
                    .getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }catch (Exception e){
            FirebaseCrash.report(e);
            return false;
        }
        return false;
    }
    public void syncChat(){
        Action0 action0 = new Action0() {
            @Override
            public void call() {
                if (!checkIsRunning(ChatService.class)){
                    Intent is = new Intent(getBaseContext(), ChatService.class);
                    startService(is);
                }
            }
        };
        Scheduler.Worker worker = Schedulers.newThread().createWorker();
        worker.schedulePeriodically(action0, DELAY_CALL, PERIODICALLY_CALL, TimeUnit.MILLISECONDS);
    }
    public void syncContact(){
        Action0 action0 = new Action0() {
            @Override
            public void call() {
                if (!checkIsRunning(ContactService.class)){
                    Intent is = new Intent(getBaseContext(), ContactService.class);
                    startService(is);
                }
            }
        };
        Scheduler.Worker worker = Schedulers.newThread().createWorker();
        worker.schedulePeriodically(action0, DELAY_CALL, PERIODICALLY_CALL, TimeUnit.MILLISECONDS);
    }
    public void syncGrup(){
        Action0 action0 = new Action0() {
            @Override
            public void call() {
                if (!checkIsRunning(GrupService.class)){
                    Intent is = new Intent(getBaseContext(), GrupService.class);
                    startService(is);
                }
            }
        };
        Scheduler.Worker worker = Schedulers.newThread().createWorker();
        worker.schedulePeriodically(action0, DELAY_CALL, PERIODICALLY_CALL, TimeUnit.MILLISECONDS);
    }
    public void syncListMember(){
        Action0 action0 = new Action0() {
            @Override
            public void call() {
                if (!checkIsRunning(ListMemberService.class)){
                    Intent is = new Intent(getBaseContext(), ListMemberService.class);
                    startService(is);
                }
            }
        };
        Scheduler.Worker worker = Schedulers.newThread().createWorker();
        worker.schedulePeriodically(action0, DELAY_CALL, PERIODICALLY_CALL, TimeUnit.MILLISECONDS);
    }
    public void syncListMemberLokasi(){
        Action0 action0 = new Action0() {
            @Override
            public void call() {
                if (!checkIsRunning(ListMemberLocation.class)){
                    Intent is = new Intent(getBaseContext(), ListMemberLocation.class);
                    startService(is);
                }
            }
        };
        Scheduler.Worker worker = Schedulers.newThread().createWorker();
        worker.schedulePeriodically(action0, DELAY_CALL, PERIODICALLY_CALL, TimeUnit.MILLISECONDS);
    }
    public void syncGrupLokasi(){
        Action0 action0 = new Action0() {
            @Override
            public void call() {
                if (!checkIsRunning(GroupLocationService.class)){
                    Intent is = new Intent(getBaseContext(), GroupLocationService.class);
                    startService(is);
                }
            }
        };
        Scheduler.Worker worker = Schedulers.newThread().createWorker();
        worker.schedulePeriodically(action0, DELAY_CALL, PERIODICALLY_CALL, TimeUnit.MILLISECONDS);
    }

}
