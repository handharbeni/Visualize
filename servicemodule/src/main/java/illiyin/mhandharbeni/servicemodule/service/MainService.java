package illiyin.mhandharbeni.servicemodule.service;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import java.util.concurrent.TimeUnit;

import illiyin.mhandharbeni.databasemodule.AdapterModel;
import illiyin.mhandharbeni.servicemodule.service.intentservice.ContactService;
import illiyin.mhandharbeni.servicemodule.service.intentservice.GroupLocationService;
import illiyin.mhandharbeni.servicemodule.service.intentservice.GrupService;
import illiyin.mhandharbeni.servicemodule.service.intentservice.ListMemberLocation;
import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.TrackerSettings;
import illiyin.mhandharbeni.servicemodule.service.intentservice.ChatService;
import illiyin.mhandharbeni.servicemodule.service.intentservice.ListMemberService;
import rx.Scheduler;
import rx.functions.Action0;
import rx.schedulers.Schedulers;


/**
 * Created by root on 17/07/17.
 */

public class MainService extends Service {
    private static final String TAG = "MyLocationService";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 5f;

    private static int PERIODICALLY_CALL = 2 * 1000;
    private static int DELAY_CALL = 500;


    public static Boolean serviceRunning = false;

    @Override
    public void onCreate() {
        syncChat();
        syncContact();
        syncGrup();
        syncGrupLokasi();
        syncListMember();
        syncListMemberLokasi();
        startTrack();
        startTrack("S");
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
        initializeLocationManager();

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.PASSIVE_PROVIDER,
                    LOCATION_INTERVAL,
                    LOCATION_DISTANCE,
                    mLocationListeners[0]
            );
        } catch (IllegalArgumentException | java.lang.SecurityException ex) {
            FirebaseCrash.report(ex);
        }

        /*try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    LOCATION_INTERVAL,
                    LOCATION_DISTANCE,
                    mLocationListeners[1]
            );
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }*/
    }
    private void startTrack(String string) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            TrackerSettings settings =
                    new TrackerSettings()
                            .setUseGPS(true)
                            .setUseNetwork(true)
                            .setUsePassive(true)
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
            tracker.startListening();
        }
    }


    private void startTracks() {
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
    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            try {
                mLastLocation = new Location(provider);
                AdapterModel adapterModel = new AdapterModel(getBaseContext());
                adapterModel.send_location(String.valueOf(mLastLocation.getLatitude()), String.valueOf(mLastLocation.getLongitude()));
            } catch (Exception e) {
                FirebaseCrash.report(e);
            }

        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
            try{
                AdapterModel adapterModel = new AdapterModel(getBaseContext());
                adapterModel.send_location(String.valueOf(mLastLocation.getLatitude()), String.valueOf(mLastLocation.getLongitude()));
            } catch (Exception e) {
                FirebaseCrash.report(e);
            }

        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }
    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.PASSIVE_PROVIDER)
    };

    @Override
    public void onDestroy() {
//        Log.e(TAG, "onDestroy");
        serviceRunning = false;
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    FirebaseCrash.report(ex);
//                    Log.i(TAG, "fail to remove location listener, ignore", ex);
                }
            }
        }
    }
    private void initializeLocationManager() {
//        Log.e(TAG, "initializeLocationManager - LOCATION_INTERVAL: "+ LOCATION_INTERVAL + " LOCATION_DISTANCE: " + LOCATION_DISTANCE);
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}
