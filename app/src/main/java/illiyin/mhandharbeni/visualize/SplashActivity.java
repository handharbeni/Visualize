package illiyin.mhandharbeni.visualize;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import illiyin.mhandharbeni.sessionlibrary.Session;
import illiyin.mhandharbeni.sessionlibrary.SessionListener;

/**
 * Created by root on 10/18/17.
 */

public class SplashActivity extends AppCompatActivity implements SessionListener {
    private Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(getApplicationContext(), this);
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
