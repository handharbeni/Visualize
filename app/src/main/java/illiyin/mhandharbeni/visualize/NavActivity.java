package illiyin.mhandharbeni.visualize;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;
import java.util.List;

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
import illiyin.mhandharbeni.visualize.navpackage.mainnav.ImageProfile;
import illiyin.mhandharbeni.visualize.navpackage.mainnav.MainNav;

public class NavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    String[] permissions = new String[]{
            Manifest.permission_group.LOCATION,
            Manifest.permission_group.STORAGE
    };
    private static final String TAG = "NavActivity";
    private Toolbar toolbar;
    private NavigationView navigationView;

    private Session session;
    private Crud crud;
    private AdapterModel adapterModel;
    private MaterialDialog dialogGrup, dialogContact, dialogPassword, dialogProfile, dialogGuideLines;

    private ServiceAdapter serviceAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nav);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkPermissions();
        fetch_modul();
        fetch_services();
        fetch_toolbar();
        fetch_flags();
        fetch_menu();
        init_first();
        fill_information_user();
        if (!session.getToken().equalsIgnoreCase("nothing")){
            turnGPSOn();
        }

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
        }else{

        }
    }
    private void fetch_flags(){
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        getWindow().setAttributes(attributes);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    private void fill_information_user(){
        View headerView = navigationView.getHeaderView(0);
        ImageView headerimage = headerView.findViewById(R.id.headerimage);
        TextView headername = headerView.findViewById(R.id.headername);
        TextView headerphone = headerView.findViewById(R.id.headerphone);

        String name = session.getCustomParams(Session.NAMA, "nothing");
        String phone = session.getCustomParams(Session.NOTELP, "nothing");
        String image = session.getCustomParams(Session.IMAGE, "http://enadcity.org/enadcity/wp-content/uploads/2017/02/profile-pictures.png");

        Glide.with(this).load(image).into(headerimage);
        headername.setText(name);
        headerphone.setText(phone);
        headerimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfileImage();
            }
        });
    }
    private void fetch_modul(){
        session = new Session(getApplicationContext(), new SessionListener() {
            @Override
            public void sessionChange() {
                fill_information_user();
            }
        });
        adapterModel = new AdapterModel(getApplicationContext());
        GrupModel grupModel = new GrupModel();
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
        }else if(id == R.id.nav_update_profile){
            modalChangeProfile();
        }else if(id == R.id.nav_update_password){
            modalChangePassword();
        }else if(id == R.id.nav_guideline){
            modalGuideline();
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
        assert v != null;
        EditText nama = v.findViewById(R.id.namagrup);
        EditText masaaktif = v.findViewById(R.id.masaaktifgrup);
        try {
            String response = adapterModel.create_grup(nama.getText().toString(), masaaktif.getText().toString());
            Log.d(TAG, "addGrup: "+response);
        } catch (Exception e) {
            FirebaseCrash.report(e);
        }
    }

    private void modalAddContact(){
        dialogContact = new MaterialDialog.Builder(this)
                .title(R.string.placeholder_hintaddcontact)
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
        assert v != null;
        EditText notelp = v.findViewById(R.id.notelpcontact);
        try {
            adapterModel.add_contact(notelp.getText().toString());
        } catch (Exception e) {
            FirebaseCrash.report(e);
        }
    }

    private void modalChangePassword(){
        dialogPassword = new MaterialDialog.Builder(this)
                .title("Change Password")
                .customView(R.layout.__nacactivity_mainnav_changepassword, true)
                .positiveText("Change")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        try {
                            changePassword();
                        } catch (Exception e) {
                            FirebaseCrash.report(e);
                        }
                    }
                })
                .build();
        dialogPassword.show();
    }
    private void changePassword() throws Exception {
        View v = dialogPassword.getCustomView();
        assert v != null;
        EditText oldpassword = v.findViewById(R.id.oldpassword);
        EditText newpassword = v.findViewById(R.id.newpassword);
        EditText repeatpassword = v.findViewById(R.id.repeatpassword);
        if (newpassword.getText().toString().equalsIgnoreCase(repeatpassword.getText().toString())){
            String ubahPassword = adapterModel.change_password(oldpassword.getText().toString(), newpassword.getText().toString());
            Toast.makeText(this, ubahPassword, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Silakan Login Kembali", Toast.LENGTH_SHORT).show();
            serviceAdapter.stopService();
            crud.deleteAll(GrupModel.class);
            crud.deleteAll(ContactModel.class);
            crud.deleteAll(ChatModel.class);
            crud.deleteAll(MemberModel.class);
            crud.deleteAll(MemberLocationModel.class);
            session.deleteSession();
            startActivity(new Intent(NavActivity.this, MainActivity.class));
            finish();
        }else{
            Toast.makeText(this, "Password Tidak Sama", Toast.LENGTH_SHORT).show();
        }
    }
    private void modalChangeProfile(){
        dialogProfile = new MaterialDialog.Builder(this)
                .title("Change Profile")
                .customView(R.layout.__navactivity_mainnav_updateprofile, true)
                .positiveText("Change")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        try {
                            changeProfile();
                        } catch (Exception e) {
                            FirebaseCrash.report(e);
                        }
                    }
                })
                .build();
        dialogProfile.show();

        View v = dialogProfile.getCustomView();
        assert v != null;
        EditText nama = v.findViewById(R.id.nama);
        EditText alamat = v.findViewById(R.id.alamat);
        EditText notelp = v.findViewById(R.id.notelp);
        nama.setText(session.getCustomParams(Session.NAMA, "nothing"));
        alamat.setText(session.getCustomParams(Session.ALAMAT, "nothing"));
        notelp.setText(session.getCustomParams(Session.NOTELP, "nothing"));
    }
    private void modalGuideline(){
        dialogGuideLines = new MaterialDialog.Builder(this)
                .title("Guide line")
                .customView(R.layout.__layout_mainnav_guidelines, true)
                .positiveText("Ok")
                .build();
        dialogGuideLines.show();
    }
    private void changeProfile() throws Exception {
        View v = dialogProfile.getCustomView();
        assert v != null;
        EditText nama = v.findViewById(R.id.nama);
        EditText alamat = v.findViewById(R.id.alamat);
        EditText notelp = v.findViewById(R.id.notelp);
//        EditText username = v.findViewById(R.id.username);
        String updateprofile = adapterModel.updateprofile(nama.getText().toString(), alamat.getText().toString(), notelp.getText().toString()/*, username.getText().toString()*/);
        session.setCustomParams(Session.NAMA, nama.getText().toString());
        session.setCustomParams(Session.ALAMAT, alamat.getText().toString());
        session.setCustomParams(Session.NOTELP, notelp.getText().toString());
        session.setCustomParams(Session.EMAIL, notelp.getText().toString());
        Toast.makeText(this, updateprofile, Toast.LENGTH_SHORT).show();
    }
    private void showProfileImage(){
        startActivity(new Intent(NavActivity.this, ImageProfile.class));
    }
    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }
}
