package illiyin.mhandharbeni.visualize;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import illiyin.mhandharbeni.sessionlibrary.SessionListener;
import illiyin.mhandharbeni.sessionlibrary.Session;

/**
 * Created by root on 10/18/17.
 */

public class SplashActivity extends AppCompatActivity implements SessionListener {
    private Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_view);
    }
    private void turnGPSOn(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(!provider.contains("gps")){ //if gps is disabled
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        session = new Session(getApplicationContext(), this);
        if (!session.getToken().equalsIgnoreCase("nothing")){
            turnGPSOn();
        }

        if (session.getStateLogin().equalsIgnoreCase("true")){
            startActivity(new Intent(SplashActivity.this, NavActivity.class));
        }else{
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
        finish();
    }

    @Override
    public void sessionChange() {

    }
}
