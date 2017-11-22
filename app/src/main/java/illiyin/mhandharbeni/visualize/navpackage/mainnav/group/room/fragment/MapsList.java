package illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.squareup.picasso.NetworkPolicy;
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
    private View preparePinView (String url, final GoogleMap mMap, final LatLng latLng, final String title) {
        builder.include(latLng);
        final View marker = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker_view, null);
        final CircleImageView profileimage = marker.findViewById(R.id.profileimage);
        Picasso.with(getActivity().getApplicationContext()).load(url).into(profileimage, new com.squareup.picasso.Callback(){
            @Override
            public void onSuccess() {
                Bitmap markerMember = createDrawableFromView(getActivity().getApplicationContext(), marker);
                Marker pinMarker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(title)
                        .icon(BitmapDescriptorFactory.fromBitmap(markerMember)));
            }
            @Override
            public void onError() {

                Toast.makeText(getActivity().getApplicationContext(), "Error On Picasso", Toast.LENGTH_SHORT).show();
            }
        });

        return marker;
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
    private void fetch_marker(){
        googleMaps.clear();
        MemberLocationModel mlm = new MemberLocationModel();
        Crud crud = new Crud(getActivity().getApplicationContext(), mlm);
        RealmResults results = crud.read("id_grup", id);
        Boolean fetchZoom = false;
        if (results.size()>0){
            for (int i=0;i<results.size();i++){
                MemberLocationModel resultLocation = (MemberLocationModel) results.get(i);
                double lats, longs;
                try {
                    lats = new Double(resultLocation.getLatitude());
                    longs =new Double(resultLocation.getLongitude());
                    LatLng latLng = new LatLng(lats, longs);
                    preparePinView(resultLocation.getImage(), googleMaps, latLng, resultLocation.getNama());
                    fetchZoom = true;
                } catch (NumberFormatException e) {
                    lats = 0;
                    longs = 0;
                    fetchZoom = false;
                }
            }
            if (fetchZoom){
                fetch_zoom(googleMaps);
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