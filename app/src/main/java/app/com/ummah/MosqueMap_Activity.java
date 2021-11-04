package app.com.ummah;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import Util.ConnectionDetector;
import Util.Constant;
import Util.SessionManager;

/**
 * Created by Dell on 22-05-2019.
 */

public class MosqueMap_Activity extends AppCompatActivity implements OnMapReadyCallback , View.OnClickListener{


    //Google Map
    private GoogleMap mMap;
    private MapView mapView;
    ImageView Iv_back;
    private static final String TAG = "Qibla_Activity";
    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    private ProgressDialog progress_dialog;
    Double Lat,Lang;
    Marker marker;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mosque_map_activity);
        mapView = (MapView)findViewById(R.id.map);
        All_Depndency();
        Find_Element();

        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
        ProgressBar_Function();
        Click_Event();

    }

    private void ProgressBar_Function() {
        if (progress_dialog == null) {
            progress_dialog = new ProgressDialog(this);
        }
        progress_dialog.setMessage(getResources().getString(R.string.Please_wait));
        progress_dialog.setCancelable(true);
        progress_dialog.setCanceledOnTouchOutside(false);
    }

    private void All_Depndency() {
        session = new SessionManager(this);
        Lat = Double.valueOf(getIntent().getStringExtra("lat"));
        Lang = Double.valueOf(getIntent().getStringExtra("lng"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }
        cd = new ConnectionDetector(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        ct = this;

    }

    private void Click_Event() {
       Iv_back.setOnClickListener(this);
    }

    private void Find_Element() {

        Iv_back = (ImageView)findViewById(R.id.Iv_back);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.Iv_back:
                onBackPressed();
                break;

        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(MosqueMap_Activity.this, Mousque_detail_activity.class);

        intent.putExtra("id", getIntent().getStringExtra("id"));
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            if (ActivityCompat.checkSelfPermission(MosqueMap_Activity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
              //  requestReadPhoneMapPermission();
                //requestMap_access_fine_location();
                return;
            }
        } catch (Exception e) {
        }
        mMap = googleMap;
        mMap.animateCamera(CameraUpdateFactory.zoomTo(2), 2000, null);
        mMap.setTrafficEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if( Constant.Map_type.equals("Normal")){
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }else if( Constant.Map_type.equals("Satellite")){
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }else if ( Constant.Map_type.equals("Hybrid")){
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }else {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }

       // MarkerOptions marker = new MarkerOptions();
       // marker.position(new LatLng(Lat, Lat)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        marker = mMap.addMarker(new MarkerOptions().position(
                (new LatLng(Lat, Lang))).icon(BitmapDescriptorFactory.fromResource(R.drawable.mosque_2)).anchor(0.5f, 0.5f));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(Lat, Lang)).zoom(12).build();
        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            private float currentZoom = -1;

            @Override
            public void onCameraChange(CameraPosition position) {
                if (position.zoom != currentZoom) {
                    currentZoom = position.zoom;  // here you get zoom level
                    try {
                        Toast.makeText(MosqueMap_Activity.this, (int) currentZoom, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {

                    }

                }
            }
        });
    }


}
