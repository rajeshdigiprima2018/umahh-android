package app.com.ummah;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import Adapter.WalkThroughPagerAdapter;
import Util.Constant;
import Util.LocationTrack;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class WalkThroughActivity extends AppCompatActivity implements View.OnClickListener {
    Context mContext;
    Activity mActivity;
    ViewPager mViewPager;
    TextView Tv_skip;

    ImageView circleImage1, circleImage2, circleImage3, circleImage4, nextImageView;
    Button Bt_Login, Bt_Sign_Up;
    WalkThroughPagerAdapter mAdapter;
    private int pageSelectInt = 0;
    private boolean isLastPage = false;

    //Location...

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_through);
        mContext = WalkThroughActivity.this;
        mActivity = WalkThroughActivity.this;
        Tv_skip = findViewById(R.id.Tv_skip);
        Tv_skip.setOnClickListener(this);
        findId();
        clickLisner();
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.
        setData();
        if (permissionsToRequest.size() > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        }

        Location_find();


    }

    private void Location_find() {

        locationTrack = new LocationTrack(WalkThroughActivity.this);
        if (locationTrack.canGetLocation()) {


            Constant.longitude = String.valueOf(locationTrack.getLongitude());
            Constant.latitude = String.valueOf(locationTrack.getLatitude());

            System.out.println("Longitude --->>  " + Constant.longitude);
            System.out.println("latitude --->>  " + Constant.latitude);
            // Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(Double.parseDouble(Constant.longitude)) + "\nLatitude:" + Double.toString(Double.parseDouble(Constant.latitude)), Toast.LENGTH_SHORT).show();
        } else {

            locationTrack.showSettingsAlert();
        }
    }

    private void clickLisner() {
        nextImageView.setOnClickListener(this);
        circleImage1.setOnClickListener(this);
        circleImage2.setOnClickListener(this);
        circleImage3.setOnClickListener(this);
        circleImage4.setOnClickListener(this);

        Bt_Login.setOnClickListener(this);
        Bt_Sign_Up.setOnClickListener(this);
        final ImageView[] circleImages = {circleImage1, circleImage2, circleImage3, circleImage4};

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.v("walkThrough", "Page scroll " + position);
                if (position == 3 && positionOffset == 0 && !isLastPage) {
                    if (pageSelectInt != 0) {
                        isLastPage = true;
                        //     getPathologyList();
                    }
                    pageSelectInt++;
                } else {
                    pageSelectInt = 0;

                }

                for (int i = 0; i < circleImages.length; i++) {
                    if (i == position) {
                        circleImages[i].setImageResource(R.drawable.fill_circle);

                    } else {
                        circleImages[i].setImageResource(R.drawable.holo_circle);

                    }

                }
            }

            @Override
            public void onPageSelected(int position) {
//pageSelectInt=position;
                Log.v("walkThrough", "page Select :" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.v("walkThrough", "onPageScrollStateChanged" + state);
            }
        });

    }

    private void setData() {
        String imageText[] = getResources().getStringArray(R.array.walkThroughDescription);
        int[] sliderImageArray = {R.drawable.wt_1, R.drawable.wt_2, R.drawable.wt_3};
        String[] headerArray = getResources().getStringArray(R.array.walkThroughHeaderArray);
        mAdapter = new WalkThroughPagerAdapter(mContext, mActivity, headerArray, sliderImageArray, imageText);
        mViewPager.setAdapter(mAdapter);


    }

    private void findId() {
        mViewPager = (ViewPager) findViewById(R.id.imageViewPager);
        circleImage1 = (ImageView) findViewById(R.id.circleImage1);
        circleImage2 = (ImageView) findViewById(R.id.circleImage2);
        circleImage3 = (ImageView) findViewById(R.id.circleImage3);
        circleImage4 = (ImageView) findViewById(R.id.circleImage4);
        nextImageView = (ImageView) findViewById(R.id.nextImageView);
        Bt_Login = (Button) findViewById(R.id.Bt_Login);
        Bt_Sign_Up = (Button) findViewById(R.id.Bt_Sign_Up);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextImageView:

                //     getPathologyList();
                break;
            case R.id.circleImage1:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.circleImage2:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.circleImage3:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.circleImage4:
                mViewPager.setCurrentItem(3);
                break;

            case R.id.Tv_skip:


                Intent intentskip = new Intent(WalkThroughActivity.this, MainActivity.class);
                startActivity(intentskip);
                finish();
                // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                break;
            case R.id.Bt_Login:

                Intent intentLogin = new Intent(WalkThroughActivity.this, Login_Activity.class);
                startActivity(intentLogin);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                break;
            case R.id.Bt_Sign_Up:

                Intent intent = new Intent(WalkThroughActivity.this, UserType_Activity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                break;
        }

    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(WalkThroughActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationTrack.stopListener();
    }


 /*   private void getPathologyList() {
        Dialogs mDialog = new Dialogs(mContext, mActivity);
        mDialog.setProgress();

        Map<String, String> requestDataMap = new HashMap<>();
        requestDataMap.put("sortColumn", "name");
        requestDataMap.put("sortOrder", "DESC");
        requestDataMap.put("pageSize", "10");
        requestDataMap.put("pageOffset", "0");
        requestDataMap.put("page", "1");
        PathologyManagement getRequest = new PathologyManagement(mContext, mActivity, mDialog, requestDataMap);
        getRequest.getPathologyList();

    }*/
}
