package app.com.ummah;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
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
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
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
 * Created by Dell on 28-03-2019.
 */

public class User_registeration_activity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {


    EditText Et_email;


    ImageView Iv_back, imgProfilePic;
    EditText et_user_name, Et_Phone, Et_pass, Et_Conf_pass;
    Button Button_Registration;
    private ProgressDialog progress_dialog = null;
    private String token = "";
    boolean isPasswordVisible = false;
    SessionManager session;
    LocationManager manager;
    GPSTracker gps;
    Context ct;
    private ConnectionDetector cd;

    private int RC_SIGN_IN = 0;
    boolean loggedOut;
    //For Facebook
    private static final String EMAIL = "email";
    LoginButton loginButton;
    CallbackManager callbackManager;
    //    ImageView imageView;
//    TextView txtUsername, txtEmail;
//    boolean loggedOut;
    Button fb;

    private GoogleApiClient mGoogleApiClient;
    private Button signInButton;
    private Button signOutButton;
    private static final String TAG = "GPlusFragent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_signup);
        All_Depndency();
        ProgressBar_Function();
        FindElement();
        FaceBook_api();
        clickEvent();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }


    private void clickEvent() {
        Iv_back.setOnClickListener(this);
        Button_Registration.setOnClickListener(this);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }

        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                updateUI(false);
                            }
                        });
            }

        });

    }

    private void FindElement() {

        signInButton = (Button) findViewById(R.id.sign_in_button);
        signOutButton = (Button) findViewById(R.id.sign_out_button);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        fb = (Button) findViewById(R.id.fb);
        Iv_back = (ImageView) findViewById(R.id.Iv_back);
        et_user_name = (EditText) findViewById(R.id.et_user_name);
        Et_email = (EditText) findViewById(R.id.et_mail);
        Et_Phone = (EditText) findViewById(R.id.Et_Phone);
        Et_pass = (EditText) findViewById(R.id.Et_pass);
        Et_Conf_pass = (EditText) findViewById(R.id.Et_Conf_pass);
        Button_Registration = (Button) findViewById(R.id.Button_Registration);
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

        Et_Conf_pass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (Et_Conf_pass.getRight() - Et_Conf_pass.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if (!isPasswordVisible) {
                            Log.v("isVisible", "before If");
                            isPasswordVisible = true;
                            Log.v("isVisible", "inside if");
                            Et_Conf_pass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.password_hide, 0);
                            Et_Conf_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        } else {
                            Log.v("isVisible", "inside else ");
                            isPasswordVisible = false;
                            Et_Conf_pass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.password_eye_01, 0);
                            Et_Conf_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());

                        }

                        return true;
                    }
                }
                return false;
            }
        });
    }
//
//    private void handleSignInResult(GoogleSignInResult result) {
//        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
//        if (result.isSuccess()) {
//            // Signed in successfully, show authenticated UI.
//            GoogleSignInAccount acct = result.getSignInAccount();
//            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
//            //Similarly you can get the email and photourl using acct.getEmail() and  acct.getPhotoUrl()
//
//            if (acct.getPhotoUrl() != null)
//                new LoadProfileImage(imgProfilePic).execute(acct.getPhotoUrl().toString());
//
//            updateUI(true);
//        } else {
//            // Signed out, show unauthenticated UI.
//            updateUI(false);
//        }
//    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            signInButton.setVisibility(View.GONE);
            signOutButton.setVisibility(View.VISIBLE);
        } else {
//            mStatusTextView.setText(R.string.signed_out);
            //   Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.user_default);
            // imgProfilePic.setImageBitmap(ImageHelper.getRoundedCornerBitmap(getContext(),icon, 200, 200, 200, false, false, false, false));
            signInButton.setVisibility(View.VISIBLE);
            signOutButton.setVisibility(View.GONE);
        }
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
        RequestBody body = RequestBody.create(mediaType, "{\r\n  \"email\" : \"" + emails + "\",\r\n  \"accesstoken\": \"" + fbtoken + "\",\r\n  \"provider\": \"facebook\",\r\n  \"deviceToken\": \"" + token + "\",\r\n  \"deviceType\": \"android\"\r\n}");
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
                        Intent intent = new Intent(User_registeration_activity.this, Select_Home_Mosque_Activity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


                    } else if (success.equals("false")) {

                        String error = json.optString("error").toString();
                        hideProgressDialog();
                        Toast.makeText(User_registeration_activity.this, error,
                                Toast.LENGTH_LONG).show();
                    }
                }
            }

            hideProgressDialog();
            System.out.println(response.body());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            hideProgressDialog();
        }

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
                        Intent intent = new Intent(User_registeration_activity.this, Select_Home_Mosque_Activity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


                    } else if (success.equals("false")) {

                        String error = json.optString("error").toString();
                        hideProgressDialog();
                        Toast.makeText(User_registeration_activity.this, error,
                                Toast.LENGTH_LONG).show();
                    }
                }
            }

            hideProgressDialog();
            System.out.println(response.body());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            hideProgressDialog();
        }

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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
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

    // FaceBook
    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("TAG", object.toString());
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

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.Iv_back:

                onBackPressed();
                break;

            case R.id.Button_Registration:
                Register();
                break;

            case R.id.fb:

                loginButton.performClick();
                break;

//            case R.id.google:
//                //  signIn();
//                break;
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(User_registeration_activity.this, UserType_Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void Register() {
        if (et_user_name.getText().toString().length() < 2) {
            et_user_name.setError(getString(R.string.pls_enter_username));
            et_user_name.requestFocus();
        } else if (!Utility.eMailValidation((Et_email.getText().toString()))) {
            String msgs = getString(R.string.text_enter_valid_email);
            Et_email.setError(msgs);
            Et_email.requestFocus();
        } else if (Et_Phone.getText().toString().length() < 12) {
            Et_Phone.setError(getString(R.string.pls_enter_mobile_no));
            Et_Phone.requestFocus();
        } else if (Et_pass.getText().toString().length() == 0) {
            Et_pass.setError(getString(R.string.pls_enter_password));
            Et_pass.requestFocus();
        } else if (Et_Conf_pass.getText().toString().length() == 0) {
            Et_Conf_pass.setError(getString(R.string.confirm_password));
            Et_Conf_pass.requestFocus();
        } else if (!Et_pass.getText().toString().equals(Et_Conf_pass.getText().toString())) {
            Et_Conf_pass.setError(getString(R.string.password_match));
            Et_Conf_pass.requestFocus();
        } else {

            postUsingVolley();

        }
    }

    private void postUsingVolley() {

        showProgressDialog();

        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("username", et_user_name.getText().toString().trim());
        map.put("email", Et_email.getText().toString().trim());
        map.put("password", Et_pass.getText().toString().trim());
        map.put("mobile", "+" + Et_Phone.getText().toString().trim());
        map.put("isFBUser", "false");
        map.put("role", "user");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constant.Base_url + "user/register", new JSONObject(map),
                new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("register ", response.toString());
                        if (response.has("success")) {
                            String success = response.optString("success").toString();
                            if (success.equals("true")) {
                                String message = response.optString("message").toString();
                                if (message.equals("Signup is completed, you can login now!")) {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(User_registeration_activity.this, Login_Activity.class);
                                    startActivity(intent);
                                    //finish();
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                    hideProgressDialog();
                                }
                            } else if (success.equals("false")) {
                                String message = response.optString("message").toString();
                                String error = response.optString("error").toString();
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                hideProgressDialog();
                            }
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(8000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjReq);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void All_Depndency() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }
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

        cd = new ConnectionDetector(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        ct = this;
        loggedOut = AccessToken.getCurrentAccessToken() == null; /// Facebook AccessToke
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.server_client_id))
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
// options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(User_registeration_activity.this)
                .enableAutoManage(User_registeration_activity.this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void ProgressBar_Function() {
        if (progress_dialog == null) {
            progress_dialog = new ProgressDialog(this);
        }
        progress_dialog.setMessage(getResources().getString(R.string.Please_wait));
        progress_dialog.setCancelable(true);
        progress_dialog.setCanceledOnTouchOutside(false);
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


}
