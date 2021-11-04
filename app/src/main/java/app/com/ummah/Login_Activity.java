package app.com.ummah;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import Util.ConnectionDetector;
import Util.Constant;
import Util.GPSTracker;
import Util.SessionManager;
import Util.Utility;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by Abhi on 1/22/2019.
 */

public class Login_Activity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    Button Bt_Sub, bt_register;
    EditText Et_email, Et_pass;
    private ProgressDialog progress_dialog = null;
    TextView Tv_forget_password;
    boolean isPasswordVisible = false;
    SessionManager session;
    LocationManager manager;
    GPSTracker gps;
    Context ct;
    private ConnectionDetector cd;

    //For Facebook
    private static final String EMAIL = "email";
    LoginButton loginButton;
    CallbackManager callbackManager;
    boolean loggedOut;
    Button fb;

    //Google

    private static final String TAG = "GPlusFragent";
    private int RC_SIGN_IN = 0;
    private GoogleApiClient mGoogleApiClient;
    private Button signInButton;
    //    private Button signOutButton;
    private Button disconnectButton;
    private LinearLayout signOutView;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;
    private ImageView Iv_back, imgProfilePic;
    private String token = "";

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();

        Intent intentLogin = new Intent(Login_Activity.this, WalkThroughActivity.class);
        startActivity(intentLogin);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.login_activity);
        ProgressBar_Function();
        All_Depndency();
        Find_Element();
        ClickEvent_Fun();
        Google_APi();
        FaceBook_api();
        printHashKey();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }

    private void Google_APi() {
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.e("Google token", account.getIdToken());
            String idToken = account.getIdToken();
//            Login_FB("","");
            Login_Google(account.getEmail(), account.getIdToken());
            // Signed in successfully, show authenticated UI.
//            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
        }
    }

    //    private void handleSignInResult(GoogleSignInResult completedTask) {
//        try {
//            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//            completedTask.getSignInAccount().
//            // Signed in successfully, show authenticated UI.
//            updateUI(account);
//        } catch (ApiException e) {
//            // The ApiException status code indicates the detailed failure reason.
//            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
//        }
//    }
    // FaceBook
    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("TOKEN", currentAccessToken.getToken());
                        try {
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String email = object.getString("email");
                            String id = object.getString("id");
                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";
                            Login_FB(email, currentAccessToken.getToken());
                            // txtUsername.setText("First Name: " + first_name + "\nLast Name: " + last_name);
                            // txtEmail.setText(email);
                            // Picasso.with(Login_Activity.this).load(image_url).into(imageView);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private void Find_Element() {
        signInButton = (Button) findViewById(R.id.sign_in_button);
//        signOutButton = (Button) findViewById(R.id.sign_out_button);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        Et_email = (EditText) findViewById(R.id.Et_email);
        Et_pass = (EditText) findViewById(R.id.Et_pass);
        Bt_Sub = (Button) findViewById(R.id.bt_submit);
        bt_register = (Button) findViewById(R.id.bt_register);
        Tv_forget_password = (TextView) findViewById(R.id.Tv_forget_password);
        Iv_back = (ImageView) findViewById(R.id.Iv_back);
        fb = (Button) findViewById(R.id.fb);

        Et_pass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (Et_pass.getRight() - Et_pass.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if (!isPasswordVisible) {
                            Log.v("isVisible", "before If");
                            isPasswordVisible = true;
                            Log.v("isVisible", "inside if");
                            Et_pass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.password_hide, 0);
                            Et_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        } else {
                            Log.v("isVisible", "inside else ");
                            isPasswordVisible = false;
                            Et_pass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.password_eye_01, 0);
                            Et_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());

                        }

                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void ClickEvent_Fun() {
        Iv_back.setOnClickListener(this);
        Tv_forget_password.setOnClickListener(this);
        Bt_Sub.setOnClickListener(this);
        bt_register.setOnClickListener(this);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }

        });

//        signOutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
//                        new ResultCallback<Status>() {
//                            @Override
//                            public void onResult(Status status) {
//                                updateUI(false);
//                            }
//                        });
//            }
//
//        });

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
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("MainActivity", "getInstanceId failed", task.getException());
                            return;
                        }

                        token = task.getResult().getToken();

                        Log.d("MainActivity", FirebaseInstanceId.getInstance().getToken());
                    }
                });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }
        cd = new ConnectionDetector(getApplicationContext()); // For Internet Check.
        session = new SessionManager(getApplicationContext());
        ct = this;
//        loggedOut = AccessToken.getCurrentAccessToken() == null; /// Facebook AccessToke
//        // Configure sign-in to request the user's ID, email address, and basic
//        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.server_client_id))
                .build();


//
//        // Build a GoogleApiClient with access to the Google Sign-In API and the
//// options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(Login_Activity.this)
                .enableAutoManage(Login_Activity.this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.Tv_forget_password:
                Forgot_Password();
                break;

            case R.id.bt_submit:
                Login_Fun();
             /*   Intent intent = new Intent(Login_Activity.this, Play_mainActivity.class);
                startActivity(intent);*/
                break;

            case R.id.bt_register:
                Register_Fun();

                break;
            case R.id.Iv_back:
                onBackPressed();
                break;

            case R.id.fb:

                loginButton.performClick();
                break;

//            case R.id.google:
//                // signIn();
//                break;
        }
    }

    private void Login_Fun() {
        if (!Utility.eMailValidation((Et_email.getText().toString()))) {
            String msgs = getString(R.string.text_enter_valid_email);
            Et_email.setError(msgs);
            Et_email.requestFocus();
        } else if (Et_pass.getText().toString().length() == 0) {
            Et_pass.setError("Password not entered");
            Et_pass.requestFocus();
        } else {

            Login_Api();
        }
    }

    private void Login_Api() {
        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.Base_url + "user/login",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        ;
                        Log.e("Login", response);
                        try {

                            JSONObject json = new JSONObject(response);
                            Log.e("Response for Login", json.toString());
                            if (json != null) {

                                if (json.has("success")) {
                                    String success = json.optString("success").toString();
                                   /* String imageurl = json.optString("imageurl").toString();
                                    Constant.imageurl = imageurl;
                                    session.SetProfile(imageurl);*/
                                    if (success.equals("true")) {
                                        String token = json.optString("token").toString();

                                        JSONObject jsonMainNode = json.optJSONObject("data");
                                        /******* Fetch node values **********/
                                        String name = jsonMainNode.optString("name").toString();
                                        String mobile = jsonMainNode.optString("mobile").toString();
                                        String isLoggedIn = jsonMainNode.optString("isLoggedIn").toString();
                                        String access_token = jsonMainNode.optString("access_token").toString();
                                        String nameContactPerson = jsonMainNode.optString("nameContactPerson").toString();
                                        String isFBUser = jsonMainNode.optString("isFBUser").toString();
                                        String avtar = jsonMainNode.optString("avtar").toString();
                                        String street_address = jsonMainNode.optString("street_address").toString();
                                        String country = jsonMainNode.optString("country").toString();
                                        String state = jsonMainNode.optString("state").toString();
                                        String city = jsonMainNode.optString("city").toString();
                                        String zipCode = jsonMainNode.optString("zipCode").toString();
                                        String vefication = jsonMainNode.optString("vefication").toString();
                                        String phoneVerification = jsonMainNode.optString("phoneVerification").toString();
                                        String deletedAt = jsonMainNode.optString("deletedAt").toString();
                                        String updatedAt = jsonMainNode.optString("updatedAt").toString();
                                        String randomToken = jsonMainNode.optString("randomToken").toString();
                                        String role = jsonMainNode.optString("role").toString();
                                        String email = jsonMainNode.optString("email").toString();
                                        String _id = jsonMainNode.optString("_id").toString();
                                        String user_id = jsonMainNode.optString("user_id").toString();
                                        String id = jsonMainNode.optString("id").toString();
                                        String mosque_id = jsonMainNode.optString("mosque_id").toString();
                                        String username = jsonMainNode.optString("username").toString();
                                        String avtar_2 = "";
                                        if (avtar != null && !avtar.equals("null")) {
                                            avtar_2 = Constant.Image_Base_url + avtar;
                                        }

/*

                                        JSONObject jsonMainNode_location = jsonMainNode.optJSONObject("location");
                                        String lat = jsonMainNode_location.optString("lat").toString();
                                        String lng = jsonMainNode_location.optString("lng").toString();*/


                                        JSONObject jsonMainNode_sessions = jsonMainNode.optJSONObject("sessions");
                                        //  String lng = jsonMainNode_location.optString("lng").toString();

                                        // Set Session for...
                                        session.setMobile(mobile);
                                        session.setToken_id(access_token);// set Token in session.
                                        //imageurl = imageurl;
                                        session.SetProfile(avtar_2);
                                        session.setIsEntered();
                                        session.setUserDetail_2(username, mosque_id, mobile, isLoggedIn,
                                                nameContactPerson, isFBUser, email, street_address, country,
                                                state, city, zipCode, vefication, phoneVerification, deletedAt,
                                                updatedAt, randomToken, role, _id, user_id, id);
                                        Intent intent = new Intent(Login_Activity.this, Select_Home_Mosque_Activity.class);
                                        startActivity(intent);
                                        finish();
                                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                        hideProgressDialog();


                                    } else if (success.equals("false")) {

                                        String error = json.optString("error").toString();
                                        hideProgressDialog();
                                        Toast.makeText(Login_Activity.this, error,
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Access denied by server.", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("email", Et_email.getText().toString().trim());
                map.put("password", Et_pass.getText().toString().trim());
                map.put("deviceToken", token);
                map.put("deviceType", "Android");
                return map;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private void Login_FB(String emails, String fbtoken) {
        showProgressDialog();
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("email", "santosh.modi00@gmail.com");
//        map.put("accesstoken", "EAAI9QXeMFFEBAFgv56TlSsmP0Fbcz9k8lCszvICPNwRaat5XsClzmHhCfu7uVEJOlhgsZAq98G1fWCxutx06arIOGbp8QTwwF2rUM9m0J5ihvMLiYl5CgS2djDrhsZB5nmxmGN7WyAq3YAwzYIoZChhMzjhDZBrIqM9ZCYZCFQm6gOV1sB2smJHgQZAkitrV0sITBZCVtvZCb9wZDZD");
//        map.put("provider", "Facebook");
//        map.put("deviceToken", token);
//        map.put("deviceType", "Android");

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
//        RequestBody body = RequestBody.create(mediaType, new JSONObject(map).toString());
        RequestBody body = RequestBody.create(mediaType, "{\r\n  \"email\" : \""+emails+"\",\r\n  \"accesstoken\": \""+fbtoken+"\",\r\n  \"provider\": \"facebook\",\r\n  \"deviceToken\": \""+token+"\",\r\n  \"deviceType\": \"android\"\r\n}");
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(Constant.Base_url + "user/loginfb")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        try {
            okhttp3.Response response = client.newCall(request).execute();

//            Log.e("Response for Login", response.body().string());
            JSONObject json = new JSONObject(response.body().string());
            if (json != null) {

                if (json.has("success")) {
                    String success = json.optString("success").toString();
                                   /* String imageurl = json.optString("imageurl").toString();
                                    Constant.imageurl = imageurl;
                                    session.SetProfile(imageurl);*/
                    if (success.equals("true")) {
                        String token = json.optString("token").toString();

                        JSONObject jsonMainNode = json.optJSONObject("data");
                        /******* Fetch node values **********/
                        String name = jsonMainNode.optString("name").toString();
                        String mobile = jsonMainNode.optString("mobile").toString();
                        String isLoggedIn = jsonMainNode.optString("isLoggedIn").toString();
                        String access_token = jsonMainNode.optString("access_token").toString();
                        String nameContactPerson = jsonMainNode.optString("nameContactPerson").toString();
                        String isFBUser = jsonMainNode.optString("isFBUser").toString();
                        String avtar = jsonMainNode.optString("avtar").toString();
                        String street_address = jsonMainNode.optString("street_address").toString();
                        String country = jsonMainNode.optString("country").toString();
                        String state = jsonMainNode.optString("state").toString();
                        String city = jsonMainNode.optString("city").toString();
                        String zipCode = jsonMainNode.optString("zipCode").toString();
                        String vefication = jsonMainNode.optString("vefication").toString();
                        String phoneVerification = jsonMainNode.optString("phoneVerification").toString();
                        String deletedAt = jsonMainNode.optString("deletedAt").toString();
                        String updatedAt = jsonMainNode.optString("updatedAt").toString();
                        String randomToken = jsonMainNode.optString("randomToken").toString();
                        String role = jsonMainNode.optString("role").toString();
                        String email = jsonMainNode.optString("email").toString();
                        String _id = jsonMainNode.optString("_id").toString();
                        String user_id = jsonMainNode.optString("user_id").toString();
                        String id = jsonMainNode.optString("id").toString();
                        String mosque_id = jsonMainNode.optString("mosque_id").toString();
                        String username = jsonMainNode.optString("username").toString();
                        String avtar_2 = "";
                        if (avtar != null && !avtar.equals("null")) {
                            avtar_2 = Constant.Image_Base_url + avtar;
                        }

/*

                                        JSONObject jsonMainNode_location = jsonMainNode.optJSONObject("location");
                                        String lat = jsonMainNode_location.optString("lat").toString();
                                        String lng = jsonMainNode_location.optString("lng").toString();*/


                        JSONObject jsonMainNode_sessions = jsonMainNode.optJSONObject("sessions");
                        //  String lng = jsonMainNode_location.optString("lng").toString();

                        // Set Session for...
                        session.setMobile(mobile);
                        session.setToken_id(access_token);// set Token in session.
                        //imageurl = imageurl;
                        session.SetProfile(avtar_2);
                        session.setIsEntered();
                        session.setUserDetail_2(username, mosque_id, mobile, isLoggedIn,
                                nameContactPerson, isFBUser, email, street_address, country,
                                state, city, zipCode, vefication, phoneVerification, deletedAt,
                                updatedAt, randomToken, role, _id, user_id, id);
                        Intent intent = new Intent(Login_Activity.this, Select_Home_Mosque_Activity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);



                    } else if (success.equals("false")) {

                        String error = json.optString("error").toString();
                        hideProgressDialog();
                        Toast.makeText(Login_Activity.this, error,
                                Toast.LENGTH_LONG).show();
                    }
                }
            }

            hideProgressDialog();
            System.out.println(response.body());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            hideProgressDialog(); }

    }

    private void Login_Google(String emails, String fbtoken) {


//        VolleyLog.DEBUG = true;
        Map<String, String> map = new HashMap<String, String>();
        map.put("email", emails);
        map.put("accesstoken", fbtoken);
        map.put("provider", "Google");
        map.put("deviceToken", token);
        map.put("deviceType", "Android");

        showProgressDialog();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
//        RequestBody body = RequestBody.create(mediaType, new JSONObject(map).toString());
//        RequestBody body = RequestBody.create(mediaType, "{\r\n  \"email\" : \"santosh.modi00@gmail.com\",\r\n  \"accesstoken\": \"EAAI9QXeMFFEBAFgv56TlSsmP0Fbcz9k8lCszvICPNwRaat5XsClzmHhCfu7uVEJOlhgsZAq98G1fWCxutx06arIOGbp8QTwwF2rUM9m0J5ihvMLiYl5CgS2djDrhsZB5nmxmGN7WyAq3YAwzYIoZChhMzjhDZBrIqM9ZCYZCFQm6gOV1sB2smJHgQZAkitrV0sITBZCVtvZCb9wZDZD\",\r\n  \"provider\": \"facebook\",\r\n  \"deviceToken\": \"2342342342\",\r\n  \"deviceType\": \"android\"\r\n}");
        RequestBody body = RequestBody.create(mediaType, "{\r\n  \"email\" : \""+emails+"\",\r\n  \"accesstoken\": \""+fbtoken+"\",\r\n  \"provider\": \"google\",\r\n  \"deviceToken\": \""+token+"\",\r\n  \"deviceType\": \"android\"\r\n}");
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(Constant.Base_url + "user/logingoogle")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        try {
            okhttp3.Response response = client.newCall(request).execute();

//            Log.e("Response for Login", response.body().string());
            JSONObject json = new JSONObject(response.body().string());
             if (json != null) {

                if (json.has("success")) {
                    String success = json.optString("success").toString();
                                   /* String imageurl = json.optString("imageurl").toString();
                                    Constant.imageurl = imageurl;
                                    session.SetProfile(imageurl);*/
                    if (success.equals("true")) {
                        String token = json.optString("token").toString();

                        JSONObject jsonMainNode = json.optJSONObject("data");
                        /******* Fetch node values **********/
                        String name = jsonMainNode.optString("name").toString();
                        String mobile = jsonMainNode.optString("mobile").toString();
                        String isLoggedIn = jsonMainNode.optString("isLoggedIn").toString();
                        String access_token = jsonMainNode.optString("access_token").toString();
                        String nameContactPerson = jsonMainNode.optString("nameContactPerson").toString();
                        String isFBUser = jsonMainNode.optString("isFBUser").toString();
                        String avtar = jsonMainNode.optString("avtar").toString();
                        String street_address = jsonMainNode.optString("street_address").toString();
                        String country = jsonMainNode.optString("country").toString();
                        String state = jsonMainNode.optString("state").toString();
                        String city = jsonMainNode.optString("city").toString();
                        String zipCode = jsonMainNode.optString("zipCode").toString();
                        String vefication = jsonMainNode.optString("vefication").toString();
                        String phoneVerification = jsonMainNode.optString("phoneVerification").toString();
                        String deletedAt = jsonMainNode.optString("deletedAt").toString();
                        String updatedAt = jsonMainNode.optString("updatedAt").toString();
                        String randomToken = jsonMainNode.optString("randomToken").toString();
                        String role = jsonMainNode.optString("role").toString();
                        String email = jsonMainNode.optString("email").toString();
                        String _id = jsonMainNode.optString("_id").toString();
                        String user_id = jsonMainNode.optString("user_id").toString();
                        String id = jsonMainNode.optString("id").toString();
                        String mosque_id = jsonMainNode.optString("mosque_id").toString();
                        String username = jsonMainNode.optString("username").toString();
                        String avtar_2 = "";
                        if (avtar != null && !avtar.equals("null")) {
                            avtar_2 = Constant.Image_Base_url + avtar;
                        }

/*

                                        JSONObject jsonMainNode_location = jsonMainNode.optJSONObject("location");
                                        String lat = jsonMainNode_location.optString("lat").toString();
                                        String lng = jsonMainNode_location.optString("lng").toString();*/


                        JSONObject jsonMainNode_sessions = jsonMainNode.optJSONObject("sessions");
                        //  String lng = jsonMainNode_location.optString("lng").toString();

                        // Set Session for...
                        session.setMobile(mobile);
                        session.setToken_id(access_token);// set Token in session.
                        //imageurl = imageurl;
                        session.SetProfile(avtar_2);
                        session.setIsEntered();
                        session.setUserDetail_2(username, mosque_id, mobile, isLoggedIn,
                                nameContactPerson, isFBUser, email, street_address, country,
                                state, city, zipCode, vefication, phoneVerification, deletedAt,
                                updatedAt, randomToken, role, _id, user_id, id);
                        Intent intent = new Intent(Login_Activity.this, Select_Home_Mosque_Activity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);



                    } else if (success.equals("false")) {

                        String error = json.optString("error").toString();
                        hideProgressDialog();
                        Toast.makeText(Login_Activity.this, error,
                                Toast.LENGTH_LONG).show();
                    }
                }
            }

            hideProgressDialog();
            System.out.println(response.body());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            hideProgressDialog(); }

    }


    private void Forgot_Password() {

        Intent intent = new Intent(Login_Activity.this, ForgetPassword_Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void Register_Fun() {

        Intent intent = new Intent(Login_Activity.this, User_registeration_activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        //  finish();
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

    private void printHashKey() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "app.com.ummah",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                System.out.println("KeyHash ------ >>> " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    private void FaceBook_api() {
        //For Facebook


//        if (!loggedOut) {
//            //Picasso.with(this).load(Profile.getCurrentProfile().getProfilePictureUri(200, 200)).into(imageView);
//            //Log.d("TAG", "Username is: " + Profile.getCurrentProfile().getName());
//            //Using Graph API
//            getUserProfile(AccessToken.getCurrentAccessToken());
//        }


        AccessTokenTracker fbTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken2) {
                if (accessToken2 == null) {
                    //  txtUsername.setText(null);
                    //txtEmail.setText(null);
                    //imageView.setImageResource(0);
                    Toast.makeText(getApplicationContext(), "User Logged Out.", Toast.LENGTH_LONG).show();
                }
            }
        };
        fbTracker.startTracking();
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                //loginResult.getAccessToken();
                //loginResult.getRecentlyDeniedPermissions()
                //loginResult.getRecentlyGrantedPermissions()
                boolean loggedOut = AccessToken.getCurrentAccessToken() == null;

                if (!loggedOut) {
                    // Picasso.with(Login_Activity.this).load(Profile.getCurrentProfile().getProfilePictureUri(200, 200)).into(imageView);
                    //Log.d("TAG", "Username is: " + Profile.getCurrentProfile().getName());
                    // Using Graph API
                    getUserProfile(AccessToken.getCurrentAccessToken());
                }

            }

            @Override
            public void onCancel() {
                // App cod
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    // code for google int.


    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            //handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    //handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }


    /**
     * Background Async task to load user profile picture from url
     */
    @SuppressLint("StaticFieldLeak")
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... uri) {
            String url = uri[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {

            if (result != null) {


                Bitmap resized = Bitmap.createScaledBitmap(result, 200, 200, true);
                //    bmImage.setImageBitmap(ImageHelper.getRoundedCornerBitmap(getContext(),resized,250,200,200, false, false, false, false));

            }
        }
    }
}
