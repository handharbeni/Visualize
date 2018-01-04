package illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.crash.FirebaseCrash;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import illiyin.mhandharbeni.databasemodule.MemberLocationModel;
import illiyin.mhandharbeni.realmlibrary.Crud;
import illiyin.mhandharbeni.sessionlibrary.Session;
import illiyin.mhandharbeni.sessionlibrary.SessionListener;
import illiyin.mhandharbeni.visualize.R;
import io.realm.RealmResults;

/**
 * Created by root on 10/25/17.
 */

public class MapsList extends Fragment implements OnMapReadyCallback, SessionListener {

    private MapView mMapView;
    private GoogleMap googleMaps;
    private Integer id;
    private Session session;
    private LatLngBounds.Builder builder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.__navactivity_mainnav_layout_group_maps, container,
                false);
        mMapView = v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
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
            turnGPSOn();
        }

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

    private void preparePinView (String url, final GoogleMap mMap, final LatLng latLng, final String title, final Boolean leader) throws NullPointerException {
        builder.include(latLng);
        final View marker;
        if (leader){
            assert getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) != null;
            marker = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker_view_leader, null);
        }else{
            assert getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) != null;
            marker = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker_view, null);
        }
        final CircleImageView profileimage = marker.findViewById(R.id.profileimage);
        Picasso.with(getActivity().getApplicationContext()).load(url).into(profileimage, new com.squareup.picasso.Callback(){
            @Override
            public void onSuccess() {
                try {
                    Bitmap markerMember = createDrawableFromView(getActivity().getApplicationContext(), marker);
                    mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(title)
                            .icon(BitmapDescriptorFactory.fromBitmap(markerMember)));
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
                    try {
                        preparePinView(resultLocation.getImage(), googleMaps, latLng, resultLocation.getNama(), leader);
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
}