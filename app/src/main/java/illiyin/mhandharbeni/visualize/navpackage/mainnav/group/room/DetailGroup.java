package illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONException;

import java.lang.reflect.Field;

import illiyin.mhandharbeni.databasemodule.AdapterModel;
import illiyin.mhandharbeni.databasemodule.GrupModel;
import illiyin.mhandharbeni.realmlibrary.Crud;
import illiyin.mhandharbeni.visualize.R;
import illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.fragment.ChatList;
import illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.fragment.MapsList;
import illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.fragment.MemberList;
import illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.fragment.PlaceList;
import io.realm.RealmResults;

public class DetailGroup extends AppCompatActivity {
    int PLACE_PICKER_REQUEST = 1;
    private Integer RESULT_OK = -1, RESULT_CANCEL=0;

    private static final String TAG = "DetailGrup";
    private BottomNavigationViewEx navigation;

    private AdapterModel adapterModel;

    private Integer id;

    private Fragment fragment;
    private Bundle bundleFragment;

    private FloatingActionButton fabinvitemember, fabsendmessage, fabaddlocation, fabviewdestinations;

    private MaterialDialog dialogInvite, dialogSendChat, dialogAddLocation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_member:
                    hideAllFab();
                    fabinvitemember.show();
                    fragment = new MemberList();
                    bundleFragment = new Bundle();
                    bundleFragment.putInt("id", id);
                    fragment.setArguments(bundleFragment);
                    changeFragment(fragment);
                    return true;
                case R.id.navigation_maps:
                    hideAllFab();
                    fragment = new MapsList();
                    bundleFragment = new Bundle();
                    bundleFragment.putInt("id", id);
                    fragment.setArguments(bundleFragment);
                    changeFragment(fragment);
                    return true;
                case R.id.navigation_chat:
                    hideAllFab();
                    fabsendmessage.show();
                    fragment = new ChatList();
                    bundleFragment = new Bundle();
                    bundleFragment.putInt("id", id);
                    fragment.setArguments(bundleFragment);
                    changeFragment(fragment);
                    return true;
                case R.id.navigation_lokasi:
                    hideAllFab();
                    fabaddlocation.show();
                    fabviewdestinations.show();
                    fragment = new PlaceList();
                    bundleFragment = new Bundle();
                    bundleFragment.putInt("id", id);
                    fragment.setArguments(bundleFragment);
                    changeFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetch_modul();
        fetch_extras();
        setContentView(R.layout.__navactivity_mainnav_layout_detailgroup);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fetch_title();
        fetch_element();
        fetch_event();
        fetch_startup();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        onBackPressed();
        return true;
    }
    private void invite_member(){
        dialogInvite = new MaterialDialog.Builder(this)
                .title(R.string.placeholder_invitemember)
                .customView(R.layout.__navactivity_mainnav_invitemember, true)
                .positiveText(R.string.placeholder_kirim)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        doInvite();
                    }
                })
                .build();
        dialogInvite.show();
    }
    private void doInvite(){
        View v = dialogInvite.getCustomView();
        EditText notelp = v.findViewById(R.id.notelpcontact);
        try {
            String response = adapterModel.invite_member(String.valueOf(id), notelp.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void send_message(){
        dialogSendChat = new MaterialDialog.Builder(this)
                .title(R.string.placeholder_sendmessage)
                .customView(R.layout.__navactivity_mainnav_sendmessage, true)
                .positiveText(R.string.placeholder_kirim)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        doSend();
                    }
                })
                .build();
        dialogSendChat.show();
    }
    private void doSend(){
        View v = dialogSendChat.getCustomView();
        EditText message = v.findViewById(R.id.sendmessage);
        try {
            String response = adapterModel.send_message(String.valueOf(id), message.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void add_location(){
        dialogAddLocation = new MaterialDialog.Builder(this)
                .title(R.string.placeholder_adddestination)
                .customView(R.layout.__navactivity_mainnav_addlocation, true)
                .positiveText(R.string.placeholder_kirim)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        doSaveLocation();
                    }
                })
                .build();
        dialogAddLocation.show();
    }
    private void doSaveLocation(){
        try {
            String lokasi = null, latitude = null, longitude = null, prioritas = "0", type = null;
            View v = dialogAddLocation.getCustomView();
            Spinner spinnerType = v.findViewById(R.id.spinnerType);
            EditText alamat = v.findViewById(R.id.alamat);
            TextView tlatitude = v.findViewById(R.id.latitude);
            TextView tlongitude = v.findViewById(R.id.longitude);
            type = spinnerType.getSelectedItem().toString();
            lokasi = alamat.getText().toString();
            latitude = tlatitude.getText().toString();
            longitude = tlongitude.getText().toString();
            adapterModel.add_location_grup(String.valueOf(id), lokasi, latitude, longitude, prioritas, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void doPickLocation(String sAlamat, String sLatitude, String sLongitude){
        View v = dialogAddLocation.getCustomView();
        EditText alamat = v.findViewById(R.id.alamat);
        TextView lati = v.findViewById(R.id.latitude);
        TextView longi = v.findViewById(R.id.longitude);
        lati.setText(sLatitude);
        longi.setText(sLongitude);
        alamat.setText(sAlamat);
    }
    private void fetch_title(){
        GrupModel gm = new GrupModel();
        Crud crud = new Crud(this, gm);
        RealmResults results = crud.read("id", id);
        GrupModel getData = (GrupModel) results.get(0);
        getSupportActionBar().setTitle(getData.getNama_grup());
    }
    private void fetch_extras(){
        Bundle b = getIntent().getExtras();
        id = 0;
        if(b != null)
            id = b.getInt("id");

    }
    @SuppressLint("RestrictedApi")
    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            //Timber.e(e, "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            //Timber.e(e, "Unable to change value of shift mode");
        }
    }
    private void fetch_modul(){
        adapterModel = new AdapterModel(getApplicationContext());
    }

    private void fetch_element(){
        fabinvitemember = (FloatingActionButton) findViewById(R.id.fabinvitemember);
        fabsendmessage = (FloatingActionButton) findViewById(R.id.fabsendmessage);
        fabaddlocation = (FloatingActionButton) findViewById(R.id.fabaddlocation);
        fabviewdestinations = (FloatingActionButton) findViewById(R.id.fabviewdestinations);
        hideAllFab();

        navigation = (BottomNavigationViewEx) findViewById(R.id.navigation);
        navigation.enableAnimation(false);
        navigation.enableItemShiftingMode(false);
        navigation.enableShiftingMode(false);

        navigation.setTextSize(12);
//        disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private void fetch_event(){
        fabinvitemember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invite_member();
            }
        });
        fabsendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_message();
            }
        });
        fabaddlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                add_location();
                placePicker();
            }
        });
        fabviewdestinations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void hideAllFab(){
        fabinvitemember.hide();
        fabsendmessage.hide();
        fabaddlocation.hide();
        fabviewdestinations.hide();
    }

    private void fetch_startup(){
        View view = navigation.findViewById(R.id.navigation_member);
        view.performClick();
    }

    private void changeFragment(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.detailmainframe, fragment);
        ft.commit();
    }
    private void placePicker(){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                add_location();
                doPickLocation(String.valueOf(place.getAddress()), String.valueOf(place.getLatLng().latitude), String.valueOf(place.getLatLng().longitude));

//                session.setCustomParams(LATDESTI, String.valueOf(place.getLatLng().latitude));
//                session.setCustomParams(LONGDESTI, String.valueOf(place.getLatLng().longitude));
//                session.setCustomParams(ADDRESSDESTI, String.valueOf(place.getAddress()));
//                session.setCustomParams(ZIPCODEDESTI, String.valueOf(place.getLocale()));
//                calculateDistance();
            }
        }
    }

}
