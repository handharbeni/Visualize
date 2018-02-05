package illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import illiyin.mhandharbeni.realmlibrary.Crud;
import fr.quentinklein.slt.LocationTracker;
import illiyin.mhandharbeni.databasemodule.GrupLocationModel;
import illiyin.mhandharbeni.libraryroute.Navigation;
import io.realm.RealmResults;

public class RouteDestinations extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    private GoogleMap mMap;
    protected LatLng start;
    protected LatLng end;
    private String id;

    private Crud crud;
    private LatLngBounds.Builder builder;

    private LocationTracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(illiyin.mhandharbeni.visualize.R.layout.activity_route_destinations);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetch_modul();
        fetch_id();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(illiyin.mhandharbeni.visualize.R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        fetch_data();
        enableMyLocation();
    }

    private void fetch_modul() {
        GrupLocationModel grupLocationModel = new GrupLocationModel();
        crud = new Crud(getApplicationContext(), grupLocationModel);
        builder = new LatLngBounds.Builder();
    }

    private void fetch_id() {
        Bundle bs = getIntent().getExtras();
        assert bs != null;
        id = String.valueOf(bs.getInt("key_id_int", 0));
    }

    private void fetch_data() {
        RealmResults results = crud.read("id_grup", Integer.valueOf(id));
        if (results.size() > 0) {
            LatLng start;
            LatLng end;
            for (int i = 0; i < results.size(); i++) {
                GrupLocationModel glm = (GrupLocationModel) results.get(i);
                assert glm != null;
                start = new LatLng(Double.valueOf(glm.getLatitude()), Double.valueOf(glm.getLongitude()));
                if (i + 1 < results.size()) {
                    GrupLocationModel glm2 = (GrupLocationModel) results.get(i + 1);

                    end = new LatLng(Double.valueOf(glm2.getLatitude()), Double.valueOf(glm2.getLongitude()));
                } else {
                    end = new LatLng(Double.valueOf(glm.getLatitude()), Double.valueOf(glm.getLongitude()));
                }
                fetch_line(start, end);
            }
            zoomMaps();
        }
    }

    private void fetch_line(LatLng start, LatLng end) {
        builder.include(start);
        builder.include(end);

        Navigation nav = new Navigation(mMap, start, end, getApplicationContext(), this);
        nav.find(
                true,
                false,
                bitmapDescriptorFromVector(getApplicationContext(), illiyin.mhandharbeni.visualize.R.drawable.ic_place_destinations_png),
                bitmapDescriptorFromVector(getApplicationContext(), illiyin.mhandharbeni.visualize.R.drawable.ic_place_destinations_png)
        );
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void zoomMaps() {
        LatLngBounds latLngBounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.10);

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(latLngBounds, width, height, padding);

        mMap.animateCamera(cu);
    }

    private void enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                builder.include(loc);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
    }
}
