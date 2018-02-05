package illiyin.mhandharbeni.visualize.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import illiyin.mhandharbeni.visualize.utils.model.InfoWindowData;
import illiyin.mhandharbeni.visualize.R;

/**
 * Created by root on 1/22/18.
 */

public class InfoWindowContent implements GoogleMap.InfoWindowAdapter {
    private Context context;

    public InfoWindowContent(Context context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.__layout_info_window_content, null);

        TextView title = view.findViewById(R.id.txtTitle);
        TextView alamat = view.findViewById(R.id.txtAlamat);
        TextView last_fetch = view.findViewById(R.id.txtLastFetch);

        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();


        assert infoWindowData != null;
        title.setText(infoWindowData.getTitle());
        alamat.setText("Address: "+infoWindowData.getAddress());
        last_fetch.setText("Last Active: "+infoWindowData.getLast_fetch());

        return view;
    }
}
