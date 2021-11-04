package app.com.ummah;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import Util.ConnectionDetector;
import Util.Constant;
import Util.GPSTracker;
import Util.SessionManager;

/**
 * Created by Dell on 26-03-2019.
 */

public class Qibla_Activity extends AppCompatActivity implements View.OnClickListener , SensorEventListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private float currentDegree = 0f;
    private float currentDegreeNeedle = 0f;



    private static final String TAG = "Qibla_Activity";
    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;
    LinearLayout lay_umarah, lay_hajj;
    ImageView Iv_back;

    // device sensor manager
    private SensorManager SensorManage;
    // define the compass picture that will be use
    private ImageView compassimage,Iv_compass_image2;
    // record the angle turned of the compass picture
    private float DegreeStart = 0f;
    TextView DegreeTV;
    GPSTracker gps;

    int mAzimuth;
    private SensorManager mSensorManager;
    private Sensor mRotationV, mAccelerometer, mMagnetometer;
    boolean haveSensor = false, haveSensor2 = false;
    float[] rMat = new float[9];
    float[] orientation = new float[3];
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;

    //Google Map
    private GoogleMap mMap;
    private MapView mapView;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 0;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    private LocationManager locationManager;
    private GoogleApiClient mGoogleApiClient;
    private LocationManager mLocationManager;
    private Location mLocation;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qibla_activity);
        mapView = (MapView)findViewById(R.id.map);
        All_Depndency();
        Find_Element();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addConnectionCallbacks(Qibla_Activity.this)
                .addOnConnectionFailedListener(Qibla_Activity.this)
                .addApi(LocationServices.API)
                .build();
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        gps = new GPSTracker(this);
        session = new SessionManager(this);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
        ProgressBar_Function();

        Compass();
        //  checkLocation(); //check whether location service is enable or not in your  phone
        Click_Event();
        start();
    }

    /**
     * function to load map. If map is not created it will create it for you
     * */
  /*  private void initilizeMap() {


    }*/
    private void Compass() {
        compassimage = (ImageView) findViewById(R.id.Iv_compass_image);
        // TextView that will display the degree
        DegreeTV = (TextView) findViewById(R.id.DegreeTV);
        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    private void getMyLocation() {


    /*    MarkerOptions marker = new MarkerOptions();
//            mMap.clear();
        marker.position(new LatLng(latitude_, longitude_)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        // marker.position(new LatLng(latitude_, longitude_)).icon(BitmapDescriptorFactory.fromResource(R.drawable.gray_small_1)).title(session.getuserinfo().get(Constant.SHARED_PREFERENCE_city_KEY)).snippet(session.getuserinfo().get(Constant.SHARED_PREFERENCE_zipCode_KEY));
       *//* CameraPosition cameraPosition = new CameraPosition.Builder()
         *//*           .target(new LatLng(latitude_, longitude_)).zoom(18).build();*//**//*
        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));*//*
        mMap.addMarker(marker);*/


    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rMat, event.values);
            mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(rMat, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(rMat, orientation);
            mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
        }

        mAzimuth = Math.round(mAzimuth);
        compassimage.setRotation(-mAzimuth);
//        Iv_compass_image2.setRotation(-mAzimuth);
        String where = "NW";

        if (mAzimuth >= 350 || mAzimuth <= 10)
            where = "N";
        if (mAzimuth < 350 && mAzimuth > 280)
            where = "NW";
        if (mAzimuth <= 280 && mAzimuth > 260)
            where = "W";
        if (mAzimuth <= 260 && mAzimuth > 190)
            where = "SW";
        if (mAzimuth <= 190 && mAzimuth > 170)
            where = "S";
        if (mAzimuth <= 170 && mAzimuth > 100)
            where = "SE";
        if (mAzimuth <= 100 && mAzimuth > 80)
            where = "E";
        if (mAzimuth <= 80 && mAzimuth > 10)
            where = "NE";

        if( MyApps.LOCATION!=null) {
            Location userLoc = new Location("service Provider");
            //get longitudeM Latitude and altitude of current location with gps class and  set in userLoc
            userLoc.setLongitude(  MyApps.LOCATION.getLongitude());
            userLoc.setLatitude( MyApps.LOCATION.getLatitude());
            userLoc.setAltitude( MyApps.LOCATION.getAltitude());


            float degree = Math.round(event.values[0]);
            float head = Math.round(event.values[0]);
            Location destinationLoc = new Location("service Provider");

            destinationLoc.setLatitude(21.422487); //kaaba latitude setting
            destinationLoc.setLongitude(39.826206); //kaaba longitude setting
            float bearTo = userLoc.bearingTo(destinationLoc);
            //  txt_compass.setText(mAzimuth + "° " + where);

            GeomagneticField geoField = new GeomagneticField(Double.valueOf(userLoc.getLatitude()).floatValue(), Double
                    .valueOf(userLoc.getLongitude()).floatValue(),
                    Double.valueOf(userLoc.getAltitude()).floatValue(),
                    System.currentTimeMillis());
            head -= geoField.getDeclination(); // converts magnetic north into true north

            if (bearTo < 0) {
                bearTo = bearTo + 360;
                //bearTo = -100 + 360  = 260;
            }

//This is where we choose to point it
            float direction = bearTo - head;

// If the direction is smaller than 0, add 360 to get the rotation clockwise.
            if (direction < 0) {
                direction = direction + 360;
            }
//        tvHeading.setText("Heading: " + Float.toString(degree) + " degrees" );

            RotateAnimation raQibla = new RotateAnimation(currentDegreeNeedle, direction, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            raQibla.setDuration(210);
            raQibla.setFillAfter(true);

//        arrow.startAnimation(raQibla);

            currentDegreeNeedle = direction;

// create a rotation animation (reverse turn degree degrees)
            RotateAnimation ra = new RotateAnimation(currentDegree, -degree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

// how long the animation will take place
            ra.setDuration(210);


// set the animation after the end of the reservation status
            ra.setFillAfter(true);

// Start the animation
            Iv_compass_image2.setRotation(-degree);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void start() {
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == null) {
            if ((mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) || (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null)) {
                noSensorsAlert();
            }
            else {
                mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                haveSensor = mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
                haveSensor2 = mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
            }
        }
        else{
            mRotationV = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            haveSensor = mSensorManager.registerListener(this, mRotationV, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void noSensorsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Your device doesn't support the Compass.")
                .setCancelable(false)
                .setNegativeButton("Close",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    public void stop() {
        if (haveSensor) {
            mSensorManager.unregisterListener(this, mRotationV);
        }
        else {
            mSensorManager.unregisterListener(this, mAccelerometer);
            mSensorManager.unregisterListener(this, mMagnetometer);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        start();

/*
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {


                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    // layout_map.setVisibility(View.INVISIBLE);
                    onBackPressed();
                    return true;

                }

                return false;
            }
        });*/
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
        Iv_compass_image2= (ImageView) findViewById(R.id.Iv_compass_image2);

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

        Intent intent = new Intent(Qibla_Activity.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            if (ActivityCompat.checkSelfPermission(Qibla_Activity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                requestReadPhoneMapPermission();
                requestMap_access_fine_location();
                return;
            }
        } catch (Exception e) {
        }
        mMap = googleMap;
        // mMap.animateCamera(CameraUpdateFactory.zoomTo((float) 2.6), 2000, null);
        //  mMap.getUiSettings().setMapToolbarEnabled(true);
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

        MarkerOptions marker = new MarkerOptions();
//            mMap.clear();
        marker.position(new LatLng(20.413374, 56.230194)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        // marker.position(new LatLng(latitude_, longitude_)).icon(BitmapDescriptorFactory.fromResource(R.drawable.gray_small_1)).title(session.getuserinfo().get(Constant.SHARED_PREFERENCE_city_KEY)).snippet(session.getuserinfo().get(Constant.SHARED_PREFERENCE_zipCode_KEY));
       /* CameraPosition cameraPosition = new CameraPosition.Builder()
     /*           .target(new LatLng(latitude_, longitude_)).zoom(18).build();*//*
        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));*/
        mMap.addMarker(marker);
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            private float currentZoom = -1;

            @Override
            public void onCameraChange(CameraPosition position) {
                if (position.zoom != currentZoom) {
                    currentZoom = position.zoom;  // here you get zoom level
                    try {
                        Toast.makeText(Qibla_Activity.this, (int) currentZoom, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {

                    }

                }
            }
        });
    }

    private void requestReadPhoneMapPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)) {

            new android.app.AlertDialog.Builder(this)
                    .setTitle("Permission Req")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Qibla_Activity.this,
                                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                        }
                    })
                    //  .setIcon(R.drawable.arrow)
                    .show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
        }
    }

    private void requestMap_access_fine_location() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            new android.app.AlertDialog.Builder(this)
                    .setTitle("Permission Req")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Qibla_Activity.this,
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                        }
                    })
                    // .setIcon(R.drawable.icon)
                    .show();
        } else {
            ActivityCompat.requestPermissions(Qibla_Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void showAlert() {
        final androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(getApplicationContext());
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")

                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
        new android.app.AlertDialog.Builder(getApplicationContext(), R.style.MyDialogStyle);

    }

    @Override
    public void onConnected(Bundle bundle) {
        try {

            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestReadPhoneMapPermission();
                requestMap_access_fine_location();
            }
        } catch (Exception e) {

        }

        //startLocationUpdates();
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLocation == null) {
            //  startLocationUpdates();
        }
        if (mLocation != null) {
            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        }/* else {
         //   Toast.makeText(getActivity(), "Location not Detected", Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

}
