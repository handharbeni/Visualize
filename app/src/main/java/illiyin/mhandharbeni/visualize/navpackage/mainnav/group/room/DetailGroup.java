package illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.crash.FirebaseCrash;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import net.frederico.showtipsview.ShowTipsBuilder;
import net.frederico.showtipsview.ShowTipsView;
import net.frederico.showtipsview.ShowTipsViewInterface;

import illiyin.mhandharbeni.databasemodule.AdapterModel;
import illiyin.mhandharbeni.sessionlibrary.SessionListener;
import illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.fragment.MemberList;
import illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.fragment.PlaceList;
import illiyin.mhandharbeni.databasemodule.GrupModel;
import illiyin.mhandharbeni.realmlibrary.Crud;
import illiyin.mhandharbeni.sessionlibrary.Session;
import illiyin.mhandharbeni.visualize.R;
import illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.activity.RouteDestinations;
import illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.fragment.ChatList;
import illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.fragment.MapsList;
import io.realm.RealmResults;

public class DetailGroup extends AppCompatActivity {
    private static Integer TOOLTIP_MEMBER = 131;
    private static Integer TOOLTIP_MAP = 132;
    private static Integer TOOLTIP_MESSAGE = 133;
    private static Integer TOOLTIP_DESTINATIONS = 134;

    private static Integer TOOLTIP_FAB_INVITE = 1311;
    private static Integer TOOLTIP_FAB_CHAT = 1331;
    private static Integer TOOLTIP_FAB_ADDDESTI = 1341;
    private static Integer TOOLTIP_FAB_LISTDESTI = 1342;

    int PLACE_PICKER_REQUEST = 1;
    private Integer RESULT_OK = -1;

    private BottomNavigationViewEx navigation;

    private AdapterModel adapterModel;

    private Integer id;

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
                    Fragment fragment = new MemberList();
                    Bundle bundleFragment = new Bundle();
                    bundleFragment.putInt("id", id);
                    fragment.setArguments(bundleFragment);
                    changeFragment(fragment);
//                    showTooltipFabInvite();
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
//                    showTooltipFabChat();
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
//                    showTooltipFabAddDesti();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.__navactivity_mainnav_layout_detailgroup);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetch_modul();
        fetch_extras();
//        getActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fetch_title();
        fetch_element();
        fetch_event();
        fetch_startup();
        showTooltipTabMember();
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
        assert v != null;
        EditText notelp = v.findViewById(R.id.notelpcontact);
        try {
            String response = adapterModel.invite_member(String.valueOf(id), notelp.getText().toString());
        } catch (Exception e) {
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
        assert v != null;
        EditText message = v.findViewById(R.id.sendmessage);
        try {
            adapterModel.send_message(String.valueOf(id), message.getText().toString());
        } catch (Exception e) {
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
            assert v != null;
            Spinner spinnerType = v.findViewById(R.id.spinnerType);
            EditText alamat = v.findViewById(R.id.alamat);
            TextView tlatitude = v.findViewById(R.id.latitude);
            TextView tlongitude = v.findViewById(R.id.longitude);
            type = spinnerType.getSelectedItem().toString();
            lokasi = alamat.getText().toString();
            latitude = tlatitude.getText().toString();
            longitude = tlongitude.getText().toString();
            adapterModel.add_location_grup(String.valueOf(id), lokasi, latitude, longitude, prioritas, type);
        } catch (Exception e) {
            FirebaseCrash.report(e);
        }
    }
    private void doPickLocation(String sAlamat, String sLatitude, String sLongitude){
        View v = dialogAddLocation.getCustomView();
        assert v != null;
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
                placePicker();
            }
        });
        fabviewdestinations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Session session = new Session(getApplicationContext(), new SessionListener() {
                    @Override
                    public void sessionChange() {

                    }
                });

                Intent i = new Intent(getApplicationContext(), RouteDestinations.class);
                i.putExtra("key_id", String.valueOf(id));
                i.putExtra("key_id_int", id);
                startActivity(i);
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
            FirebaseCrash.report(e);
        } catch (GooglePlayServicesNotAvailableException e) {
            FirebaseCrash.report(e);
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
            }
        }
    }
    private void showTooltipTabMember(){
        ShowTipsView showtips = new ShowTipsBuilder(this)
                .setTarget(findViewById(R.id.navigation_member))
                .setTitle("Menu of Member")
                .setDescription("List of members that have been invited in this group.")
                .setDelay(1000)
                .displayOneTime(TOOLTIP_MEMBER)
                .build();

        showtips.show(this);
        showtips.setCallback(new ShowTipsViewInterface() {
            @Override
            public void gotItClicked() {
                showTooltipTabMaps();
            }
        });
    }
    private void showTooltipTabMaps(){
        ShowTipsView showtips = new ShowTipsBuilder(this)
                .setTarget(findViewById(R.id.navigation_maps))
                .setTitle("Menu of Maps")
                .setDescription("Track the location of each member in this group.")
                .setDelay(1000)
                .displayOneTime(TOOLTIP_MAP)
                .build();

        showtips.show(this);
        showtips.setCallback(new ShowTipsViewInterface() {
            @Override
            public void gotItClicked() {
                showTooltipChat();
            }
        });

    }
    private void showTooltipChat(){
        ShowTipsView showtips = new ShowTipsBuilder(this)
                .setTarget(findViewById(R.id.navigation_chat))
                .setTitle("Menu of Chat")
                .setDescription("Sending and receiving chats in this group.")
                .setDelay(1000)
                .displayOneTime(TOOLTIP_MESSAGE)
                .build();

        showtips.show(this);
        showtips.setCallback(new ShowTipsViewInterface() {
            @Override
            public void gotItClicked() {
                showTooltipDestination();
            }
        });

    }
    private void showTooltipDestination(){
        ShowTipsView showtips = new ShowTipsBuilder(this)
                .setTarget(findViewById(R.id.navigation_lokasi))
                .setTitle("Menu of Destinations")
                .setDescription("List of locations that will be the destination for the members in the group.")
                .setDelay(1000)
                .displayOneTime(TOOLTIP_DESTINATIONS)
                .build();

        showtips.show(this);
    }
    private void showTooltipFabInvite(){
        ShowTipsView showtips = new ShowTipsBuilder(this)
                .setTarget(findViewById(R.id.fabinvitemember))
                .setTitle("Tambah Member")
                .setDescription("Klik disini untuk mengundang member")
                .setDelay(1000)
                .displayOneTime(TOOLTIP_FAB_INVITE)
                .build();

        showtips.show(this);
    }
    private void showTooltipFabChat(){
        ShowTipsView showtips = new ShowTipsBuilder(this)
                .setTarget(findViewById(R.id.fabsendmessage))
                .setTitle("Kirim Pesan")
                .setDescription("Klik disini untuk saling mengirim pesan")
                .setDelay(1000)
                .displayOneTime(TOOLTIP_FAB_CHAT)
                .build();

        showtips.show(this);
    }
    private void showTooltipFabAddDesti(){
        ShowTipsView showtips = new ShowTipsBuilder(this)
                .setTarget(findViewById(R.id.fabaddlocation))
                .setTitle("Tambah Tujuan")
                .setDescription("Tambahkan lokasi tujuan disini")
                .setDelay(1000)
                .displayOneTime(TOOLTIP_FAB_ADDDESTI)
                .build();

        showtips.show(this);
        showtips.setCallback(new ShowTipsViewInterface() {
            @Override
            public void gotItClicked() {
                showTooltipFabListDesti();
            }
        });
    }
    private void showTooltipFabListDesti(){
        ShowTipsView showtips = new ShowTipsBuilder(this)
                .setTarget(findViewById(R.id.fabviewdestinations))
                .setTitle("Daftar Lokasi Tujuan")
                .setDescription("Melihat daftar tujuan di dalam maps dengan rute yang sudah tersedia")
                .setDelay(1000)
                .displayOneTime(TOOLTIP_FAB_LISTDESTI)
                .build();

        showtips.show(this);
    }


}
