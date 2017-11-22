package illiyin.mhandharbeni.visualize.navpackage.mainnav.group.room.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import illiyin.mhandharbeni.databasemodule.GrupLocationModel;
import illiyin.mhandharbeni.libraryroute.Navigation;
import illiyin.mhandharbeni.realmlibrary.Crud;
import illiyin.mhandharbeni.visualize.R;
import io.realm.RealmResults;

public class RouteDestinations extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    protected LatLng start;
    protected LatLng end;
    private String id;
    private ArrayList<GrupLocationModel> listLocationGrup;

    private GrupLocationModel grupLocationModel;
    private Crud crud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetch_modul();
        fetch_id();

        setContentView(R.layout.activity_route_destinations);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        fetch_data();
    }
    private void fetch_modul(){
        grupLocationModel = new GrupLocationModel();
        crud = new Crud(getApplicationContext(), grupLocationModel);
    }
    private void fetch_id(){
        Bundle bs = getIntent().getExtras();
        id = String.valueOf(bs.getInt("key_id_int", 0));
    }
    private void fetch_data(){
        RealmResults results = crud.read("id_grup", Integer.valueOf(id));
        if (results.size() > 0){
            LatLng start;
            LatLng end;
            for (int i=0;i<results.size();i++){
                GrupLocationModel glm = (GrupLocationModel) results.get(i);
                start = new LatLng(Double.valueOf(glm.getLatitude()), Double.valueOf(glm.getLongitude()));
                if (i+1 < results.size()){
                    GrupLocationModel glm2 = (GrupLocationModel) results.get(i+1);

                    end = new LatLng(Double.valueOf(glm2.getLatitude()), Double.valueOf(glm2.getLongitude()));
                }else{
                    end = new LatLng(Double.valueOf(glm.getLatitude()), Double.valueOf(glm.getLongitude()));
                }
                fetch_line(start, end);
            }
        }
    }
    private void fetch_line(LatLng start, LatLng end){
        Navigation nav = new Navigation(mMap,start,end,getApplicationContext(), this);
        nav.find(
                true,
                false,
                bitmapDescriptorFromVector(getApplicationContext(), R.drawable.marker_destinations),
                bitmapDescriptorFromVector(getApplicationContext(), R.drawable.marker_destinations)
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
}
