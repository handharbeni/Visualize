package illiyin.mhandharbeni.visualize;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.stephentuso.welcome.WelcomeHelper;

import illiyin.mhandharbeni.visualize.mainpackage.account.LoginFragment;

public class MainActivity extends AppCompatActivity {
    private Fragment fragment;
    private WelcomeHelper welcomeHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        welcomeHelper = new WelcomeHelper(this, WelcomeScreen.class);
        welcomeHelper.show(savedInstanceState);

        init_fragment();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        welcomeHelper.onSaveInstanceState(outState);
    }

    private void init_fragment(){
        fragment = new LoginFragment();
        changeFragment();
    }

    private void changeFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainframe, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }
}
