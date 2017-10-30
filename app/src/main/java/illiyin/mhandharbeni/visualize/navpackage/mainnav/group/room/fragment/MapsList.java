package illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import illiyin.mhandharbeni.databasemodule.MemberLocationModel;
import illiyin.mhandharbeni.realmlibrary.Crud;
import illiyin.mhandharbeni.sessionlibrary.Session;
import illiyin.mhandharbeni.sessionlibrary.SessionListener;
import illiyin.mhandharbeni.visualize.R;
import io.realm.RealmResults;

import static android.content.ContentValues.TAG;

/**
 * Created by root on 10/25/17.
 */

public class MapsList extends Fragment implements OnMapReadyCallback, SessionListener {

    private MapView mMapView;
    private GoogleMap googleMaps;
    private Integer id;
    private Session session;
    private LatLngBounds.Builder builder;
    private MarkerOptions marker;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fetch_extras();
        fetch_module();
        View v = inflater.inflate(R.layout.__navactivity_mainnav_layout_group_maps, container,
                false);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);

        return v;
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
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMaps = googleMap;
        fetch_marker();
    }
    private void create_marker(GoogleMap googleMap, Double latitude, Double longitude, String name){
        LatLng latLng = new LatLng(latitude, longitude);
        builder.include(latLng);
        marker = new MarkerOptions().position(
                new LatLng(latitude, longitude)).title(name);
        marker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
        googleMap.addMarker(marker);
    }
    private void fetch_zoom(GoogleMap googleMap){
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.20);
        LatLngBounds bounds = builder.build();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));
    }
    private void fetch_marker(){
        googleMaps.clear();
        MemberLocationModel mlm = new MemberLocationModel();
        Crud crud = new Crud(getActivity().getApplicationContext(), mlm);
        RealmResults results = crud.read("id_grup", id);
        if (results.size()>0){
            for (int i=0;i<results.size();i++){
                MemberLocationModel resultLocation = (MemberLocationModel) results.get(i);
                create_marker(googleMaps, Double.valueOf(resultLocation.getLatitude()), Double.valueOf(resultLocation.getLongitude()), resultLocation.getNama());
            }
            fetch_zoom(googleMaps);
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