package app.com.ummah;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Util.ConnectionDetector;
import Util.Constant;
import Util.SessionManager;
import butterknife.ButterKnife;

/**
 * Created by Dell on 25-03-2019.
 */

public class Profile_Activity extends AppCompatActivity implements View.OnClickListener {

    TextView Tv_name, Tv_Email;
    ImageView Iv_back, Iv_Profile;
    LinearLayout Lay_My_mosque, lay_My_favorite_Mosque, Lay_Setting, Lay_Today_feed, Lay_My_Donation;
    ImageView Iv_image_show;

    private static final String TAG = Profile_Activity.class.getSimpleName();
    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;
    public static final int REQUEST_IMAGE = 100;
    static Bitmap encodedString;
    private RequestQueue rQueue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        ButterKnife.bind(this);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        All_Depndency();
        requestMultiplePermissions();
        ProgressBar_Function();
        Find_Element();
        Click_Event();
    }

    private void Click_Event() {

        Iv_back.setOnClickListener(this);
        Lay_My_mosque.setOnClickListener(this);
        lay_My_favorite_Mosque.setOnClickListener(this);
        Lay_Setting.setOnClickListener(this);
        Lay_Today_feed.setOnClickListener(this);
        Lay_My_Donation.setOnClickListener(this);
        Iv_image_show.setOnClickListener(this);


    }


    private void Find_Element() {
        Iv_image_show = (ImageView) findViewById(R.id.Iv_image_show);
        Tv_Email = (TextView) findViewById(R.id.Tv_Email);
        Tv_name = (TextView) findViewById(R.id.Tv_name);
        Iv_back = (ImageView) findViewById(R.id.Iv_back);
        Lay_My_mosque = (LinearLayout) findViewById(R.id.Lay_My_mosque);
        lay_My_favorite_Mosque = (LinearLayout) findViewById(R.id.lay_My_favorite_Mosque);
        Lay_Setting = (LinearLayout) findViewById(R.id.Lay_Setting);
        Lay_Today_feed = (LinearLayout) findViewById(R.id.Lay_Today_feed);
        Lay_My_Donation = (LinearLayout) findViewById(R.id.Lay_My_Donation);
        if (session.checkEntered()) {
            String url =  session.getProfile().get(Constant.SHARED_PREFERENCE_Profile_pic_KEY);
            Tv_name.setText(session.getuserinfo().get(Constant.SHARED_PREFERENCE_NAME_KEY));
            Tv_Email.setText(session.getuserinfo().get(Constant.SHARED_PREFERENCE_EMAIL_KEY));

            if (url.length()>0) {


                Iv_image_show.setImageBitmap(getBitmapFromURL(url));
                //Iv_image_show.setImageURI(Uri.parse(url));
                //Glide.with(this).load(url).into(Iv_image_show);
              //  Iv_image_show.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));


            } else {
                Iv_image_show.setImageResource(R.drawable.imgpsh_fullsize_anim);
            }
        } else {
            Iv_image_show.setImageResource(R.drawable.imgpsh_fullsize_anim);
            Tv_Email.setVisibility(View.GONE);

        }


    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.Iv_back:
                onBackPressed();
                break;

            case R.id.Iv_image_show:

                Dexter.withActivity(this)
                        .withPermissions(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {
                                    showImagePickerOptions();
                                }

                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    showSettingsDialog();
                                }
                            }


                            @Override
                            public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
                break;

            case R.id.Lay_My_mosque:
                Intent intent = new Intent(Profile_Activity.this, My_Home_Mosque_Activity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.Lay_Setting:

                Intent intentSetting = new Intent(Profile_Activity.this, Setting_Activity.class);
                intentSetting.putExtra("fromProfile", true);
                startActivity(intentSetting);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.lay_My_favorite_Mosque:

                Intent intent_favorite = new Intent(Profile_Activity.this, Favorite_Mosque_Activity.class);
                startActivity(intent_favorite);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.Lay_Today_feed:
                break;
            case R.id.Lay_My_Donation:

                Intent intent_donation = new Intent(Profile_Activity.this, My_Donation_Activity.class);
                startActivity(intent_donation);
//                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;


        }
    }

    private void requestMultiplePermissions() {
        Dexter.withActivity(this)
                .withPermissions(

                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            //   Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }


    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Profile_Activity.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(Profile_Activity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(Profile_Activity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Profile_Activity.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    System.out.println(" bitmap  ----- >>" + bitmap);
                    encodedString = bitmap;
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    // Must compress the Image to reduce image size to make upload easy
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                    byte[] byte_arr = stream.toByteArray();
                    // Encode Image to String
                    String encodedString___ = Base64.encodeToString(byte_arr, 0);
                    //codedString = uri.toString();
                    System.out.println("encodedString--- >>> " + encodedString);
                    //session.SetProfile(encodedString___);
                    // loading profile image from local cache
                    loadProfile(uri.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap_Photo to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    System.out.println(" bitmap_Photo  ----- >>" + bitmap);
                    encodedString = bitmap;
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    // Must compress the Image to reduce image size to make upload easy
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                    byte[] byte_arr = stream.toByteArray();
                    // Encode Image to String
                    //encodedString = Base64.encodeToString(byte_arr, 0);
                    //codedString = uri.toString();
                    System.out.println("encodedString--- >>> " + encodedString);

                    // loading profile image from local cache
                    loadProfile(uri.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    private void loadProfile(String url) {
        Log.d(TAG, "Image cache path: " + url);
        Glide.with(this).load(url)
                .into(Iv_image_show);
        Iv_image_show.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));
        uploadImage();
    }

    public void showProgressDialog() {

        if (!progress_dialog.isShowing()) {
            progress_dialog.show();
        }
    }

    public void hideProgressDialog() {
        if (progress_dialog.isShowing()) {
            progress_dialog.dismiss();
        }
    }

    private void uploadImage() {

        showProgressDialog();
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, Constant.Add_Profile_Pic,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.d("Profile pic Update", new String(response.data));
                        rQueue.getCache().clear();
                        try {
                            JSONObject jsonObject = new JSONObject(new String(response.data));
                                // Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                hideProgressDialog();
                                //String message = jsonObject.getString("message");
                                String success = jsonObject.getString("success");
                                if (success.equals("true")) {
                                    JSONObject jsonMainNode = jsonObject.optJSONObject("data");

                                    /******* Fetch node values **********/
                                    String avtar = jsonMainNode.optString("avtar").toString();
                                    String avtar_2 = "http://167.172.131.53:4002"+avtar;
                                    Iv_image_show.setImageBitmap(getBitmapFromURL(avtar_2));
                                    session.SetProfile(avtar_2);
                                    hideProgressDialog();

                                }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
               // params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Authorization", "Bearer " + session.getToken_id().get(Constant.SHARED_PREFERENCE_TOKEN_KEY));
                return params;
            }


            /*
             *pass files using below method
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("image", new DataPart(imagename + ".png", getFileDataFromDrawable(encodedString)));
                return params;
            }
        };


        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rQueue = Volley.newRequestQueue(Profile_Activity.this);
        rQueue.add(volleyMultipartRequest);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
