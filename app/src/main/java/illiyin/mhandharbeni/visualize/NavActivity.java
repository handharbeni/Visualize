package illiyin.mhandharbeni.visualize;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONException;

import illiyin.mhandharbeni.databasemodule.AdapterModel;
import illiyin.mhandharbeni.databasemodule.ChatModel;
import illiyin.mhandharbeni.databasemodule.ContactModel;
import illiyin.mhandharbeni.databasemodule.GrupModel;
import illiyin.mhandharbeni.databasemodule.MemberLocationModel;
import illiyin.mhandharbeni.databasemodule.MemberModel;
import illiyin.mhandharbeni.realmlibrary.Crud;
import illiyin.mhandharbeni.servicemodule.ServiceAdapter;
import illiyin.mhandharbeni.sessionlibrary.Session;
import illiyin.mhandharbeni.sessionlibrary.SessionListener;
import illiyin.mhandharbeni.visualize.navpackage.mainnav.MainNav;

public class NavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "NavActivity";
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    private Session session;
    private GrupModel grupModel;
    private Crud crud;
    private AdapterModel adapterModel;
    private MaterialDialog dialogGrup, dialogContact;

    private ServiceAdapter serviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetch_modul();
        fetch_services();

        setContentView(R.layout.activity_nav);

        fetch_toolbar();
        fetch_menu();

        init_first();
    }
    private void fetch_services(){
        serviceAdapter = new ServiceAdapter(getApplicationContext());
        serviceAdapter.startService();
    }
    private void init_first(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.navframe, new MainNav());
        ft.commit();
    }
    private void fetch_toolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void fetch_menu(){
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void fetch_modul(){
        session = new Session(getApplicationContext(), new SessionListener() {
            @Override
            public void sessionChange() {

            }
        });
        adapterModel = new AdapterModel(getApplicationContext());
        grupModel = new GrupModel();
        crud = new Crud(getApplicationContext(), grupModel);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_group) {
            modalAddGrup();
            return true;
        }else if(id == R.id.add_contact){
            modalAddContact();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout){
            /*do logout*/
            crud.deleteAll(GrupModel.class);
            crud.deleteAll(ContactModel.class);
            crud.deleteAll(ChatModel.class);
            crud.deleteAll(MemberModel.class);
            crud.deleteAll(MemberLocationModel.class);
            session.deleteSession();
            startActivity(new Intent(NavActivity.this, MainActivity.class));
            finish();
        }else if (id == R.id.add_group){
            modalAddGrup();
        }else if(id == R.id.add_contact){
            modalAddContact();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void modalAddGrup(){
        dialogGrup = new MaterialDialog.Builder(this)
                .title(R.string.placeholder_hintcreategrup)
                .customView(R.layout.__navactivity_mainnav_addgroup, true)
                .positiveText(R.string.placeholder_buttoncreategrup)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        addGrup();
                    }
                })
                .build();
        dialogGrup.show();
    }

    private void addGrup(){
        View v = dialogGrup.getCustomView();
        EditText nama = v.findViewById(R.id.namagrup);
        EditText masaaktif = v.findViewById(R.id.masaaktifgrup);
        try {
            String response = adapterModel.create_grup(nama.getText().toString(), masaaktif.getText().toString());
            Log.d(TAG, "addGrup: "+response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void modalAddContact(){
        dialogContact = new MaterialDialog.Builder(this)
                .title(R.string.placeholder_hintcreategrup)
                .customView(R.layout.__navactivity_mainnav_addcontact, true)
                .positiveText(R.string.placeholder_buttonaddcontact)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        addContact();
                    }
                })
                .build();
        dialogContact.show();
    }

    private void addContact(){
        View v = dialogContact.getCustomView();
        EditText notelp = v.findViewById(R.id.notelpcontact);
        try {
            adapterModel.add_contact(notelp.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
