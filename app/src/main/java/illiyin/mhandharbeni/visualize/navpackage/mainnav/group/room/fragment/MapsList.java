package illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crash.FirebaseCrash;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import illiyin.mhandharbeni.databasemodule.MemberLocationModel;
import illiyin.mhandharbeni.realmlibrary.Crud;
import illiyin.mhandharbeni.sessionlibrary.Session;
import illiyin.mhandharbeni.sessionlibrary.SessionListener;
import illiyin.mhandharbeni.utilslibrary.Address;
import illiyin.mhandharbeni.visualize.utils.InfoWindowContent;
import illiyin.mhandharbeni.visualize.utils.model.InfoWindowData;
import io.realm.RealmResults;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by root on 10/25/17.
 */

public class MapsList extends Fragment implements OnMapReadyCallback, SessionListener {

    private MapView mMapView;
    private GoogleMap googleMaps;
    private Integer id;
    private Session session;
    private LatLngBounds.Builder builder;
    private GoogleApiClient googleApiClient;
    private int REQUEST_CHECK_SETTINGS = 123;
    final static int REQUEST_LOCATION = 199;
    String[] permissions = new String[]{
            Manifest.permission_group.LOCATION,
            Manifest.permission_group.STORAGE
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(illiyin.mhandharbeni.visualize.R.layout.__navactivity_mainnav_layout_group_maps, container,
                false);
        mMapView = v.findViewById(illiyin.mhandharbeni.visualize.R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetch_extras();
        fetch_module();

        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            FirebaseCrash.report(e);
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);

        if (!session.getToken().equalsIgnoreCase("nothing")){
//            turnGPSOn();
            checkGps();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        fetch_extras();
//        fetch_module();
//
//        mMapView.onResume();
//
//        try {
//            MapsInitializer.initialize(getActivity().getApplicationContext());
//        } catch (Exception e) {
//            FirebaseCrash.report(e);
//            e.printStackTrace();
//        }
//
//        mMapView.getMapAsync(this);
//
//        if (!session.getToken().equalsIgnoreCase("nothing")){
////            turnGPSOn();
//            checkGps();
//        }

    }

    private void fetch_module(){
        session = new Session(getActivity().getApplicationContext(), this);
        builder = new LatLngBounds.Builder();
    }
    private void fetch_extras(){
        Bundle args = getArguments();
        id = args.getInt("id", 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        mMapView.onLowMemory();
        super.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMaps = googleMap;
        fetch_marker();
    }

    private void preparePinView (String url, final GoogleMap mMap, final LatLng latLng, final String title, final Boolean leader, final String lastFetch) throws NullPointerException {
        builder.include(latLng);
        final View marker;
        if (leader){
            assert getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) != null;
            assert ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)) != null;
            marker = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(illiyin.mhandharbeni.visualize.R.layout.marker_view_leader, null);
        }else{
            assert getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) != null;
            assert ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)) != null;
            marker = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(illiyin.mhandharbeni.visualize.R.layout.marker_view, null);
        }
        final CircleImageView profileimage = marker.findViewById(illiyin.mhandharbeni.visualize.R.id.profileimage);
        Picasso.with(getActivity().getApplicationContext()).load(url).into(profileimage, new com.squareup.picasso.Callback(){
            @Override
            public void onSuccess() {
                try {
                    Address address = new Address(getActivity().getApplicationContext());
                    String sAddress = address.getCurrentAddress(latLng.latitude, latLng.longitude);
                    String sSnippet = "Address : "+sAddress+""
                                        +"Fetch : "+ lastFetch+"";
                    Bitmap markerMember = createDrawableFromView(getActivity().getApplicationContext(), marker);


                    InfoWindowData infoWindowData = new InfoWindowData();
                    infoWindowData.setTitle(title);
                    infoWindowData.setAddress(sAddress);
                    infoWindowData.setLast_fetch(lastFetch);

                    InfoWindowContent infoWindowContent = new InfoWindowContent(getActivity());
                    mMap.setInfoWindowAdapter(infoWindowContent);
                    Marker m = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(title)
                            .snippet(sSnippet)
                            .icon(BitmapDescriptorFactory.fromBitmap(markerMember)));
                    m.setTag(infoWindowData);
                }catch (Exception e){
                    FirebaseCrash.report(e);
                }
            }
            @Override
            public void onError() {
            }
        });

    }
    public static Bitmap createDrawableFromView(Context context, View view) {
        view.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
        view.measure(100, 100);
        view.layout(0, 0, 100, 100);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    private void fetch_zoom(GoogleMap googleMap){
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.20);
        LatLngBounds bounds = builder.build();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));
    }
    private void turnGPSOn(){
        String provider = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(!provider.contains("gps")){ //if gps is disabled
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            getActivity().sendBroadcast(poke);
        }
    }
    private void fetch_marker(){
        googleMaps.clear();
        MemberLocationModel mlm = new MemberLocationModel();
        Crud crud = new Crud(getContext(), mlm);
        RealmResults results = crud.read("id_grup", id);
        Boolean fetchZoom = false;
        if (results.size()>0){
            for (int i=0;i<results.size();i++){
                MemberLocationModel resultLocation = (MemberLocationModel) results.get(i);

                double lats, longs;
                try {
                    Boolean leader = false;
                    assert resultLocation != null;
                    if (resultLocation.getType_member().equalsIgnoreCase("Leader")){
                        leader = true;
                    }
                    lats = Double.valueOf(resultLocation.getLatitude());
                    longs =Double.valueOf(resultLocation.getLongitude());
                    LatLng latLng = new LatLng(lats, longs);
                    String lastFetch = resultLocation.getDate_modified();
                    try {
                        preparePinView(resultLocation.getImage(), googleMaps, latLng, resultLocation.getNama(), leader, lastFetch);
                    }catch (NullPointerException | IllegalArgumentException e){
                        FirebaseCrash.report(e);
                        if (session.getToken().equalsIgnoreCase("nothing")){
                            turnGPSOn();
                        }
                    }

                    fetchZoom = true;
                } catch (Exception e) {
                    FirebaseCrash.report(e);
                    fetchZoom = false;
                }
            }
            if (fetchZoom){
                try {
                    fetch_zoom(googleMaps);
                }catch (Exception e){
                    FirebaseCrash.report(e);
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void sessionChange() {
        String getLocation = session.getCustomParams("CHANGELOCATION", "nothing");
        if (!getLocation.equalsIgnoreCase("nothing")){
            fetch_marker();
        }
    }
    private void checkGps(){
        final LocationManager manager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        assert manager != null;
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(getActivity().getApplicationContext())) {
//            Toast.makeText(NavActivity.this,"Gps already enabled",Toast.LENGTH_SHORT).show();
        }

        if(!hasGPSDevice(getActivity().getApplicationContext())){
            Toast.makeText(getActivity().getApplicationContext(),"Gps not Supported",Toast.LENGTH_SHORT).show();
        }

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(getActivity().getApplicationContext())) {
            enableLoc();
        }else{
            Toast.makeText(getActivity().getApplicationContext(),"Gps already enabled",Toast.LENGTH_SHORT).show();
        }
    }
    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (mgr == null)
            return false;

        final List<String> providers = mgr.getAllProviders();
        return providers != null && providers.contains(LocationManager.GPS_PROVIDER);

    }

    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(connectionResult -> FirebaseCrash.log(connectionResult.getErrorMessage())).build();

            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);
            builder.setNeedBle(true);

            Task<LocationSettingsResponse> result =
                    LocationServices.getSettingsClient(getActivity()).checkLocationSettings(builder.build());
            result.addOnCompleteListener(task -> {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                } catch (ApiException exception) {
                    FirebaseCrash.report(exception);
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(
                                        getActivity(),
                                        REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException | ClassCastException e) {
                                // Ignore the error.
                                FirebaseCrash.report(e);
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }
    }

}