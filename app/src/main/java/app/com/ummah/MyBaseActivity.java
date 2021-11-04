package app.com.ummah;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;


public abstract class MyBaseActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    public abstract void UpdateLocation(Location location, String place);

    PendingResult<LocationSettingsResult> result;
    GoogleApiClient mGoogleApiClient;
    public Location mLocation;
    private LocationManager locationManager;
    private LocationRequest mLocationRequest;
    final static int REQUEST_LOCATION = 199;
    private String strAdd = "";

    public String getCurrentAddressString() {
        // strAdd = "";
        if (mLocation != null) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(mLocation.getLatitude(), mLocation.getLongitude(), 1);
                if (addresses != null) {
                    Address returnedAddress = addresses.get(0);
                    StringBuilder strReturnedAddress = new StringBuilder("");

//                    for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
//                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
//                    }
                  //  strAdd = returnedAddress.getLocality() + ", " + returnedAddress.getCountryName();
                    strAdd = returnedAddress.getLocality();
                    Intent intent = new Intent("LocationUpdate");
                    // You can also include some extra data.
                    intent.putExtra("message", strAdd);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                    Log.w("TAG", strReturnedAddress.toString());
                } else {
                    Log.w("TAG", "No Address returned!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.w("TAG", "Canont get Address!");
            }
        }
        return strAdd;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    public Activity getActivity() {
        return this;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    public void StartGPS() {
        if (mGoogleApiClient.isConnected()) {
            mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(5000);
            mLocationRequest.setFastestInterval(2000);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(mLocationRequest);
            builder.setAlwaysShow(true);

            result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    //final LocationSettingsStates state = result.getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can initialize location
                            // requests here.
                            //

                            mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                            if (mLocation != null) {
                                getCurrentAddressString();
                                // Updatelocation(mLocation);
                                UpdateLocation(mLocation,strAdd);
                                // Blank for a moment...
                                // LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                            } else {
                                startLocationUpdates();
                            }
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(
                                        MyBaseActivity.this,
                                        REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            //...
                            // familyInfoFragment.FillLatLong();
                            break;
                    }
                }
            });
        } else {
            //Toast.makeText(MyBaseActivity.this, "Service Not Enable! Trying to start please try again!", Toast.LENGTH_LONG).show();
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            mGoogleApiClient.connect();
        }
    }


    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        StartGPS();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        getCurrentAddressString();
        UpdateLocation(mLocation,strAdd);
    }


}
