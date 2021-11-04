package Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Modal.Select_Home_Mosque_Modal;
import Util.Constant;
import Util.GPSTracker;
import Util.SessionManager;
import app.com.ummah.All_Mosque_Activity;
import app.com.ummah.Mousque_detail_activity;
import app.com.ummah.R;


/**
 * Created by Abhi on 8/14/2018.
 */
public class Fragment_Map extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, AdapterView.OnItemSelectedListener {

    double latitude2, longitude2;
    static int val = 1;
    private static final int REQUEST_PERMISSIONS = 100;
    Marker mPositionMarker;
    private GoogleMap mMap;
    private MapView mapView;
    SessionManager session;
    double latitude;
    double longitude;
    static double latitude_;
    static double longitude_;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 0;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    int Role_id;
    GPSTracker gps;
    ImageView imgMyLocation;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL;
    private long FASTEST_INTERVAL;
    private static final String TAG = "Play_mainActivity";
    private LocationManager locationManager;
    float bearing;
    Marker marker;
    LinearLayout Lay_near_mosque;
    private FloatingActionButton fab;
    boolean boolean_permission;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_activity, container, false);
        mapView = (MapView) view.findViewById(R.id.map);
        EditText Spinner_map = (EditText) view.findViewById(R.id.Spinner_map);
        Lay_near_mosque = (LinearLayout) view.findViewById(R.id.Lay_near_mosque);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
//        startLocationUpdates();
        val = 1;
        setHasOptionsMenu(true);
        imgMyLocation = (ImageView) view.findViewById(R.id.imgMyLocation);
        gps = new GPSTracker(getActivity());
        session = new SessionManager(getActivity());
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
        Spinner_map.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Mosque_Citybytext(Spinner_map.getText().toString());
                    return true;
                }
                return false;
            }
        });
    /*    if (Role_id == 2) {
            UPDATE_INTERVAL = 500;  *//* 10 secs *//*
            FASTEST_INTERVAL = 500; *//* 2 sec *//*
        } else if (Role_id == 3 || Role_id == 4) {
            UPDATE_INTERVAL = 8 * 1000;  *//* 10 secs *//*
            FASTEST_INTERVAL = 3000; *//* 2 sec *//*
        }*/
        //Drawer.toolbar.setTitle("Invite & Earn");
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        checkLocation(); //check whether location service is enable or not in your  phone
        Mosque_City();
        imgMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getMyLocation();

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), All_Mosque_Activity.class);
                startActivity(intent);
                /* getActivity().finish();*/
                getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
            }
        });

        fn_permission();
        MapsInitializer.initialize(this.getActivity());

//        Spinner_map.setOnItemSelectedListener(this);
//        List<String> categories = new ArrayList<String>();
//        categories.add("All");
//        categories.add("Mosque");
////        categories.add("Business/Organization");
//        // Creating adapter for spinner
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
//
//        // Drop down layout style - list view with radio button
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // attaching data adapter to spinner
//        Spinner_map.setAdapter(dataAdapter);
        return view;
    }

    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION))) {


            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION

                        },
                        REQUEST_PERMISSIONS);

            }
        } else {
            boolean_permission = true;
        }
    }

    private void getMyLocation() {


        MarkerOptions marker = new MarkerOptions();
//            mMap.clear();
        marker.position(new LatLng(latitude_, longitude_)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        // marker.position(new LatLng(latitude_, longitude_)).icon(BitmapDescriptorFactory.fromResource(R.drawable.gray_small_1)).title(session.getuserinfo().get(Constant.SHARED_PREFERENCE_city_KEY)).snippet(session.getuserinfo().get(Constant.SHARED_PREFERENCE_zipCode_KEY));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude_, longitude_)).zoom(18).build();
        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
        mMap.addMarker(marker);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);
        mMap.setTrafficEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (Constant.Map_type.equals("Normal")) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        } else if (Constant.Map_type.equals("Satellite")) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else if (Constant.Map_type.equals("Hybrid")) {
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        } else {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            private float currentZoom = -1;

            @Override
            public void onCameraChange(CameraPosition position) {
                if (position.zoom != currentZoom) {
                    currentZoom = position.zoom;  // here you get zoom level
                    try {
                        Toast.makeText(getActivity(), (int) currentZoom, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {

                    }

                }
            }
        });


    }

    private void requestReadPhoneMapPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)) {

            new AlertDialog.Builder(getActivity())
                    .setTitle("Permission Req")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                        }
                    })
                    //  .setIcon(R.drawable.arrow)
                    .show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
        }
    }

    private void requestMap_access_fine_location() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)) {

            new AlertDialog.Builder(getActivity())
                    .setTitle("Permission Req")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                        }
                    })
                    // .setIcon(R.drawable.icon)
                    .show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void Mosque_City() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Base_url + "mosque/nearby/" + Constant.latitude + "/" + Constant.longitude + "/10000",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Mosque list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("Mosque List", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        // modalList.clear();
                                        JSONArray jsonMainNode = json.optJSONArray("data");
                                        int lengthJsonArr = jsonMainNode.length();

                                        for (int i = 0; i < lengthJsonArr; i++) {
                                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                                            String _id = jsonChildNode.optString("_id").toString();
                                            String username = jsonChildNode.optString("username").toString();
                                            String role = jsonChildNode.optString("role").toString();
                                            String randomToken = jsonChildNode.optString("randomToken").toString();
                                            String updatedAt = jsonChildNode.optString("updatedAt").toString();
                                            String createdAt = jsonChildNode.optString("createdAt").toString();
                                            String phoneVerification = jsonChildNode.optString("phoneVerification").toString();
                                            String vefication = jsonChildNode.optString("vefication").toString();
                                            String zipCode = jsonChildNode.optString("zipCode").toString();
                                            String category = jsonChildNode.optString("category").toString();
                                            String city = jsonChildNode.optString("city").toString();
                                            String state = jsonChildNode.optString("state").toString();
                                            String country = jsonChildNode.optString("country").toString();
                                            String street_address = jsonChildNode.optString("street_address").toString();
                                            Double lng = Double.valueOf(jsonChildNode.optString("lng").toString());
                                            Double lat = Double.valueOf(jsonChildNode.optString("lat").toString());
                                            String deviceToken = jsonChildNode.optString("deviceToken").toString();
                                            String avtar = jsonChildNode.optString("avtar").toString();
                                            String businessType = jsonChildNode.optString("businessType").toString();
                                            String isSelect = jsonChildNode.optString("isSelect").toString();
                                            String isFBUser = jsonChildNode.optString("isFBUser").toString();
                                            String nameContactPerson = jsonChildNode.optString("nameContactPerson").toString();
                                            String access_token = jsonChildNode.optString("access_token").toString();
                                            String isLoggedIn = jsonChildNode.optString("isLoggedIn").toString();
                                            String mobile = jsonChildNode.optString("mobile").toString();
                                            String password = jsonChildNode.optString("password").toString();
                                            String deletedAt = jsonChildNode.optString("deletedAt").toString();
                                            String email = jsonChildNode.optString("email").toString();

                                            if ((!lng.equals("NaN")) && (!lng.equals("0.0")) && (!lng.equals(""))) {
                                                System.out.println("Long---->>  " + lng);
                                                marker = mMap.addMarker(new MarkerOptions().position(
                                                        (new LatLng(lat, lng))).icon(BitmapDescriptorFactory.fromResource(R.drawable.mosque_2)).anchor(0.5f, 0.5f));

                                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                                        .target(new LatLng(lat, lng)).zoom(12).build();
                                                mMap.animateCamera(CameraUpdateFactory
                                                        .newCameraPosition(cameraPosition));
                                                marker.setSnippet(city);
                                                marker.isVisible();
                                                marker.setTitle(username);
                                                // mMap.addMarker(marker).showInfoWindow();
                                            }
                                        }
                                    } else {
                                        Toast.makeText(getActivity(), "No Data Found.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
    private void Mosque_Citybytext(String txt) {
mMap.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.Base_url + "mosque/searchMosque",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Mosque list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("Mosque List", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        // modalList.clear();
                                        JSONArray jsonMainNode = json.optJSONArray("data");
                                        int lengthJsonArr = jsonMainNode.length();

                                        for (int i = 0; i < lengthJsonArr; i++) {
                                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                                            String _id = jsonChildNode.optString("_id").toString();
                                            String username = jsonChildNode.optString("username").toString();
                                            String role = jsonChildNode.optString("role").toString();
                                            String randomToken = jsonChildNode.optString("randomToken").toString();
                                            String updatedAt = jsonChildNode.optString("updatedAt").toString();
                                            String createdAt = jsonChildNode.optString("createdAt").toString();
                                            String phoneVerification = jsonChildNode.optString("phoneVerification").toString();
                                            String vefication = jsonChildNode.optString("vefication").toString();
                                            String zipCode = jsonChildNode.optString("zipCode").toString();
                                            String category = jsonChildNode.optString("category").toString();
                                            String city = jsonChildNode.optString("city").toString();
                                            String state = jsonChildNode.optString("state").toString();
                                            String country = jsonChildNode.optString("country").toString();
                                            String street_address = jsonChildNode.optString("street_address").toString();
                                            Double lng = Double.valueOf(jsonChildNode.optString("lng").toString());
                                            Double lat = Double.valueOf(jsonChildNode.optString("lat").toString());
                                            String deviceToken = jsonChildNode.optString("deviceToken").toString();
                                            String avtar = jsonChildNode.optString("avtar").toString();
                                            String businessType = jsonChildNode.optString("businessType").toString();
                                            String isSelect = jsonChildNode.optString("isSelect").toString();
                                            String isFBUser = jsonChildNode.optString("isFBUser").toString();
                                            String nameContactPerson = jsonChildNode.optString("nameContactPerson").toString();
                                            String access_token = jsonChildNode.optString("access_token").toString();
                                            String isLoggedIn = jsonChildNode.optString("isLoggedIn").toString();
                                            String mobile = jsonChildNode.optString("mobile").toString();
                                            String password = jsonChildNode.optString("password").toString();
                                            String deletedAt = jsonChildNode.optString("deletedAt").toString();
                                            String email = jsonChildNode.optString("email").toString();

                                            if ((!lng.equals("NaN")) && (!lng.equals("0.0")) && (!lng.equals(""))) {
                                                System.out.println("Long---->>  " + lng);
                                                marker = mMap.addMarker(new MarkerOptions().position(
                                                        (new LatLng(lat, lng))).icon(BitmapDescriptorFactory.fromResource(R.drawable.mosque_2)).anchor(0.5f, 0.5f));

                                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                                        .target(new LatLng(lat, lng)).zoom(12).build();
                                                mMap.animateCamera(CameraUpdateFactory
                                                        .newCameraPosition(cameraPosition));
                                                marker.setSnippet(city);
                                                marker.isVisible();
                                                marker.setTitle(username);
                                                // mMap.addMarker(marker).showInfoWindow();
                                            }
                                        }
                                    } else {
                                        Toast.makeText(getActivity(), "No Data Found.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("searchmosque", txt);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    public void onBackPressed() {
        // openExitDialog();
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }


    @Override
    public void onConnected(Bundle bundle) {
        try {

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

//    protected void startLocationUpdates() {
//        // Create the location request
//        mLocationRequest = LocationRequest.create()
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                .setInterval(UPDATE_INTERVAL)
//                .setFastestInterval(FASTEST_INTERVAL);
//        // Request location updates
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            return;
//        }
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//        Log.d("reque", "--->>>>");
    // }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        latitude_ = location.getLatitude();
        longitude_ = location.getLongitude();


        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        try {
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(18).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            try {
                mPositionMarker = mMap.addMarker(new MarkerOptions().flat(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_green_color_location))
                        .anchor(0.5f, 0.5f).rotation(bearing)
                        .flat(true)
                        .position(new LatLng(location.getLatitude(), location.getLongitude())));
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }


    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void showAlert() {
        final androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
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
        new AlertDialog.Builder(requireContext(), R.style.MyDialogStyle);

    }


    @Override
    public void onResume() {

        super.onResume();
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
        });

        // getActivity().registerReceiver(broadcastReceiver, new IntentFilter(GoogleService.str_receiver));
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (marker != null) {
            try {
                JSONObject js = new JSONObject(marker.getTag().toString());
                Intent intent = new Intent(getActivity(), Mousque_detail_activity.class);
                intent.putExtra("id", js.optString("_id").toString());
                intent.putExtra("just_finish", true);
                startActivity(intent);
            } catch (Exception ee) {
                ee.printStackTrace();
            }

        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String item = parent.getItemAtPosition(position).toString();
        ((TextView) view).setTextColor(getResources().getColorStateList(R.color.white));
        ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColorStateList(R.color.white));
        // Set the text color of drop down items
        ((TextView) view).setTextColor(Color.WHITE);
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

        switch (item) {
            case "All":
                Filte_Method("all");
                break;
            case "Mosque":
                Filte_Method("mosque");
                break;
            case "Business/Organization":

                Filte_Method("business");
                break;
        }


    }

    private void Filte_Method(final String all) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.Base_url + "user/getAllRoleUsers/{role}",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Filter list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("Filter List", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        mMap.clear();
                                        JSONArray jsonMainNode = json.optJSONArray("data");
                                        int lengthJsonArr = jsonMainNode.length();

                                        for (int i = 0; i < lengthJsonArr; i++) {
                                            //   Select_Home_Mosque_Modal select_home_mosque_modal = new Select_Home_Mosque_Modal();
                                            Select_Home_Mosque_Modal selectHomeMosqueModal = new Select_Home_Mosque_Modal();
                                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                                            String _id = jsonChildNode.optString("_id").toString();
                                            String username = jsonChildNode.optString("username").toString();
                                            String role = jsonChildNode.optString("role").toString();
                                            String randomToken = jsonChildNode.optString("randomToken").toString();
                                            String updatedAt = jsonChildNode.optString("updatedAt").toString();
                                            String createdAt = jsonChildNode.optString("createdAt").toString();
                                            String phoneVerification = jsonChildNode.optString("phoneVerification").toString();
                                            String vefication = jsonChildNode.optString("vefication").toString();
                                            String zipCode = jsonChildNode.optString("zipCode").toString();
                                            String category = jsonChildNode.optString("category").toString();
                                            String city = jsonChildNode.optString("city").toString();
                                            String state = jsonChildNode.optString("state").toString();
                                            String country = jsonChildNode.optString("country").toString();
                                            String street_address = jsonChildNode.optString("street_address").toString();
                                            Double lng = Double.valueOf(jsonChildNode.optString("lng").toString());
                                            Double lat = Double.valueOf(jsonChildNode.optString("lat").toString());
                                            String deviceToken = jsonChildNode.optString("deviceToken").toString();
                                            String avtar = jsonChildNode.optString("avtar").toString();
                                            String businessType = jsonChildNode.optString("businessType").toString();
                                            String isSelect = jsonChildNode.optString("isSelect").toString();
                                            String isFBUser = jsonChildNode.optString("isFBUser").toString();
                                            String nameContactPerson = jsonChildNode.optString("nameContactPerson").toString();
                                            String access_token = jsonChildNode.optString("access_token").toString();
                                            String isLoggedIn = jsonChildNode.optString("isLoggedIn").toString();
                                            String mobile = jsonChildNode.optString("mobile").toString();
                                            String password = jsonChildNode.optString("password").toString();
                                            String deletedAt = jsonChildNode.optString("deletedAt").toString();
                                            String email = jsonChildNode.optString("email").toString();

                                            if ((!lng.equals("NaN")) && (!lng.equals("0.0")) && (!lng.equals(""))) {
                                                System.out.println("Long---->>  " + lng);


                                                Marker marker = mMap.addMarker(new MarkerOptions().position(
                                                        (new LatLng(lat, lng)))
                                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.mosque_small)).anchor(0.5f, 0.5f));

                                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                                        .target(new LatLng(lat, lng)).zoom(12).build();
                                                mMap.animateCamera(CameraUpdateFactory
                                                        .newCameraPosition(cameraPosition));
                                                marker.setSnippet(street_address);
                                                marker.setTag(jsonChildNode);
                                                marker.isVisible();

                                                marker.setTitle(username);
                                                // mMap.addMarker(marker).showInfoWindow();

                                            }

                                        }


                                    } else {
                                        Toast.makeText(getActivity(), "No Data Found.", Toast.LENGTH_LONG).show();
                                    }
                                }

                           /*     if (modalList.size() > 0) {
                                    selectMosque = new Select_mousque_Adapter(Select_Home_Mosque_Activity.this, modalList);
                                    recyclerView.setAdapter(selectMosque);
                                    selectMosque.notifyDataSetChanged();
                                }*/
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("role", all);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
